package com.btireland.talos.quote.facade.base.enumeration.internal;

public enum SlaType {
    STANDARD("Standard"),
    ENHANCED("Enhanced"),
    PREMIUM("Premium");

    private final String value;

    SlaType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
