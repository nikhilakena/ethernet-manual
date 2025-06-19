package com.btireland.talos.ethernet.engine.util;

public enum QuoteStatus {
    ORDERED("Ordered"),
    REJECTED("Rejected"),
    ERROR("Error"),
    QUOTED("Quoted"),
    EXPIRED("Expired"),
    NO_BID("No Bid");

    private final String value;
    QuoteStatus(final String newValue) {
        value = newValue;
    }
    public String getValue() { return value; }
}
