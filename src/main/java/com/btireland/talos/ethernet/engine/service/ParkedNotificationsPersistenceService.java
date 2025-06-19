package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.rest.exception.ResourceNotFoundException;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.domain.ParkedNotifications;
import com.btireland.talos.ethernet.engine.dto.NotificationDTO;
import com.btireland.talos.ethernet.engine.exception.ValidationException;
import com.btireland.talos.ethernet.engine.repository.ParkedNotificationsRepository;
import com.btireland.talos.ethernet.engine.util.NotificationProcessedStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ParkedNotificationsPersistenceService {

    private ParkedNotificationsRepository parkedNotificationsRepository;

    private OrdersPersistenceService ordersPersistenceService;

    private ObjectMapper objectMapper;

    public ParkedNotificationsPersistenceService(ParkedNotificationsRepository parkedNotificationsRepository, OrdersPersistenceService ordersPersistenceService, ObjectMapper objectMapper) {
        this.parkedNotificationsRepository = parkedNotificationsRepository;
        this.ordersPersistenceService = ordersPersistenceService;
        this.objectMapper = objectMapper;
    }

    public ParkedNotifications createNotificationEntry(ParkedNotifications parkedNotifications) throws ValidationException {

        log.info("Request order id " + parkedNotifications.getOrders().getWagOrderId());

        //Create the ParkedNotifications record and persist
        return parkedNotificationsRepository.save(parkedNotifications);
    }

    public ParkedNotifications findFirstRecordByWagOrderId(Long orderId, String processedStatus) {
        return parkedNotificationsRepository.findFirstByOrdersWagOrderIdAndProcessedStatusOrderByReceivedAtAsc(orderId, processedStatus);
    }

    public ParkedNotifications findFirstRecordByWagOrderIdAndNotificationType(Long orderId, String notificationType, String processedStatus) {
        return parkedNotificationsRepository.findFirstByOrdersWagOrderIdAndNotificationTypeAndProcessedStatusOrderByReceivedAtAsc(orderId, notificationType, processedStatus);
    }

    public ParkedNotifications findById(Long notificationId){
        return parkedNotificationsRepository.findById(notificationId).get();
    }

    public ParkedNotifications update(ParkedNotifications parkedNotifications) {
        return parkedNotificationsRepository.save(parkedNotifications);
    }

    public void deleteById(Long talosOrderId) {
        parkedNotificationsRepository.deleteById(talosOrderId);
    }

    /**
     * Use RSQL framework to parse a String of filters and transform it into a J
     *
     * @param filters
     * @return
     */
    public Page<ParkedNotifications> findByFilters(String filters, Pageable pageable) {
        return parkedNotificationsRepository.findAllByRsqlQuery(filters, pageable);
    }

    public List<Orders> findAllActiveOrders() {
        return parkedNotificationsRepository.findAllActiveOrders();
    }

    public void persistSupplierNotifications(String supplierOrderNumber, NotificationDTO notification) {

        Orders order = null;
        try {
            order = ordersPersistenceService.findBySupplierOrderNumberNoRollbackonNotfound(supplierOrderNumber);
        } catch (ResourceNotFoundException rne) {
            log.info("No records exist in the system , disregarding the supplier notification of type {} with reference {} ",  notification.getType(), notification.getReference());
            return;
        }

        log.info("NotCom Notification Event received for Notification Type : " + notification.getType());
        log.info("NotCom Notification Event received for wagOrderID  : " + order.getWagOrderId());
        log.info("NotCom Notification Event received with talos_order_id : " + order.getId());

        try {
            //Store the notifications in the parked_notifications table for sequential processing by scheduler
            ParkedNotifications parkedNotification = populateParkedNotifications(order, notification);
            if (parkedNotification != null) {
                createNotificationEntry(parkedNotification);
            }

        } catch (Exception e) {
            log.error("Exception occurred while storing notifications for the order in intervention", e);
        }

    }

    public ParkedNotifications populateParkedNotifications(Orders order, NotificationDTO supplierNotification) {

        ParkedNotifications parkedNotificationRecord = null;
        try {

            parkedNotificationRecord = ParkedNotifications.builder()
                    .orders(order)
                    .notificationType(supplierNotification.getType())
                    .processedStatus(NotificationProcessedStatus.UNPROCESSED.getValue())
                    .supplierNotification(objectMapper.writeValueAsBytes(supplierNotification))
                    .build();


        } catch (JsonProcessingException exp) {
            log.error("Exception occurred while serializing supplier notification", exp);
        }

        return parkedNotificationRecord;
    }


}
