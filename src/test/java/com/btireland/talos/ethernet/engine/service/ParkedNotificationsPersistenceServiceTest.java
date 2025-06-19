package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.domain.ParkedNotifications;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.exception.ValidationException;
import com.btireland.talos.ethernet.engine.repository.ParkedNotificationsRepository;
import com.btireland.talos.ethernet.engine.util.NotificationFactory;
import com.btireland.talos.ethernet.engine.util.OrderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@UnitTest
@ExtendWith(MockitoExtension.class)
class ParkedNotificationsPersistenceServiceTest {
    @Mock
    private ParkedNotificationsRepository parkedNotificationsRepository;

    @Mock
    private OrdersPersistenceService ordersPersistenceService;

    @InjectMocks
    private ParkedNotificationsPersistenceService parkedNotificationsPersistenceService;

    public ParkedNotificationsPersistenceServiceTest() {
    }

    @BeforeEach
    public void setup() {
        parkedNotificationsPersistenceService = new ParkedNotificationsPersistenceService(parkedNotificationsRepository, ordersPersistenceService, new ObjectMapper());
    }

    @Test
    void testCreateNotificationEntry() throws IOException, ValidationException {
        ParkedNotifications parkedNotifications = NotificationFactory.defaultParkedNotifications();
        Exception exception = null;

        Mockito.when(parkedNotificationsRepository.save(parkedNotifications)).thenReturn(parkedNotifications);
        ParkedNotifications actuals = parkedNotificationsPersistenceService.createNotificationEntry(parkedNotifications);

        Assertions.assertThat(actuals).isEqualTo(parkedNotifications);

    }

    @Test
    void testFindFirstByWagOrderId() throws IOException {
        List<ParkedNotifications> parkedNotificationsList = NotificationFactory.parkerNotificationsList();

        Mockito.when(parkedNotificationsRepository.findFirstByOrdersWagOrderIdAndProcessedStatusOrderByReceivedAtAsc(2L, "U")).thenReturn(parkedNotificationsList.get(0));
        ParkedNotifications parkedNotificationsListActual = parkedNotificationsPersistenceService.findFirstRecordByWagOrderId(2L, "U");
        Assertions.assertThat(parkedNotificationsList.get(0).getId()).isEqualTo(parkedNotificationsListActual.getId());

    }


    @Test
    void testUpdate() throws IOException {
        ParkedNotifications parkedNotifications = NotificationFactory.defaultParkedNotifications();
        Mockito.when(parkedNotificationsRepository.save(parkedNotifications)).thenReturn(parkedNotifications);
        ParkedNotifications actuals = parkedNotificationsPersistenceService.update(parkedNotifications);
        Assertions.assertThat(actuals).isEqualTo(parkedNotifications);
    }

    @Test
    @DisplayName("Persist Supplier Notifications")
    void persistSupplierNotifications() throws Exception {
        String reference = "TALOS-123-PBTDC-1";
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        when(ordersPersistenceService.findBySupplierOrderNumberNoRollbackonNotfound(reference)).thenReturn(pbtdcOrders);

        parkedNotificationsPersistenceService.persistSupplierNotifications("TALOS-123-PBTDC-1", NotificationFactory.undeliverableNotificationRequest());

        Mockito.verify(parkedNotificationsRepository, times(1)).save(Mockito.any(ParkedNotifications.class));
    }

    @Test
    @DisplayName("Populate Parked notification")
    void populateParkedNotifications() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        ParkedNotifications actual = NotificationFactory.defaultParkedNotifications();
        ParkedNotifications expected = parkedNotificationsPersistenceService.populateParkedNotifications(pbtdcOrders, NotificationFactory.undeliverableNotificationRequest());
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
