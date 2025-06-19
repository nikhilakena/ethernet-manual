package com.btireland.talos.ethernet.engine.rest;


import com.btireland.talos.ethernet.engine.service.ParkedNotificationProcessor;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name= "Notification Processing Controller", description = "Controller to invoke Notification processing")
@RequestMapping("/api/v1/batch/notifications/process")
public class ParkedNotificationProcessingController {
    private ParkedNotificationProcessor parkedNotificationProcessor;

    
    public ParkedNotificationProcessingController(ParkedNotificationProcessor parkedNotificationProcessor) {
            this.parkedNotificationProcessor = parkedNotificationProcessor;
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void processParkedNotifications() {
        parkedNotificationProcessor.processNotificationsForActiveOrders();

    }


}
