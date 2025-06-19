package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.util.Color;
import com.btireland.talos.ethernet.engine.util.Status;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@Transactional
public class ParkedNotificationProcessor {

    private SingleNotificationProcessor singleNotificationProcessor;

    private ParkedNotificationsPersistenceService parkedNotificationsPersistenceService;

    private InterventionService interventionService;

    private OrdersPersistenceService ordersPersistenceService;

    public ParkedNotificationProcessor(SingleNotificationProcessor singleNotificationProcessor, ParkedNotificationsPersistenceService parkedNotificationsPersistenceService,
                                       InterventionService interventionService, OrdersPersistenceService ordersPersistenceService) {
        this.singleNotificationProcessor = singleNotificationProcessor;
        this.parkedNotificationsPersistenceService = parkedNotificationsPersistenceService;
        this.interventionService = interventionService;
        this.ordersPersistenceService = ordersPersistenceService;
    }

    public void processNotificationsForActiveOrders() {

        List<Orders> ordersList = fetchActiveOrders();

        if (!ordersList.isEmpty()) {
            log.debug("ParkedNotificationProcessor invoked and active ordersList retrieved" + ordersList.get(0).toString());
        }

        for (Orders order : ordersList) {
            log.info("Processing parked notifications for Order " + order.getWagOrderId());
            try {
                singleNotificationProcessor.processOneParkedNotificationPerOrder(order);
            } catch (Exception e) {
                log.error("Exception occurred while processing parked notification for order {}", order.getWagOrderId(), e);
                createIntervention(order.getWagOrderId(), e.getMessage());
            }
        }
    }

    private void createIntervention(Long wagOrderId, String message) {
        InterventionDetails interventionDetails = InterventionDetails.builder()
                .color(Color.RED)
                .agent("ethernet-engine")
                .status(Status.OPEN)
                .workflow(PbtdcOrderProcessConstants.ACT_ID_SUPPLIER_ACCEPT_PROCESS)
                .notes(message)
                .build();

        Orders order = ordersPersistenceService.findByOrderId(wagOrderId);

        log.warn("order {}, creating intervention {}", order.getWagOrderId(), interventionDetails);
        try {
            interventionService.createOrUpdateIntervention(order, interventionDetails);
        } catch (Exception ex) {
            log.error("Error while creating intervention for Order {}", order.getWagOrderId(), ex);
        }

    }

    public List<Orders> fetchActiveOrders() {
        List<Orders> ordersList = parkedNotificationsPersistenceService.findAllActiveOrders();
        return ordersList;
    }

}
