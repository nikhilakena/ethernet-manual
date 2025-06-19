package com.btireland.talos.ethernet.engine.rest;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.service.ParkedNotificationProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
class ParkedNotificationProcessingControllerTest {

    @Mock
    private ParkedNotificationProcessor parkedNotificationProcessor;

    @InjectMocks
    private ParkedNotificationProcessingController parkedNotificationProcessingController;


    @Test
    void testProcessParkedNotification(){
        parkedNotificationProcessingController.processParkedNotifications();
        Mockito.verify(parkedNotificationProcessor, Mockito.times(1)).processNotificationsForActiveOrders();
    }
}
