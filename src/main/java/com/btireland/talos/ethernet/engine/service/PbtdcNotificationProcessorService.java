package com.btireland.talos.ethernet.engine.service;


import com.btireland.talos.ethernet.engine.domain.Access;
import com.btireland.talos.ethernet.engine.domain.AccessInstall;
import com.btireland.talos.ethernet.engine.domain.Address;
import com.btireland.talos.ethernet.engine.domain.Contact;
import com.btireland.talos.ethernet.engine.domain.CustomerDelay;
import com.btireland.talos.ethernet.engine.domain.Location;
import com.btireland.talos.ethernet.engine.domain.LogicalLink;
import com.btireland.talos.ethernet.engine.domain.ParkedNotifications;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.domain.RejectionDetails;
import com.btireland.talos.ethernet.engine.domain.Site;
import com.btireland.talos.ethernet.engine.domain.SupplierOrder;
import com.btireland.talos.ethernet.engine.domain.TerminatingEquipment;
import com.btireland.talos.ethernet.engine.dto.NotificationDTO;
import com.btireland.talos.ethernet.engine.dto.PBTDCGlanIdDTO;
import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import com.btireland.talos.ethernet.engine.exception.PBTDCOrderClientException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceNotFoundException;
import com.btireland.talos.ethernet.engine.soap.notifications.EthernetServiceEndpoint;
import com.btireland.talos.ethernet.engine.soap.notifications.EthernetServiceEndpoint.SITE.SITECONTACT;
import com.btireland.talos.ethernet.engine.soap.notifications.Notification;
import com.btireland.talos.ethernet.engine.soap.notifications.NotificationData;
import com.btireland.talos.ethernet.engine.soap.notifications.ServiceDetails;
import com.btireland.talos.ethernet.engine.util.BTRejectCode;
import com.btireland.talos.ethernet.engine.util.CircuitTypes;
import com.btireland.talos.ethernet.engine.util.ContactTypes;
import com.btireland.talos.ethernet.engine.util.LocationType;
import com.btireland.talos.ethernet.engine.util.NotificationProcessedStatus;
import com.btireland.talos.ethernet.engine.util.OrderStatus;
import com.btireland.talos.ethernet.engine.util.SiteTypes;
import com.btireland.talos.ethernet.engine.util.XmlUtils;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class PbtdcNotificationProcessorService {

    private PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService;
    private ParkedNotificationsPersistenceService parkedNotificationsPersistenceService;
    private ObjectMapper objectMapper;
    private PBTDCBusinessStatusService pbtdcBusinessStatusService;
    private OrderManagerService orderManagerService;

    public PbtdcNotificationProcessorService(
            PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService,
            ParkedNotificationsPersistenceService parkedNotificationsPersistenceService,
            ObjectMapper objectMapper, PBTDCBusinessStatusService pbtdcBusinessStatusService,
            OrderManagerService orderManagerService) {
        this.pbtdcOrdersPersistenceService = pbtdcOrdersPersistenceService;
        this.parkedNotificationsPersistenceService = parkedNotificationsPersistenceService;
        this.objectMapper = objectMapper;
        this.pbtdcBusinessStatusService = pbtdcBusinessStatusService;
        this.orderManagerService = orderManagerService;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processUndeliverable(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate QuoteOrder with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrderId in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);

            //Update Orders with supplier information
            pbtdcOrder.setRejectionDetails(RejectionDetails.builder().rejectCode(BTRejectCode.REJECT_CODE_105.getRejectCode()).rejectReason(notification.getNOTIFICATIONDATA().getREJECTREASON()).build());
            pbtdcOrder.getRejectionDetails().setOrders(pbtdcOrder);

            pbtdcBusinessStatusService.updateBusinessStatusForUndelivered(pbtdcOrder.getBusinessStatus());

            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processAppointmentRequest(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);

            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate QuoteOrder with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrderId in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);

            pbtdcOrder.setStatusText(notification.getNOTIFICATIONDATA().getTEXT());

            String site = notification.getNOTIFICATIONDATA().getSITE();
            if (site != null) {
                if (site.equalsIgnoreCase("A")) {
                    if (pbtdcOrder.getCustomerAccess() == null) {
                        pbtdcOrder.setCustomerAccess(Access.builder().build());
                    }
                    pbtdcOrder.getCustomerAccess().setAppointmentRequestReceivedTimestamp(LocalDateTime.now());
                } else if (site.equalsIgnoreCase("B")) {
                    if (pbtdcOrder.getWholesalerAccess() == null) {
                        pbtdcOrder.setWholesalerAccess(Access.builder().build());
                    }
                    pbtdcOrder.getWholesalerAccess().setAppointmentRequestReceivedTimestamp(LocalDateTime.now());
                }
            }

            pbtdcBusinessStatusService.updateBusinessStatusForAPTR(pbtdcOrder.getBusinessStatus());

            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean processStatusNotification(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        Boolean internalNotificationType = null;
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);

            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate QuoteOrder with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrderId in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);

            String orderStatus = notification.getNOTIFICATIONDATA().getORDERSTATUS();
            String talosOrderStatus = OrderStatus.getTalosOrderStatusForSiebelOrderStatus(orderStatus);

            if (talosOrderStatus != null) {
                pbtdcOrder.setOrderStatus(talosOrderStatus);
                internalNotificationType = OrderStatus.getInternalNotificationTypeForSiebelOrderStatus(orderStatus);
            } else {
                pbtdcOrder.setOrderStatus(orderStatus);
                internalNotificationType = true;
            }

            if (notification.getNOTIFICATIONDATA().getCIRCUITDETAILS() != null && notification.getNOTIFICATIONDATA().getCIRCUITDETAILS().size() > 0) {
                String site = notification.getNOTIFICATIONDATA().getCIRCUITDETAILS().get(0).getSITE();
                if (site.equalsIgnoreCase("A End") || site.equalsIgnoreCase("A")) {
                    if (pbtdcOrder.getCustomerAccess() == null) {
                        pbtdcOrder.setCustomerAccess(Access.builder().build());
                    }
                    pbtdcOrder.getCustomerAccess().setCircuitReference(notification.getNOTIFICATIONDATA().getCIRCUITDETAILS().get(0).getCIRCUITREFERENCE());
                } else if (site.equalsIgnoreCase("B End") || site.equalsIgnoreCase("B")) {
                    if (pbtdcOrder.getWholesalerAccess() == null) {
                        pbtdcOrder.setWholesalerAccess(Access.builder().build());
                    }
                    pbtdcOrder.getWholesalerAccess().setCircuitReference(notification.getNOTIFICATIONDATA().getCIRCUITDETAILS().get(0).getCIRCUITREFERENCE());
                }
            }

            pbtdcOrder.setStatusText(notification.getNOTIFICATIONDATA().getTEXT());

            pbtdcBusinessStatusService.updateBusinessStatusForStatusNotification(pbtdcOrder.getBusinessStatus(),
                    pbtdcOrder.getOrderStatus());

            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }

        return internalNotificationType;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processDelayStart(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);

            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate QuoteOrder with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrderId in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);

            //Update Orders with notification details
            CustomerDelay customerDelay = pbtdcOrder.getCustomerDelay() != null ? pbtdcOrder.getCustomerDelay() :
                    CustomerDelay.builder().build();
            customerDelay.setStartDate(notification.getNOTIFICATIONDATA().getCUSTOMERDELAY().getSTARTDATE());
            customerDelay.setReason(notification.getNOTIFICATIONDATA().getCUSTOMERDELAY().getREASON());
            pbtdcOrder.setCustomerDelay(customerDelay);

            pbtdcBusinessStatusService.updateBusinessStatusForDelayStart(pbtdcOrder.getBusinessStatus());

            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processDelayEnd(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);

            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate QuoteOrder with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrderId in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);

            //Update Orders with notification details
            CustomerDelay customerDelay = pbtdcOrder.getCustomerDelay() != null ? pbtdcOrder.getCustomerDelay() :
                    CustomerDelay.builder().build();
            customerDelay.setStartDate(notification.getNOTIFICATIONDATA().getCUSTOMERDELAY().getSTARTDATE());
            customerDelay.setEndDate(notification.getNOTIFICATIONDATA().getCUSTOMERDELAY().getENDDATE());
            customerDelay.setReason(notification.getNOTIFICATIONDATA().getCUSTOMERDELAY().getREASON());
            pbtdcOrder.setCustomerDelay(customerDelay);

            pbtdcBusinessStatusService.updateBusinessStatusForDelayEnd(pbtdcOrder.getBusinessStatus());

            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }

    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processConfirmationNotification(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);

            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate QuoteOrder with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrder in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);

            pbtdcOrder.setDueCompletionDate(notification.getNOTIFICATIONDATA().getDUECOMPLETIONDATE());
            pbtdcOrder.setReforecastDueDate(notification.getNOTIFICATIONDATA().getREFORECASTDUEDATE());
            pbtdcOrder.getBusinessStatus().setDeliveryOnTrack(
                    BooleanUtils.toBooleanObject(notification.getNOTIFICATIONDATA().getCONFIRMATIONRESULT(), "Y", "N"
                            , null));
            if (pbtdcOrder.getReforecastDueDate() != null) {
                pbtdcBusinessStatusService.updateBusinessStatusForConfirmation(pbtdcOrder.getBusinessStatus(),
                        pbtdcOrder.getReforecastDueDate());
            } else {
                pbtdcBusinessStatusService.updateBusinessStatusForConfirmation(pbtdcOrder.getBusinessStatus(),
                        pbtdcOrder.getDueCompletionDate());
            }
            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processOrderManagerDetailsNotification(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);

            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate QuoteOrder with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrder in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);
            if (pbtdcOrder.getOrderManager() == null) {
                pbtdcOrder.addContact(Contact.builder().role(ContactTypes.ORDER_MANAGER.getContactOwner()).orders(pbtdcOrder).build());
            }
            Contact orderManagerContact = pbtdcOrder.getOrderManager();
            orderManagerContact.setFirstName(notification.getNOTIFICATIONDATA().getORDERMANAGER().getFIRSTNAME());
            orderManagerContact.setLastName(notification.getNOTIFICATIONDATA().getORDERMANAGER().getLASTNAME());
            orderManagerContact.setEmail(notification.getNOTIFICATIONDATA().getORDERMANAGER().getEMAIL());

            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }
    }

    private void setSupplierOrderToPbtdcOrder(Pbtdc pbtdcOrder, Notification notification) {
        //Update SupplierOrder in ordersDTO.
        SupplierOrder supplierOrder = pbtdcOrder.getSupplierOrder() == null ? SupplierOrder.builder().build() :
                pbtdcOrder.getSupplierOrder();

        if (notification.getNOTIFICATIONDATA().getORDERID() != null) {
            supplierOrder.setSiebelNumber(notification.getNOTIFICATIONDATA().getORDERID());
        }
        if (notification.getNOTIFICATIONDATA().getORDERNUMBER() != null) {
            supplierOrder.setOrderNumber(notification.getNOTIFICATIONDATA().getORDERNUMBER());
        }
        pbtdcOrder.setSupplierOrder(supplierOrder);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map<String, Boolean> processNotesNotification(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        Boolean deliveryOnTrackChanged = false;
        Boolean notesTextEmpty = true;
        Map<String, Boolean> resultMap = new HashMap<>();
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);

            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate QuoteOrder with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrder in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);

            if (notification.getNOTIFICATIONDATA().getTEXT() != null) {
                pbtdcOrder.setNotes(notification.getNOTIFICATIONDATA().getTEXT());
                pbtdcOrder.setNotesReceivedDate(notification.getHEADER().getDATETIMESTAMP());
                notesTextEmpty = false;
            }

            Boolean deliveryOnTrack = BooleanUtils.toBooleanObject(
                    notification.getNOTIFICATIONDATA().getDELIVERYONTRACK(), "Y", "N", null);
            if (!Objects.equals(pbtdcOrder.getBusinessStatus().getDeliveryOnTrack(), deliveryOnTrack)) {
                pbtdcOrder.getBusinessStatus().setDeliveryOnTrack(deliveryOnTrack);
                deliveryOnTrackChanged = true;
            }

            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }

        resultMap.put(PbtdcOrderProcessConstants.VAR_NAME_DELIVERY_ON_TRACK_CHANGED, deliveryOnTrackChanged);
        resultMap.put(PbtdcOrderProcessConstants.VAR_NAME_NOTES_TEXT_EMPTY, notesTextEmpty);

        return resultMap;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processAcceptNotification(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);

            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate QuoteOrder with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrderId in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);

            pbtdcOrder.setDueCompletionDate(notification.getNOTIFICATIONDATA().getDUECOMPLETIONDATE());
            pbtdcOrder.setEstimatedCompletionDate(notification.getNOTIFICATIONDATA().getESTIMATEDCOMPLETIONDATE());

            if (notification.getHEADER().getDATETIMESTAMP() != null) {
                pbtdcOrder.setDateReceivedByDelivery(notification.getHEADER().getDATETIMESTAMP());
            }
            NotificationData.ORDERMANAGER ordermanager = notification.getNOTIFICATIONDATA().getORDERMANAGER();
            if (ordermanager != null) {
                pbtdcOrder.addContact(Contact.builder()
                        .role(ContactTypes.ORDER_MANAGER.getContactOwner())
                        .firstName(ordermanager.getFIRSTNAME())
                        .lastName(ordermanager.getLASTNAME())
                        .email(ordermanager.getEMAIL())
                        .orders(pbtdcOrder)
                        .build());
            }

            pbtdcBusinessStatusService.updateBusinessStatusForAccept(pbtdcOrder.getBusinessStatus(),
                    pbtdcOrder.getDueCompletionDate());

            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processCompleteNotification(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);

            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate QuoteOrder with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrderId in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);

            if (notification.getNOTIFICATIONDATA().getACCOUNTNUMBER() != null) {
                pbtdcOrder.setAccountNumber(notification.getNOTIFICATIONDATA().getACCOUNTNUMBER());
            }

            populateLogicalLinkForCompleteNotification(pbtdcOrder, notification);

            populateCustomerAccessForCompleteNotification(pbtdcOrder, notification);

            populateWholesalerAccessForCompleteNotification(pbtdcOrder, notification);

            pbtdcBusinessStatusService.updateBusinessStatusForComplete(pbtdcOrder.getBusinessStatus());

            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processCircuitId(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);

            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate QuoteOrder with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrderId in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);

            //Update Orders with notification details
            for (NotificationData.CIRCUITDETAILS circuitdetails :
                    notification.getNOTIFICATIONDATA().getCIRCUITDETAILS()) {
                if (SiteTypes.A.name().equalsIgnoreCase(circuitdetails.getSITE())) {
                    Access customerAccess = pbtdcOrder.getCustomerAccess() != null ? pbtdcOrder.getCustomerAccess() :
                            Access.builder().build();
                    customerAccess.setCircuitReference(circuitdetails.getID());
                    Site site = customerAccess.getSite() != null ? customerAccess.getSite() : Site.builder().build();
                    site.setName(SiteTypes.A.name());
                    customerAccess.setSite(site);
                    if (circuitdetails.getEXISTINGACCESSBANDWIDTH() != null) {
                        customerAccess.setBandWidth(String.valueOf(Float.valueOf(circuitdetails.getEXISTINGACCESSBANDWIDTH()) / 1000));
                    }
                    pbtdcOrder.setServiceClass(circuitdetails.getPRODUCTTYPE());
                    pbtdcOrder.setCustomerAccess(customerAccess);
                } else if (SiteTypes.B.name().equalsIgnoreCase(circuitdetails.getSITE())) {
                    Access wholeSalerAccess = pbtdcOrder.getWholesalerAccess() != null ?
                            pbtdcOrder.getWholesalerAccess() : Access.builder().build();
                    wholeSalerAccess.setCircuitReference(circuitdetails.getID());
                    Site site = wholeSalerAccess.getSite() != null ? wholeSalerAccess.getSite() :
                            Site.builder().build();
                    site.setName(SiteTypes.B.name());
                    wholeSalerAccess.setSite(site);
                    pbtdcOrder.setServiceClass(circuitdetails.getPRODUCTTYPE());
                    pbtdcOrder.setWholesalerAccess(wholeSalerAccess);
                } else if (CircuitTypes.BT_ETHERFLOW.name().equalsIgnoreCase(circuitdetails.getTYPE()) || CircuitTypes.BT_ETHERFLOW_IP.name().equalsIgnoreCase(circuitdetails.getTYPE())) {
                    LogicalLink logicalLink = pbtdcOrder.getLogicalLink() != null ? pbtdcOrder.getLogicalLink() :
                            LogicalLink.builder().build();
                    logicalLink.setCircuitReference(circuitdetails.getID());
                    pbtdcOrder.setServiceClass(circuitdetails.getPRODUCTTYPE());
                    pbtdcOrder.setLogicalLink(logicalLink);
                }
            }

            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }

    }


    private void populateLogicalLinkForCompleteNotification(Pbtdc pbtdcOrder, Notification notification) {
        NotificationData.CIRCUIT circuit = notification.getNOTIFICATIONDATA().getCIRCUIT().get(0);
        LogicalLink logicalLink = pbtdcOrder.getLogicalLink() != null ? pbtdcOrder.getLogicalLink() :
                LogicalLink.builder().serviceDetails(new ArrayList<>()).build();
        logicalLink.setCircuitReference(circuit.getLOGICALDETAILS().getCIRCUITREFERENCE());
        logicalLink.setAction(ActionFlag.valueOf(circuit.getLOGICALDETAILS().getACTIONFLAG()));
        logicalLink.setBandWidth(circuit.getLOGICALDETAILS().getBANDWIDTH());
        logicalLink.setServiceClass(circuit.getLOGICALDETAILS().getSERVICECLASS());
        logicalLink.setVlan(circuit.getLOGICALDETAILS().getVLAN());
        logicalLink.setPbtdc(pbtdcOrder);
        pbtdcOrder.setLogicalLink(logicalLink);

        if (circuit.getLOGICALDETAILS().getADDITIONALSERVICES() != null && circuit.getLOGICALDETAILS().getADDITIONALSERVICES().getSERVICEDETAILS().size() > 0) {
            for (ServiceDetails serviceDetails :
                    circuit.getLOGICALDETAILS().getADDITIONALSERVICES().getSERVICEDETAILS()) {
                com.btireland.talos.ethernet.engine.domain.ServiceDetails llServiceDetails =
                        new com.btireland.talos.ethernet.engine.domain.ServiceDetails();
                llServiceDetails.setAction(ActionFlag.valueOf(serviceDetails.getACTIONFLAG()));
                llServiceDetails.setServiceName(serviceDetails.getSERVICENAME());
                llServiceDetails.setServiceValue(serviceDetails.getSERVICEVALUE());
                llServiceDetails.setLogicalLink(logicalLink);
                logicalLink.getServiceDetails().add(llServiceDetails);
            }
        }
    }

    private void populateCustomerAccessForCompleteNotification(Pbtdc pbtdcOrder, Notification notification) {
        EthernetServiceEndpoint aEnd = notification.getNOTIFICATIONDATA().getAEND();
        Access customerAccess = pbtdcOrder.getCustomerAccess() != null ? pbtdcOrder.getCustomerAccess() :
                Access.builder().build();
        customerAccess.setCircuitReference(aEnd.getCIRCUITDETAILS().get(0).getCIRCUITREFERENCE());
        customerAccess.setServiceClass(aEnd.getCIRCUITDETAILS().get(0).getSERVICECLASS());
        String actionFlag =
                aEnd.getCIRCUITDETAILS().get(0).getACTIONFLAG().equalsIgnoreCase(ActionFlag.L.toString()) ?
                        ActionFlag.CH.toString() : aEnd.getCIRCUITDETAILS().get(0).getACTIONFLAG();
        customerAccess.setAction(ActionFlag.valueOf(actionFlag));
        Site customerSite = customerAccess.getSite() != null ? customerAccess.getSite() :
                Site.builder().location(Location.builder().address(Address.builder().build()).build()).build();
        customerSite.getLocation().getAddress().setLocationLine1(aEnd.getSITE().getLOCATION().getLINE1());
        customerSite.getLocation().getAddress().setLocationLine2(aEnd.getSITE().getLOCATION().getLINE2());
        customerSite.getLocation().getAddress().setLocationLine3(aEnd.getSITE().getLOCATION().getLINE3());
        customerSite.getLocation().getAddress().setCity(aEnd.getSITE().getLOCATION().getCITY());
        customerSite.getLocation().getAddress().setCounty(aEnd.getSITE().getLOCATION().getCOUNTY());
        customerSite.getLocation().setId(aEnd.getSITE().getLOCATION().getEIRCODE());
        customerSite.getLocation().setType(LocationType.EIRCODE.name());
        customerAccess.setSite(customerSite);
        TerminatingEquipment customerAccessTerminatingEquipment = customerAccess.getTerminatingEquipment() != null ?
                customerAccess.getTerminatingEquipment() : TerminatingEquipment.builder().build();
        if (aEnd.getSITE().getPORTSETTINGS() != null) {
            customerAccessTerminatingEquipment.setPort(aEnd.getSITE().getPORTSETTINGS().getADVAPORTNUMBER());
            customerAccessTerminatingEquipment.setPortSetting(aEnd.getSITE().getPORTSETTINGS().getPORTCONFIGURATION());
            customerAccessTerminatingEquipment.setPresentation(aEnd.getSITE().getPORTSETTINGS().getPRESENTATION());
            customerAccessTerminatingEquipment.setPortSpeed(aEnd.getSITE().getPORTSETTINGS().getPORTSPEED());
        }
        customerAccess.setTerminatingEquipment(customerAccessTerminatingEquipment);
        populateSiteContacts(aEnd.getSITE().getSITECONTACT(), customerAccess);
        pbtdcOrder.setCustomerAccess(customerAccess);

    }

    private void populateWholesalerAccessForCompleteNotification(Pbtdc pbtdcOrder, Notification notification) {
        EthernetServiceEndpoint bEnd = notification.getNOTIFICATIONDATA().getBEND();
        if (bEnd != null) {
            Access wholeSalerAccess = pbtdcOrder.getWholesalerAccess() != null ? pbtdcOrder.getWholesalerAccess() :
                    Access.builder().build();
            wholeSalerAccess.setCircuitReference(bEnd.getCIRCUITDETAILS().get(0).getCIRCUITREFERENCE());
            wholeSalerAccess.setServiceClass(bEnd.getCIRCUITDETAILS().get(0).getSERVICECLASS());
            String actionFlag =
                    bEnd.getCIRCUITDETAILS().get(0).getACTIONFLAG().equalsIgnoreCase(ActionFlag.L.toString()) ?
                            ActionFlag.CH.toString() : bEnd.getCIRCUITDETAILS().get(0).getACTIONFLAG();
            wholeSalerAccess.setAction(ActionFlag.valueOf(actionFlag));
            Site wholesalerSite = wholeSalerAccess.getSite() != null ? wholeSalerAccess.getSite() :
                    Site.builder().build();
            Location location = wholesalerSite.getLocation() != null ? wholesalerSite.getLocation() :
                    Location.builder().build();
            location.setAddress(Address.builder().build());
            location.getAddress().setLocationLine1(bEnd.getSITE().getLOCATION().getLINE1());
            location.getAddress().setLocationLine2(bEnd.getSITE().getLOCATION().getLINE2());
            location.getAddress().setLocationLine3(bEnd.getSITE().getLOCATION().getLINE3());
            location.getAddress().setCity(bEnd.getSITE().getLOCATION().getCITY());
            location.getAddress().setCounty(bEnd.getSITE().getLOCATION().getCOUNTY());
            //As on now we are ignoring eircode update to the location id field for BEnd.
            wholesalerSite.setLocation(location);
            wholeSalerAccess.setSite(wholesalerSite);
            TerminatingEquipment wholesalerAccessTerminatingEquipment =
                    wholeSalerAccess.getTerminatingEquipment() != null ? wholeSalerAccess.getTerminatingEquipment() :
                            TerminatingEquipment.builder().build();
            if (bEnd.getSITE().getPORTSETTINGS() != null) {
                wholesalerAccessTerminatingEquipment.setPort(bEnd.getSITE().getPORTSETTINGS().getADVAPORTNUMBER());
                wholesalerAccessTerminatingEquipment.setPortSetting(bEnd.getSITE().getPORTSETTINGS().getPORTCONFIGURATION());
                wholesalerAccessTerminatingEquipment.setPresentation(bEnd.getSITE().getPORTSETTINGS().getPRESENTATION());
                wholesalerAccessTerminatingEquipment.setPortSpeed(bEnd.getSITE().getPORTSETTINGS().getPORTSPEED());
            }
            wholeSalerAccess.setTerminatingEquipment(wholesalerAccessTerminatingEquipment);
            populateSiteContacts(bEnd.getSITE().getSITECONTACT(), wholeSalerAccess);
            pbtdcOrder.setWholesalerAccess(wholeSalerAccess);
        }

    }

    private void populateSiteContacts(List<SITECONTACT> updatedSiteContactList, Access access) {
        if (updatedSiteContactList != null && !updatedSiteContactList.isEmpty()) {
            AccessInstall accessInstall = access.getAccessInstall() != null ? access.getAccessInstall() :
                    AccessInstall.builder().access(access).build();
            access.setAccessInstall(accessInstall);
            Contact mainContact = accessInstall.getMainSiteContact() != null ? accessInstall.getMainSiteContact() :
                    Contact.builder().build();
            mainContact.setFirstName(updatedSiteContactList.get(0).getFIRSTNAME());
            mainContact.setLastName(updatedSiteContactList.get(0).getLASTNAME());
            mainContact.setNumber(updatedSiteContactList.get(0).getCONTACTNUMBER());
            mainContact.setEmail(updatedSiteContactList.get(0).getEMAIL());
            mainContact.setAccessInstall(accessInstall);
            mainContact.setRole(ContactTypes.MAIN_CONTACT.getContactOwner());
            if (accessInstall.getMainSiteContact() == null)
                accessInstall.addContact(mainContact);

            if (updatedSiteContactList.size() > 1) {
                Contact secondaryContact = accessInstall.getSecondarySiteContact() != null ?
                        accessInstall.getSecondarySiteContact() : Contact.builder().build();
                secondaryContact.setFirstName(updatedSiteContactList.get(1).getFIRSTNAME());
                secondaryContact.setLastName(updatedSiteContactList.get(1).getLASTNAME());
                secondaryContact.setNumber(updatedSiteContactList.get(1).getCONTACTNUMBER());
                secondaryContact.setEmail(updatedSiteContactList.get(1).getEMAIL());
                secondaryContact.setAccessInstall(accessInstall);
                secondaryContact.setRole(ContactTypes.SECONDARY_CONTACT.getContactOwner());
                if (accessInstall.getSecondarySiteContact() == null)
                    accessInstall.addContact(secondaryContact);
            }
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processGlanIdUpdationNotification(Long wagOrderId, Long notificationId) throws PBTDCOrderClientException {
        Notification notification = null;
        ParkedNotifications parkedNotification = null;
        try {
            Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);

            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);

            //Update Status as InProgress
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.INPROGRESS.getValue());

            //populate OrdersDTO with Supplier Info and persist
            notification = extractSupplierNotification(parkedNotification);

            //Update SupplierOrder in ordersDTO.
            setSupplierOrderToPbtdcOrder(pbtdcOrder, notification);

            for (NotificationData.GLANIDDETAILS glanIdDetails : notification.getNOTIFICATIONDATA().getGLANIDDETAILS()) {
                if (CircuitTypes.BT_ETHERFLOW.name().equalsIgnoreCase(glanIdDetails.getTYPE()) || CircuitTypes.BT_ETHERFLOW_IP.name().equalsIgnoreCase(glanIdDetails.getTYPE())) {
                    if (pbtdcOrder.getLogicalLink() == null) {
                        pbtdcOrder.setLogicalLink(LogicalLink.builder().build());
                    }
                    pbtdcOrder.getLogicalLink().setGlanId(glanIdDetails.getGLANID());
                } else if (SiteTypes.A.name().equalsIgnoreCase(glanIdDetails.getSITE())) {
                    if (pbtdcOrder.getCustomerAccess() == null) {
                        pbtdcOrder.setCustomerAccess(Access.builder().build());
                    }
                    pbtdcOrder.getCustomerAccess().setGlanId(glanIdDetails.getGLANID());
                } else if (SiteTypes.B.name().equalsIgnoreCase(glanIdDetails.getSITE())) {
                    if (pbtdcOrder.getWholesalerAccess() == null) {
                        pbtdcOrder.setWholesalerAccess(Access.builder().build());
                    }
                    pbtdcOrder.getWholesalerAccess().setGlanId(glanIdDetails.getGLANID());
                }
            }

            updatePBTDCOrderGlanIdsToWag(pbtdcOrder.getWagOrderId(), notification);

            //Update Status as Processed
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.PROCESSED.getValue());

        } catch (Exception | OrderManagerServiceBadRequestException | OrderManagerServiceNotFoundException e) {
            parkedNotification = parkedNotificationsPersistenceService.findById(notificationId);
            log.error("Exception occurred while processing parked notification for order {}", wagOrderId, e);
            parkedNotification.setProcessedStatus(NotificationProcessedStatus.UNPROCESSED.getValue());
            throw new PBTDCOrderClientException(e.getMessage());
        }
    }

    private void updatePBTDCOrderGlanIdsToWag(Long orderId, Notification notification) throws OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException {
        List<PBTDCGlanIdDTO> pbtdcGlanIdDTOList = new ArrayList<>();
        for (NotificationData.GLANIDDETAILS glanIdDetails : notification.getNOTIFICATIONDATA().getGLANIDDETAILS()) {
            if (CircuitTypes.BT_ETHERFLOW.name().equalsIgnoreCase(glanIdDetails.getTYPE()) || CircuitTypes.BT_ETHERFLOW_IP.name().equalsIgnoreCase(glanIdDetails.getTYPE())) {
                PBTDCGlanIdDTO pbtdcGlanIdDTO = PBTDCGlanIdDTO.builder()
                        .glanId(glanIdDetails.getGLANID())
                        .type(PBTDCGlanIdDTO.GLANIDType.BT_ETHERFLOW)
                        .build();
                pbtdcGlanIdDTOList.add(pbtdcGlanIdDTO);
            } else if (SiteTypes.A.name().equalsIgnoreCase(glanIdDetails.getSITE())) {

                PBTDCGlanIdDTO pbtdcGlanIdDTOFirSiteA = PBTDCGlanIdDTO.builder()
                        .glanId(glanIdDetails.getGLANID())
                        .type(PBTDCGlanIdDTO.GLANIDType.BT_ETHERWAY)
                        .site(PBTDCGlanIdDTO.GLANIDSite.A)
                        .build();
                pbtdcGlanIdDTOList.add(pbtdcGlanIdDTOFirSiteA);
            } else if (SiteTypes.B.name().equalsIgnoreCase(glanIdDetails.getSITE())) {
                PBTDCGlanIdDTO pbtdcGlanIdDTOFirSiteB = PBTDCGlanIdDTO.builder()
                        .glanId(glanIdDetails.getGLANID())
                        .type(PBTDCGlanIdDTO.GLANIDType.BT_ETHERWAY)
                        .site(PBTDCGlanIdDTO.GLANIDSite.B)
                        .build();
                pbtdcGlanIdDTOList.add(pbtdcGlanIdDTOFirSiteB);
            }
        }
        orderManagerService.updatePBTDCOrderGlanId(orderId, pbtdcGlanIdDTOList);
    }

    private Notification extractSupplierNotification(ParkedNotifications parkedNotification) throws com.fasterxml.jackson.core.JsonProcessingException, JAXBException {
        NotificationDTO notificationObj =
                objectMapper.readValue(new String(parkedNotification.getSupplierNotification()), NotificationDTO.class);
        Notification notification = XmlUtils.unmarshalNotification(notificationObj.getContent());
        return notification;
    }
}

