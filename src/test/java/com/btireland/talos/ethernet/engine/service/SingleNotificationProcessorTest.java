package com.btireland.talos.ethernet.engine.service;


import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.facade.OrderMapper;
import com.btireland.talos.ethernet.engine.util.Color;
import com.btireland.talos.ethernet.engine.util.NotificationFactory;
import com.btireland.talos.ethernet.engine.util.NotificationProcessedStatus;
import com.btireland.talos.ethernet.engine.util.Status;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@UnitTest
class SingleNotificationProcessorTest {

    @Mock
    MessageCorrelationBuilder messageCorrelationBuilder;
    @Mock
    private ParkedNotificationsPersistenceService parkedNotificationsPersistenceService;
    @Mock
    private RuntimeService runtimeService;
    @Mock
    private InterventionService interventionService;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private SingleNotificationProcessor singleNotificationProcessor;

    @Test
    @DisplayName("Appropriate Message is invoked of the process instance")
    void processOneParkedNotificationPerOrder() throws IOException {
        Orders order = Orders.builder().wagOrderId(1L).build();

        when(parkedNotificationsPersistenceService.findFirstRecordByWagOrderId(anyLong(), anyString())).thenReturn(NotificationFactory.defaultParkedNotifications());
        when(runtimeService.createMessageCorrelation(Mockito.anyString())).thenReturn(messageCorrelationBuilder);
        when(runtimeService.createMessageCorrelation(Mockito.anyString()).processInstanceBusinessKey(Mockito.anyString())).thenReturn(messageCorrelationBuilder);
        when(runtimeService.createMessageCorrelation(Mockito.anyString()).setVariables(Mockito.anyMap())).thenReturn(messageCorrelationBuilder);

        singleNotificationProcessor.processOneParkedNotificationPerOrder(order);

        verify(messageCorrelationBuilder).processInstanceBusinessKey("1");
    }

    @Test
    @DisplayName("Notifications other than Accept is not processed for an order with intervention")
    void processOneParkedNotificationForIntervention() throws IOException {
        Orders order = Orders.builder().wagOrderId(1L)
                .interventionDetails(Arrays.asList(InterventionDetails.builder().status(Status.OPEN).color(Color.RED).build()))
                .interventionFlag(true).build();
        when(parkedNotificationsPersistenceService.findFirstRecordByWagOrderId(anyLong(), anyString())).thenReturn(NotificationFactory.defaultParkedNotifications());
        singleNotificationProcessor.processOneParkedNotificationPerOrder(order);

        verifyNoInteractions(messageCorrelationBuilder);
        verifyNoInteractions(interventionService);
    }

    @Test
    @DisplayName("Appropriate Message is invoked of the process instance when accept notification received for Intervention order")
    void processOneParkedNotificationForAcceptforInterventionOrder() throws IOException {
        Orders order = Orders.builder().wagOrderId(1L)
                .interventionDetails(Arrays.asList(InterventionDetails.builder().status(Status.OPEN).color(Color.RED).build()))
                .interventionFlag(true).build();
        Pbtdc pbtdcWithClosedIntervention = Pbtdc.builder().wagOrderId(1L)
                .interventionDetails(Arrays.asList(InterventionDetails.builder().status(Status.CLOSED).color(Color.CLEAR).build()))
                .interventionFlag(true).build();
//        when(parkedNotificationsPersistenceService.findFirstRecordByWagOrderId(anyLong(), anyString())).thenReturn(NotificationFactory.parkedNotificationForAccept());
        when(parkedNotificationsPersistenceService.findFirstRecordByWagOrderIdAndNotificationType(1L,"A", NotificationProcessedStatus.UNPROCESSED.getValue())).thenReturn(NotificationFactory.parkedNotificationForAccept());
        when(interventionService.createOrUpdateIntervention(Mockito.any(Orders.class), Mockito.any(InterventionDetails.class))).thenReturn(pbtdcWithClosedIntervention);
        when(runtimeService.createMessageCorrelation(Mockito.anyString())).thenReturn(messageCorrelationBuilder);
        when(runtimeService.createMessageCorrelation(Mockito.anyString()).processInstanceBusinessKey(Mockito.anyString())).thenReturn(messageCorrelationBuilder);
        when(runtimeService.createMessageCorrelation(Mockito.anyString()).setVariables(Mockito.anyMap())).thenReturn(messageCorrelationBuilder);

        singleNotificationProcessor.processOneParkedNotificationPerOrder(order);

        verify(messageCorrelationBuilder).processInstanceBusinessKey("1");
        verify(interventionService, times(1)).createOrUpdateIntervention(Mockito.any(Orders.class), Mockito.any(InterventionDetails.class));
    }

}
