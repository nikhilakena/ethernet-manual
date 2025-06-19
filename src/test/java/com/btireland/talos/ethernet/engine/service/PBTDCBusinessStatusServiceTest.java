package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.domain.PBTDCBusinessStatus;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.util.OrderFactory;
import com.btireland.talos.ethernet.engine.util.PBTDCBusinessStatusFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.assertj.core.api.Assertions;

import java.time.LocalDate;

@UnitTest
@ExtendWith(MockitoExtension.class)
class PBTDCBusinessStatusServiceTest {

    PBTDCBusinessStatusService pbtdcBusinessStatusService;

    @BeforeEach
    public void setup() {
        pbtdcBusinessStatusService = new PBTDCBusinessStatusService();
    }

    @Test
    @DisplayName("Business Status is updated when DS notification is received")
    void updateBusinessStatusForDelayStart() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.businessStatusAfterDS();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForDelayStart(pbtdcOrders.getBusinessStatus());
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is updated when DE notification is received")
    void updateBusinessStatusForDelayEnd() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.businessStatusAfterDE();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForDelayEnd(pbtdcOrders.getBusinessStatus());
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }


    @Test
    @DisplayName("Business Status is updated when Status notification is received with planning order status")
    void updateBusinessStatusForStatusNotificationWithPlanningOrderStatus() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.bsForStatusNotificationWithPlanningStatus();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForStatusNotification(pbtdcOrders.getBusinessStatus(), "Network Planning");
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is updated when Status notification is received with no planning order status")
    void updateBusinessStatusForStatusNotificationWithNoPlanningOrderStatus() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.bsForStatusNotificationWithAccessInstallationStatus();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForStatusNotification(pbtdcOrders.getBusinessStatus(), "Jumper And Patches");
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is updated when Status notification is received with access installation order status")
    void updateBusinessStatusForStatusNotificationWithAccessInstallationOrderStatus() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.bsForStatusNotificationWithAccessInstallationStatus();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForStatusNotification(pbtdcOrders.getBusinessStatus(), "Etherflow Pending Delivery");
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is updated when Status notification is received with no access installation order status")
    void updateBusinessStatusForStatusNotificationWithNoAccessInstallationOrderStatus() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.bsForStatusNotificationWithTestingCpeInstallationStatus();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForStatusNotification(pbtdcOrders.getBusinessStatus(), "Fault Issue");
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is updated when Status notification is received with testing cpe installation order status")
    void updateBusinessStatusForStatusNotificationWithTestingCpeInstallationOrderStatus() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.bsForStatusNotificationWithTestingCpeInstallationStatus();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForStatusNotification(pbtdcOrders.getBusinessStatus(), "Service Software Build");
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is updated when Status notification is received with no testing cpe installation order status")
    void updateBusinessStatusForStatusNotificationWithNoTestingCpeInstallationOrderStatus() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.bsForStatusNotificationWithPlanningStatus();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForStatusNotification(pbtdcOrders.getBusinessStatus(), "Interconnect Tech Info");
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is not updated when Status notification is received with invalid order status")
    void updateBusinessStatusForStatusNotificationWithInvalidOrderStatus() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.bsForStatusNotificationWithInvalidStatus();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForStatusNotification(pbtdcOrders.getBusinessStatus(), "Invalid Status");
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is updated when Undelivered notification is received")
    void updateBusinessStatusForUndeliveredNotification() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        pbtdcOrders.getBusinessStatus().setOrderEntryAndValidationStatus("Complete");
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.businessStatusAfterUndeliveredNotification();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForUndelivered(pbtdcOrders.getBusinessStatus());
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is updated when APTR notification is received")
    void updateBusinessStatusForAPTRNotification() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.businessStatusAfterAPTRNotification();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForAPTR(pbtdcOrders.getBusinessStatus());
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is updated when Accept notification is received")
    void updateBusinessStatusForAcceptNotification() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.businessStatusAfterAcceptNotification();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForAccept(pbtdcOrders.getBusinessStatus(), LocalDate.of(2022, 01, 01));
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is updated when Confirmation notification is received")
    void updateBusinessStatusForConfirmationNotification() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.businessStatusAfterConfirmationNotification();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForConfirmation(pbtdcOrders.getBusinessStatus(), LocalDate.of(2022, 03, 03));
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Business Status is updated when Complete notification is received")
    void updateBusinessStatusForCompleteNotification() throws Exception {
        Pbtdc pbtdcOrders = OrderFactory.defaultPbtdcOrders();
        PBTDCBusinessStatus expected = PBTDCBusinessStatusFactory.businessStatusAfterCompleteNotification();
        PBTDCBusinessStatus actual = pbtdcBusinessStatusService.updateBusinessStatusForComplete(pbtdcOrders.getBusinessStatus());
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
