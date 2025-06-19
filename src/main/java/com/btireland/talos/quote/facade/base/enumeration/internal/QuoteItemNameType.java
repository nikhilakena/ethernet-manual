package com.btireland.talos.quote.facade.base.enumeration.internal;

public enum QuoteItemNameType {

    A_END("a-end"),
    B_END("b-end"),
    LOGICAL("logical");
    private final String value;
    QuoteItemNameType(final String newValue) {
        value = newValue;
    }
    public String getValue() { return value; }

}
