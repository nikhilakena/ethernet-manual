package com.btireland.talos.ethernet.engine.util;

public enum NotificationProcessedStatus {
    UNPROCESSED("U"),
    INPROGRESS("I"),
    PROCESSED("P"),
    ERRORED("E");

    private final String value;
    NotificationProcessedStatus(final String newValue) {
        value = newValue;
    }
    public String getValue() { return value; }
}
