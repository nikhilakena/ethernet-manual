package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.client.asset.notcom.NotificationClient;
import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.dto.ContactDTO;
import com.btireland.talos.ethernet.engine.dto.CustomerDelayDTO;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.dto.PbtdcDTO;
import com.btireland.talos.ethernet.engine.exception.NotificationClientException;
import com.btireland.talos.ethernet.engine.facade.PbtdcMapper;
import com.btireland.talos.ethernet.engine.util.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotComNotificationsService {

    private NotificationClient notificationClient;

    private ObjectMapper objectMapper;

    private PbtdcMapper pbtdcMapper;

    public NotComNotificationsService(NotificationClient notificationClient, ObjectMapper objectMapper, PbtdcMapper pbtdcMapper) {
        this.notificationClient = notificationClient;
        this.objectMapper = objectMapper;
        this.pbtdcMapper = pbtdcMapper;
    }

    /**
     *
     * @param notComRequest
     * @return
     * @throws com.btireland.talos.core.common.rest.exception.ResourceNotFoundException if the asset does not exist
     */
    public Notifications createNotification(Notifications notComRequest) {

            return notificationClient.createNotification(notComRequest);

    }
    public Notifications createNotification(String btNotificationType, Pbtdc pbtdcOrder) throws NotificationClientException, JsonProcessingException {
        Notifications notComRequest = generateNotComRequestForRSPNotification(pbtdcOrder.getWagOrderId(), btNotificationType, pbtdcOrder);
        log.info("NotCom Request being sent for RSP Notification : "+objectMapper.writeValueAsString(notComRequest));
        try {
            return notificationClient.createNotification(notComRequest);
        }catch(FeignException ex){
            throw new NotificationClientException("Exception received while creating RSP Notification of type: " +notComRequest.getType()  +", for order reference "+notComRequest.getReference(), ex);
        }
    }

    private Notifications generateNotComRequestForRSPNotification(Long orderId, String btNotificationType, Pbtdc pbtdcOrder) throws JsonProcessingException {
        OrdersDTO orderDto = generateOrderForNotcom(pbtdcOrder);
        return Notifications.builder().source("TALOS").type(btNotificationType).reference(generateTrackingRef(orderId, orderDto.getServiceType())).content(objectMapper.writeValueAsBytes(orderDto)).build();
    }

    private OrdersDTO generateOrderForNotcom(Pbtdc pbtdcOrder) {

       OrdersDTO orderDto = OrdersDTO.builder()
                .orderId(pbtdcOrder.getWagOrderId())
                .oao(pbtdcOrder.getOao())
                .dataContract(pbtdcOrder.getDataContract())
                .originatorCode(pbtdcOrder.getOriginatorCode())
                .resellerTransactionId(pbtdcOrder.getResellerTransactionId())
                .resellerOrderRequestDateTime(DateUtils.btDateTimeToString(pbtdcOrder.getResellerOrderRequestDateTime()))
                .orderNumber(pbtdcOrder.getOrderNumber())
                .accountNumber(pbtdcOrder.getAccountNumber())
                .operatorName(pbtdcOrder.getOperatorName())
                .operatorCode(pbtdcOrder.getOperatorCode())
                .createdAt(DateUtils.isoDateTimeToString(pbtdcOrder.getCreatedAt()))
                .serviceType(pbtdcOrder.getServiceType())
                .lastNotificationType(pbtdcOrder.getLastNotificationType())
                .orderStatus(pbtdcOrder.getOrderStatus())
                .statusNotes(pbtdcOrder.getNotes())
                .dueCompletionDate(DateUtils.btDateToString(pbtdcOrder.getDueCompletionDate()))
                .estimatedCompletionDate(DateUtils.btDateToString(pbtdcOrder.getEstimatedCompletionDate()))
                .reforecastDueDate(DateUtils.btDateToString(pbtdcOrder.getReforecastDueDate()))
                .confirmationResult(BooleanUtils.toString(pbtdcOrder.getBusinessStatus().getDeliveryOnTrack(), "Y", "N", "Y"))
                .serviceClass(pbtdcOrder.getServiceClass())
                .build();

        orderDto.setRejectionDetails(pbtdcMapper.toRejectionDetailsDTO(pbtdcOrder.getRejectionDetails()));

        orderDto.setPbtdc(pbtdcMapper.toPbtdcDTO(pbtdcOrder));

        //Cannot set this in mapper as mapper is used at other places like while sending email where handoff is required
        if(pbtdcOrder.getLastNotificationType() != null && pbtdcOrder.getLastNotificationType().equalsIgnoreCase("C")
                && orderDto.getPbtdc().getWholesalerAccess().getSite().getLocation().getType() != null
                && orderDto.getPbtdc().getWholesalerAccess().getSite().getLocation().getType().equalsIgnoreCase("HANDOVER")) {
            orderDto.getPbtdc().getWholesalerAccess().getSite().getLocation().setId(null);
        }

        if(pbtdcOrder.getLastNotificationType() != null && !pbtdcOrder.getLastNotificationType().equalsIgnoreCase("N")) {
            orderDto.setStatusNotes(pbtdcOrder.getStatusText());
        }

        if(pbtdcOrder.getOrderManager()!=null)  {
            if(orderDto.getPbtdc() == null) {
                orderDto.setPbtdc(PbtdcDTO.builder().build());
            }
            orderDto.getPbtdc().setOrderManagerContact(ContactDTO.builder()
                    .firstName(pbtdcOrder.getOrderManager().getFirstName())
                    .lastName(pbtdcOrder.getOrderManager().getLastName())
                    .email(pbtdcOrder.getOrderManager().getEmail())
                    .build());
        }

        if(pbtdcOrder.getCustomerDelay()!=null){
            orderDto.setCustomerDelay(CustomerDelayDTO.builder()
                    .startDate(DateUtils.btDateTimeToString(pbtdcOrder.getCustomerDelay().getStartDate()))
                    .endDate(DateUtils.btDateTimeToString(pbtdcOrder.getCustomerDelay().getEndDate()))
                    .reason(pbtdcOrder.getCustomerDelay().getReason())
                    .build()
            );
        }

        return orderDto;
    }

    private String generateTrackingRef(Long orderId, String serviceType) {
        return "BT-" + serviceType + "-" + orderId.toString();
    }

}
