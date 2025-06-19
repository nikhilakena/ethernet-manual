package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.client.asset.notcom.OrdersDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCOrderDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.RejectDTO;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderJsonResponse;
import com.btireland.talos.ethernet.engine.dto.PBTDCOtherDetailsDTO;
import com.btireland.talos.ethernet.engine.exception.AuthenticationException;
import com.btireland.talos.ethernet.engine.exception.BadRequestException;
import com.btireland.talos.ethernet.engine.exception.PersistanceException;
import com.btireland.talos.ethernet.engine.exception.RequestValidationException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceNotFoundException;
import com.btireland.talos.ethernet.engine.facade.PBTDCOrderMapper;
import com.btireland.talos.ethernet.engine.soap.context.RequestContext;
import com.btireland.talos.ethernet.engine.soap.orders.PBTDCOrderresponse;
import com.btireland.talos.ethernet.engine.util.RejectCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
public class PBTDCOrderService {
    private ObjectMapper objectMapper;
    private PBTDCOrderMapper pbtdcOrderMapper;
    private OrderManagerService orderManagerService;
    private NotComNotificationsService notComNotificationsService;
    private PBTDCOrderValidationService pbtdcOrderValidationService;
    private RequestContext requestContext;

    public PBTDCOrderService(ObjectMapper objectMapper,
                             PBTDCOrderMapper pbtdcOrderMapper,
                             OrderManagerService orderManagerService,
                             NotComNotificationsService notComNotificationsService,
                             PBTDCOrderValidationService pbtdcOrderValidationService,
                             RequestContext requestContext) {
        this.objectMapper = objectMapper;
        this.pbtdcOrderMapper = pbtdcOrderMapper;
        this.orderManagerService = orderManagerService;
        this.notComNotificationsService = notComNotificationsService;
        this.pbtdcOrderValidationService = pbtdcOrderValidationService;
        this.requestContext = requestContext;
    }

    public PBTDCOrderresponse createPBTDCOrder(PBTDCOrderDTO pbtdcOrderData, String version) throws BadRequestException, RequestValidationException, AuthenticationException, PersistanceException, OrderManagerServiceNotFoundException, OrderManagerServiceBadRequestException {

        RejectDTO pbtdcRejectDTO = pbtdcOrderValidationService.validateOrder(pbtdcOrderData);
        if (pbtdcRejectDTO != null && pbtdcRejectDTO.getRejectCode() != 0) {
            return pbtdcOrderMapper.generatePBTDCRejectResponse(pbtdcOrderData, pbtdcRejectDTO);
        }
        //Setting the originalXMLRequest
        pbtdcOrderData.setOriginalXMLRequest(requestContext.getOriginalXMLRequest().getBytes());

        /***Persist in Order DB by calling Order Manager***/
        try {

            pbtdcOrderData = orderManagerService.createPBTDCOrder(pbtdcOrderData);

        } catch (OrderManagerServiceBadRequestException e) {
            log.error("Error while creating PBTDC Order response from persistence ", e);
            if (isDuplicateOrderNumber(e))
                return handleRejection(pbtdcOrderData);
            else
                throw e;
        } catch (Exception e) {
            log.error("Error while creating PBTDC Order response from persistence ", e);
            throw new PersistanceException("WS10: Unexpected Failure.");
        }


        OrdersDTO ordersDTO = generatePBTDCOrdersForNotCom(pbtdcOrderData);

        /***Call NotCom to create Notification entry in WAG***/
        Notifications notifications =
                notComNotificationsService.createNotification(generateNotComRequestForWSA(pbtdcOrderData, ordersDTO));


        /***Return WSA with updated Notification Id***/
        return (pbtdcOrderMapper.generatePBTDCOrderResponse(pbtdcOrderData, notifications, version));
    }

