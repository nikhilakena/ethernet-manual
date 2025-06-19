package com.btireland.talos.quote.facade.base.enumeration.internal;

public enum QuoteStateType {
    ORDERED("Ordered"),
    REJECTED("Rejected"),
    ERROR("Error"),
    QUOTED("Quoted"),
    EXPIRED("Expired"),
    NO_BID("No Bid"),
    PENDING("Pending");

    private final String value;

    QuoteStateType(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
