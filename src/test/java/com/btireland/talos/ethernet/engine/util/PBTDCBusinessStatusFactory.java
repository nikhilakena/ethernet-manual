package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.domain.PBTDCBusinessStatus;

import java.time.LocalDate;

public class PBTDCBusinessStatusFactory {
    public static PBTDCBusinessStatus businessStatusAfterDS() {
        return PBTDCBusinessStatus.builder()
                .orderEntryAndValidationStatus("Customer Delay")
                .build();
    }

    public static PBTDCBusinessStatus businessStatusAfterDE() {
        return PBTDCBusinessStatus.builder()
                .orderEntryAndValidationStatus("WIP")
                .build();
    }

    public static PBTDCBusinessStatus bsForStatusNotificationWithPlanningStatus() {
        return PBTDCBusinessStatus.builder()
                .planningStatus("WIP")
                .orderEntryAndValidationStatus("Complete")
                .build();
    }

    public static PBTDCBusinessStatus bsForStatusNotificationWithInvalidStatus() {
        return PBTDCBusinessStatus.builder()
                .planningStatus("Complete")
                .orderEntryAndValidationStatus("Complete")
                .build();
    }

    public static PBTDCBusinessStatus bsForStatusNotificationWithAccessInstallationStatus() {
        return PBTDCBusinessStatus.builder()
                .planningStatus("Complete")
                .accessInstallation("WIP")
                .orderEntryAndValidationStatus("Complete")
                .build();
    }

    public static PBTDCBusinessStatus bsForStatusNotificationWithTestingCpeInstallationStatus() {
        return PBTDCBusinessStatus.builder()
                .planningStatus("Complete")
                .accessInstallation("Complete")
                .testingCpeInstallation("WIP")
                .orderEntryAndValidationStatus("Complete")
                .build();
    }

    public static PBTDCBusinessStatus businessStatusAfterUndeliveredNotification() {
        return PBTDCBusinessStatus.builder()
                .orderEntryAndValidationStatus("Complete")
                .planningStatus("Order Cancelled")
                .build();
    }

    public static PBTDCBusinessStatus businessStatusAfterAPTRNotification() {
        return PBTDCBusinessStatus.builder()
                .planningStatus("Complete")
                .accessInstallation("Complete")
                .testingCpeInstallation("Book Appointment")
                .orderEntryAndValidationStatus("Complete")
                .build();
    }

    public static PBTDCBusinessStatus businessStatusAfterAcceptNotification() {
        return PBTDCBusinessStatus.builder()
                .orderEntryAndValidationStatus("WIP")
                .deliveryDate(LocalDate.of(2022, 01, 01))
                .build();
    }

    public static PBTDCBusinessStatus businessStatusAfterConfirmationNotification() {
        return PBTDCBusinessStatus.builder()
                .deliveryDate(LocalDate.of(2022, 03, 03))
                .build();
    }

    public static PBTDCBusinessStatus businessStatusAfterCompleteNotification() {
        return PBTDCBusinessStatus.builder()
                .planningStatus("Complete")
                .accessInstallation("Complete")
                .testingCpeInstallation("Complete")
                .orderEntryAndValidationStatus("Complete")
                .networkProvisioning("Complete")
                .serviceCompleteAndOperational("Complete")
                .build();
    }
}
