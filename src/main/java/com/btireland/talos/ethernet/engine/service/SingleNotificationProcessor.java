package com.btireland.talos.ethernet.engine.service;

import lombok.extern.slf4j.Slf4j;
import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.domain.ParkedNotifications;
import com.btireland.talos.ethernet.engine.dto.InterventionDetailsDTO;
import com.btireland.talos.ethernet.engine.facade.PbtdcMapper;
import com.btireland.talos.ethernet.engine.util.Color;
import com.btireland.talos.ethernet.engine.util.NotificationProcessedStatus;
import com.btireland.talos.ethernet.engine.util.NotificationTypes;
import com.btireland.talos.ethernet.engine.util.Status;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SingleNotificationProcessor {

    public static final String MSG_PREFIX = "Msg_";

    private ParkedNotificationsPersistenceService parkedNotificationsPersistenceService;
    private RuntimeService runtimeService;
    private InterventionService interventionService;
    private PbtdcMapper pbtdcMapper;

    public SingleNotificationProcessor(ParkedNotificationsPersistenceService parkedNotificationsPersistenceService, RuntimeService runtimeService,
                                       InterventionService interventionService, PbtdcMapper pbtdcMapper) {
        this.parkedNotificationsPersistenceService = parkedNotificationsPersistenceService;
        this.runtimeService = runtimeService;
        this.interventionService = interventionService;
        this.pbtdcMapper = pbtdcMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processOneParkedNotificationPerOrder(Orders order){
        ParkedNotifications notification =
                parkedNotificationsPersistenceService.findFirstRecordByWagOrderIdAndNotificationType(order.getWagOrderId(), "A", NotificationProcessedStatus.UNPROCESSED.getValue());


        if(notification != null){
            if(order.getInterventionFlag()) {
                //Adding additional check for already processed Accept Notification as it was causing an infinite loop for closing and opening an intervention.
                ParkedNotifications processedAcceptNotification =
                        parkedNotificationsPersistenceService.findFirstRecordByWagOrderIdAndNotificationType(order.getWagOrderId(), "A", NotificationProcessedStatus.PROCESSED.getValue());
                if(processedAcceptNotification == null){
                    order = closeIntervention(order);
              }
            }
        }else {
            notification = parkedNotificationsPersistenceService.findFirstRecordByWagOrderId(order.getWagOrderId(),
                    NotificationProcessedStatus.UNPROCESSED.getValue());
        }
        log.info("ParkNotification captured for OrderId " + order.getOrderNumber()
                + ", wagOrderId " + order.getWagOrderId()
                + ", notificationType " + order.getLastNotificationType()
                + ", status " + order.getOrderStatus()
                + " and the order is modified on " + order.getChangedAt());

        if (notification != null && (!order.getInterventionFlag()) ) {
            Map<String, Object> variables = new HashMap<>();
            log.info("ParkNotification processed for the NotificationId " + notification.getId() + " with the " +
                    "NotificationType " + notification.getNotificationType() + " and the wagOrderId " + order.getWagOrderId().toString());
            variables.put(PbtdcOrderProcessConstants.PARKED_NOTIFICATION_ID, notification.getId());
            //Invoke relevant signal/message events
            runtimeService
                    .createMessageCorrelation(MSG_PREFIX + NotificationTypes.getMessageNameByNotificationCode(notification.getNotificationType()))
                    .processInstanceBusinessKey(order.getWagOrderId().toString())
                    .setVariables(variables)
                    .correlate();

        }
    }

    private Orders closeIntervention(Orders order) {
        InterventionDetails interventionDetails = InterventionDetails.builder()
                .closingAgent(InterventionDetailsDTO.DEFAULT_AGENT)
                .color(Color.CLEAR)
                .status(Status.CLOSED)
                .closingNotes(InterventionDetailsDTO.DEFAULT_CLOSING_NOTES)
                .workflow(PbtdcOrderProcessConstants.ACT_ID_SUPPLIER_ACCEPT_PROCESS)
                .build();
                log.info("Intervention is closed for wagOrderId " + order.getWagOrderId()
                        +  "OrderId " + order.getOrderNumber()
                        + " NotificationType " + order.getLastNotificationType());

        order = interventionService.createOrUpdateIntervention(order, interventionDetails);
        return order;
    }
}