    public PBTDCOrderJsonResponse createPBTDCOrder2(Pbtdc pbtdcOrder, PBTDCOtherDetailsDTO otherDetailsDTO) throws BadRequestException, RequestValidationException, AuthenticationException, PersistanceException, OrderManagerServiceNotFoundException, OrderManagerServiceBadRequestException {

        Integer version = pbtdcOrder.getVersion();
        PBTDCOrderDTO pbtdcOrderData = PBTDCOrderDTO.builder()
                .orders(com.btireland.talos.ethernet.engine.client.asset.ordermanager.OrdersDTO.builder()
                        .createdAt(String.valueOf(pbtdcOrder.getCreatedAt()))
                        .operatorName(pbtdcOrder.getOperatorName())
                        .operatorCode(pbtdcOrder.getOperatorCode())
                        .orderNumber(pbtdcOrder.getOrderNumber())
                        .obo(pbtdcOrder.getObo())
                        .oao(pbtdcOrder.getOao())
                        .dataContractName(pbtdcOrder.getDataContract())
                        .originatorCode(pbtdcOrder.getOriginatorCode())
                        .transactionId(pbtdcOrder.getResellerTransactionId())
                        .orderRequestDateTime(String.valueOf(pbtdcOrder.getResellerOrderRequestDateTime()))
                        .orderServiceType(pbtdcOrder.getServiceType())
                        .orderStatus(pbtdcOrder.getOrderStatus() != null && !pbtdcOrder.getOrderStatus().isBlank() ? pbtdcOrder.getOrderStatus() : "Talos Pending")
                        .workflowStatus(pbtdcOrder.getWorkflowStatus() != null && !pbtdcOrder.getWorkflowStatus().isBlank() ? pbtdcOrder.getWorkflowStatus() : "Talos In System")
                        .build())
                .pbtdcs(PBTDCDTO.builder()
                        .applicationDate(String.valueOf(pbtdcOrder.getApplicationDate()))
                        .organisationName(pbtdcOrder.getOrganisationName())
                        .refQuoteItemId(Math.toIntExact(pbtdcOrder.getRefQuoteItemId()))
                        .connectionType(pbtdcOrder.getConnectionType())
                        .firstSiteContactFirstName(otherDetailsDTO.getFirstSiteContactFirstName())
                        .firstSiteContactLastName(otherDetailsDTO.getFirstSiteContactLastName())
                        .firstSiteContactContactNumber(otherDetailsDTO.getFirstSiteContactContactNumber())
                        .firstSiteContactEmail(otherDetailsDTO.getFirstSiteContactEmail())
                        .methodInsuranceCert(otherDetailsDTO.getMethodInsuranceCert())
                        .siteInduction(otherDetailsDTO.getSiteInduction())
                        .commsRoomDetails(otherDetailsDTO.getCommsRoomDetails())
                        .readyForDelivery(otherDetailsDTO.getReadyForDelivery())
                        .powerSocketType(otherDetailsDTO.getPowerSocketType())
                        .cableManager(otherDetailsDTO.getCableManager())
                        .presentation(otherDetailsDTO.getPresentation())
                        .aEndActionFlag(otherDetailsDTO.getAendActionFlag())
                        .notes(pbtdcOrder.getNotes())
                        .build())
                .build();
        RejectDTO pbtdcRejectDTO = pbtdcOrderValidationService.validateOrder(pbtdcOrderData);
        if (pbtdcRejectDTO != null && pbtdcRejectDTO.getRejectCode() != 0) {
            return PBTDCOrderJsonResponse.builder().pbtdcOrderData(pbtdcOrderData).rejectDTO(pbtdcRejectDTO).build();
        }
        //Setting the originalXMLRequest
        // pbtdcOrderData.setOriginalXMLRequest(requestContext.getOriginalXMLRequest().getBytes());

        /***Persist in Order DB by calling Order Manager***/
        try {

            pbtdcOrderData = orderManagerService.createPBTDCOrder(pbtdcOrderData);

        } catch (OrderManagerServiceBadRequestException e) {
            log.error("Error while creating PBTDC Order response from persistence ", e);
            if (isDuplicateOrderNumber(e)) {
                pbtdcRejectDTO =
                        RejectDTO.builder().rejectCode(RejectCode.CODE_7.getCode()).rejectReason(RejectCode.CODE_7.getRejectReason()).build();
                return PBTDCOrderJsonResponse.builder().pbtdcOrderData(pbtdcOrderData).rejectDTO(pbtdcRejectDTO).build();
            }
            else
                throw e;
        } catch (Exception e) {
            log.error("Error while creating PBTDC Order response from persistence ", e);
            throw new PersistanceException("WS10: Unexpected Failure.");
        }


        OrdersDTO ordersDTO = generatePBTDCOrdersForNotCom(pbtdcOrderData);

        /***Call NotCom to create Notification entry in WAG***/
        Notifications notifications =
                notComNotificationsService.createNotification(generateNotComRequestForWSA(pbtdcOrderData, ordersDTO));


        /***Return WSA with updated Notification Id***/
        return PBTDCOrderJsonResponse.builder().pbtdcOrderData(pbtdcOrderData).notifications(notifications).version(String.valueOf(version)).build();
    }

    private boolean isDuplicateOrderNumber(OrderManagerServiceBadRequestException e) {
        return e.getMessage().contains("Order with the supplied Order Number already exists in the system for the RSP" +
                " :");
    }

    private PBTDCOrderresponse handleRejection(PBTDCOrderDTO pbtdcOrderData) {
        RejectDTO pbtdcRejectDTO =
                RejectDTO.builder().rejectCode(RejectCode.CODE_7.getCode()).rejectReason(RejectCode.CODE_7.getRejectReason()).build();
        return pbtdcOrderMapper.generatePBTDCRejectResponse(pbtdcOrderData, pbtdcRejectDTO);

    }


    public Notifications generateNotComRequestForWSA(PBTDCOrderDTO pbtdcOrderData,
                                                     OrdersDTO ordersDTO) throws BadRequestException {

        try {
            Notifications wsaNotificationRequest = Notifications.builder()
                    .source("TALOS")
                    .createdAt(LocalDateTime.now())
                    .changedAt(LocalDateTime.now())
                    .type("WSA")
                    .reference(pbtdcOrderData.getOrders().getOrderNumber())
                    .content(objectMapper.writeValueAsBytes(ordersDTO))
                    .build();

            log.info("WSA Notification Request to be sent to NotCom " + objectMapper.writeValueAsString(wsaNotificationRequest));
            return wsaNotificationRequest;
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Unable to convert notification details into encoded bytes.");
        }

    }


    public OrdersDTO generatePBTDCOrdersForNotCom(PBTDCOrderDTO pbtdcOrderData) {

        return OrdersDTO.builder()
                .orderId(pbtdcOrderData.getOrders().getOrderId())
                .oao(pbtdcOrderData.getOrders().getOao())
                .dataContract(pbtdcOrderData.getOrders().getDataContractName())
                .originatorCode(pbtdcOrderData.getOrders().getOriginatorCode())
                .resellerTransactionId(pbtdcOrderData.getOrders().getTransactionId())
                .resellerOrderRequestDateTime(pbtdcOrderData.getOrders().getOrderRequestDateTime())
                .orderNumber(pbtdcOrderData.getOrders().getOrderNumber())
                .operatorName(pbtdcOrderData.getOrders().getOperatorName())
                .operatorCode(pbtdcOrderData.getOrders().getOperatorCode())
                .createdAt(pbtdcOrderData.getOrders().getCreatedAt())
                .serviceType(pbtdcOrderData.getOrders().getOrderServiceType())
                .build();

    }


}
