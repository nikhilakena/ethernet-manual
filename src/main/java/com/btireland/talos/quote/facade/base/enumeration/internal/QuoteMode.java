package com.btireland.talos.quote.facade.base.enumeration.internal;

public enum QuoteMode {

    SYNC("sync"),
    ASYNC("async");
    private final String value;

    QuoteMode(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }

}
