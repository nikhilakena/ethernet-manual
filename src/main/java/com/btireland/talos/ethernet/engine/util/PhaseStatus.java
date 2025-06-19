package com.btireland.talos.ethernet.engine.util;

public enum PhaseStatus {
    COMPLETE("Complete" ),
    REJECTED("Rejected" ),
    UNDELIVERABLE("Undeliverable"),
    CUSTOMER_DELAY("Customer Delay"),
    ORDER_CANCELLED("Order Cancelled"),
    WIP("WIP"),
    BOOK_APPOINTMENT("Book Appointment");

    private final String phaseStatusValue;

    PhaseStatus(String phaseStatusValue) {
        this.phaseStatusValue = phaseStatusValue;
    }

    public String getPhaseStatusValue() {
        return phaseStatusValue;
    }
}
