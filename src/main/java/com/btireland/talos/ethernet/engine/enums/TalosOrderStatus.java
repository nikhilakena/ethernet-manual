package com.btireland.talos.ethernet.engine.enums;

public enum TalosOrderStatus {
    TALOS_COMPLETE("talos complete"),
    REJECTED("rejected"),
    CREATED("Created"),
    DELAYED("Delayed");

    private final String value;
    TalosOrderStatus(final String newValue) {
        value = newValue;
    }
    public String getValue() { return value; }
}
