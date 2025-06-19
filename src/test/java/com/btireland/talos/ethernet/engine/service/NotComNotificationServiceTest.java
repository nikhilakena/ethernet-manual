package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.rest.exception.NotFoundException;
import com.btireland.talos.core.common.rest.exceptionmessage.ExceptionResponse;
import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.client.asset.notcom.NotificationClient;
import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.domain.RejectionDetails;
import com.btireland.talos.ethernet.engine.dto.RejectionDetailsDTO;
import com.btireland.talos.ethernet.engine.exception.NotificationClientException;
import com.btireland.talos.ethernet.engine.facade.PbtdcMapper;
import com.btireland.talos.ethernet.engine.util.BTRejectCode;
import com.btireland.talos.ethernet.engine.util.NotificationFactory;
import com.btireland.talos.ethernet.engine.util.OrderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;

@UnitTest
@ExtendWith(MockitoExtension.class)
class NotComNotificationServiceTest {


    @Mock
    private NotificationClient notificationClient;

    private NotComNotificationsService notComNotificationsService;

    @Mock
    private PbtdcMapper pbtdcMapper;

    @BeforeEach
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        notComNotificationsService = new NotComNotificationsService(notificationClient, mapper, pbtdcMapper);
    }


    @Test
    void testCreateNotification() throws Exception {
        Notifications expected = NotificationFactory.undeliverableNotcomNotification();
        Mockito.when(notificationClient.createNotification(Mockito.any(Notifications.class))).thenReturn(expected);
        Mockito.when(pbtdcMapper.toRejectionDetailsDTO(Mockito.any(RejectionDetails.class))).thenReturn(RejectionDetailsDTO.builder().rejectCode(BTRejectCode.REJECT_CODE_105.getRejectCode()).rejectReason("Cancellation").build());
        Notifications actuals = notComNotificationsService.createNotification("U", OrderFactory.ordersWithRejectDetails());

        Assertions.assertEquals(expected, actuals);


    }

    @Test
    void testCreateNotification_FeignExcep() throws IOException {
        Map<String, Collection<String>> headers = new HashMap<>();
        Mockito.when(notificationClient.createNotification(Mockito.any(Notifications.class))).thenThrow(new FeignException.BadRequest("Bad Request", feign.Request.create(Request.HttpMethod.POST, "/api/v1/notification", headers, null, null, null),null, headers));
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> notComNotificationsService.createNotification("U", OrderFactory.defaultPbtdcOrders()))
                .isInstanceOf(NotificationClientException.class);
        Mockito.verify(notificationClient, times(1)).createNotification(Mockito.any(Notifications.class));
    }

    @Test
    void testCreateNotification_NotFoundException() throws IOException {
        Map<String, Collection<String>> headers = new HashMap<>();
        Mockito.when(notificationClient.createNotification(Mockito.any(Notifications.class))).thenThrow(new NotFoundException(feign.Request.create(Request.HttpMethod.POST, "/api/v1/notification", headers, null, null, null),null, ExceptionResponse.builder().build()));
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> notComNotificationsService.createNotification("U", OrderFactory.defaultPbtdcOrders()))
                .isInstanceOf(NotificationClientException.class);
        Mockito.verify(notificationClient, times(1)).createNotification(Mockito.any(Notifications.class));
    }

    @Test
    void testCreateNotificationWithOrderManagerDetails() throws Exception {
        Notifications expected = NotificationFactory.acceptNotcomNotification();
        Mockito.when(notificationClient.createNotification(Mockito.any(Notifications.class))).thenReturn(expected);
        Notifications actuals = notComNotificationsService.createNotification("A", OrderFactory.ordersWithOrderManagerDetails());

        Assertions.assertEquals(expected, actuals);
    }

    @Test
    void testCreateNotificationWithCustomerDelayDetails() throws Exception {
        Notifications expected = NotificationFactory.delayEndNotcomNotification();
        Mockito.when(notificationClient.createNotification(Mockito.any(Notifications.class))).thenReturn(expected);
        Notifications actuals = notComNotificationsService.createNotification("DE", OrderFactory.ordersWithCustomerDelayDetails());

        Assertions.assertEquals(expected, actuals);
    }
}
