package com.btireland.talos.ethernet.engine.util;

import java.util.HashMap;
import java.util.Map;

public enum NotificationTypes {
    ACCEPT("A","SupplierAccept"),
    UNDELIVERABLE("U","SupplierUndeliverable"),
    DELAY_START("DS","SupplierCustomerDelayStart"),
    DELAY_END("DE","SupplierCustomerDelayEnd"),
    CIRCUIT_ID("CI","SupplierCircuitId"),
    APPOINTMENT_REQUEST("APTR", "SupplierAppointmentRequest"),
    STATUS("S", "SupplierStatus"),
    CONFIRMATION("CF", "SupplierConfirmation"),
    NOTES("N", "SupplierNotes"),
    COMPLETE("C", "SupplierCompleted"),
    ORDER_MANAGER_DETAILS("OMD", "SupplierOrderManagerDetails"),
    GLAN_ID_UPDATION("GIU", "SupplierGlanIdUpdation"),
    DELAYED("D", "SupplierDelayed");

    private static Map<String, String> notificationTypeMap;
    private final String messageName;
    private final String notificationCode;

    public String getMessageName() {
        return messageName;
    }

    public String getNotificationCode() {
        return notificationCode;
    }

    private NotificationTypes(String notificationCode, String messageName) {
        this.notificationCode = notificationCode;
        this.messageName = messageName;
    }

    public static String getMessageNameByNotificationCode(String notificationCode) {
        if (notificationTypeMap == null) {
            initializeMapping();
        }
        if (notificationTypeMap.containsKey(notificationCode)) {
            return notificationTypeMap.get(notificationCode);
        }
        return null;
    }

    private static void initializeMapping() {
        notificationTypeMap = new HashMap<>();
        for (NotificationTypes s : NotificationTypes.values()) {
            notificationTypeMap.put(s.getNotificationCode(),s.getMessageName());
        }
    }



}
