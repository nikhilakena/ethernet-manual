package com.btireland.talos.ethernet.engine.mq;

import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.dto.NotificationDTO;
import com.btireland.talos.ethernet.engine.exception.ValidationException;
import com.btireland.talos.ethernet.engine.service.ParkedNotificationsPersistenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SupplierNotificationConsumer {

    private final ApplicationConfiguration applicationConfiguration;

    private ParkedNotificationsPersistenceService parkedNotificationsPersistenceService;


    public SupplierNotificationConsumer(ApplicationConfiguration applicationConfiguration, ParkedNotificationsPersistenceService parkedNotificationsPersistenceService) {
        this.applicationConfiguration = applicationConfiguration;
        this.parkedNotificationsPersistenceService = parkedNotificationsPersistenceService;
    }

    //Use statically configured queue for the Topic. Since we use same name using same property for both address and queue
    @JmsListener(destination = "${application.queue.supplier.notif.topic}")
    public void receiveMessage(NotificationDTO notification) throws JsonProcessingException, ValidationException {
        log.debug("Received notifications data from Notification MQ Topic ::" + notification);

        String supplierOrderNumber = notification.getReference();
        Boolean processNotification = false;
        for (ApplicationConfiguration.NotificationToProcessItem notificationToProcessItem : applicationConfiguration.getNotificationToProcess()) {
            if (notificationToProcessItem.getSource().equalsIgnoreCase(notification.getSource())) {
                processNotification = notificationToProcessItem.getTypes().contains(notification.getType());
                break;
            }
        }
        if (processNotification) {
            //Persist notification in db for processing
            parkedNotificationsPersistenceService.persistSupplierNotifications(supplierOrderNumber, notification);
        } else {
            log.warn("Notification Type {} not configured for processing" , notification.getType());
        }

    }

}