package com.btireland.talos.ethernet.engine.service;


import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.domain.ParkedNotifications;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.util.PbtdcFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@UnitTest
class ParkedNotificationProcessorTest {

    @Mock
    private ParkedNotificationsPersistenceService parkedNotificationsPersistenceService;

    @Mock
    private SingleNotificationProcessor singleNotificationProcessor;

    @Mock
    private InterventionService interventionService;

    @Mock
    private OrdersPersistenceService ordersPersistenceService;

    @InjectMocks
    private ParkedNotificationProcessor parkedNotificationProcessor;

    @Test
    @DisplayName("No notifications to be processed. Do nothing in that case")
    void processNotificationsForActiveOrders_donothing_when_notifications_empty() {
        when(parkedNotificationsPersistenceService.findAllActiveOrders()).thenReturn(List.of());
        parkedNotificationProcessor.processNotificationsForActiveOrders();
        verifyNoInteractions(singleNotificationProcessor);
    }

    @Test
    @DisplayName("2 notifications to be processed. workflow service must be invoked for each notification")
    void processNotificationsForActiveOrders() throws Exception {
        Orders order1 = Orders.builder().wagOrderId(1L).build();
        Orders order2 = Orders.builder().wagOrderId(2L).build();
        when(parkedNotificationsPersistenceService.findAllActiveOrders()).thenReturn(List.of(order1,order2));
        parkedNotificationProcessor.processNotificationsForActiveOrders();

        verify(singleNotificationProcessor).processOneParkedNotificationPerOrder(order1);
        verify(singleNotificationProcessor).processOneParkedNotificationPerOrder(order2);
    }

    @Test
    @DisplayName("The second notification processing throws an exception. Its should create a manual intervention")
    @MockitoSettings(strictness = Strictness.WARN)
    void processNotificationsForActiveOrders_when_process_notification_throw_exception() throws Exception {
        Orders order1 = Orders.builder().wagOrderId(1L).build();
        Orders order2 = Orders.builder().wagOrderId(2L).build();
        when(parkedNotificationsPersistenceService.findAllActiveOrders()).thenReturn(List.of(order1, order2));
        doThrow(new RuntimeException("Notification processing error")).when(singleNotificationProcessor).processOneParkedNotificationPerOrder(order2);

        ParkedNotifications secondNotification = ParkedNotifications.builder().id(2L).build();
        when(ordersPersistenceService.findByOrderId(2L)).thenReturn(PbtdcFactory.defaultPbtdc());
        parkedNotificationProcessor.processNotificationsForActiveOrders();

        verify(interventionService, times(1)).createOrUpdateIntervention(Mockito.any(Orders.class), Mockito.any(InterventionDetails.class));
    }

    @Test
    @DisplayName("The first notification processing throws an exception while creating intervention. The second notification should be processed successfully")
    @MockitoSettings(strictness = Strictness.WARN)
    void processNotificationsForActiveOrders_when_creating_interveniton_throw_exception() throws Exception {
        Orders order1 = Orders.builder().wagOrderId(1L).build();
        Orders order2 = Orders.builder().wagOrderId(2L).build();
        when(parkedNotificationsPersistenceService.findAllActiveOrders()).thenReturn(List.of(order1, order2));
        doThrow(new RuntimeException("Notification processing error")).when(singleNotificationProcessor).processOneParkedNotificationPerOrder(order1);
        doThrow(new RuntimeException("Random Error")).when(interventionService).createOrUpdateIntervention(Mockito.any(Pbtdc.class), Mockito.any(InterventionDetails.class));

        ParkedNotifications secondNotification = ParkedNotifications.builder().id(2L).build();
        when(ordersPersistenceService.findByOrderId(1L)).thenReturn(PbtdcFactory.defaultPbtdc());
        parkedNotificationProcessor.processNotificationsForActiveOrders();

        verify(interventionService, times(1)).createOrUpdateIntervention(Mockito.any(Orders.class), Mockito.any(InterventionDetails.class));
        verify(singleNotificationProcessor).processOneParkedNotificationPerOrder(order1);
        verify(singleNotificationProcessor).processOneParkedNotificationPerOrder(order2);
    }
}
