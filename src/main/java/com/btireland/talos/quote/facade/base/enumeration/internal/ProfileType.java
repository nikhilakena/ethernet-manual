package com.btireland.talos.quote.facade.base.enumeration.internal;

public enum ProfileType {
    PRIMARY("Primary (0% AF)"),
    PREMIUM_5("Premium 5 (5% AF)"),
    PREMIUM_10("Premium 10 (10% AF)"),
    PREMIUM_50("Premium 50 (50% AF)"),
    PREMIUM_100("Premium 100 (100% AF)"),
    PREMIUM_EXPRESS("Premium Express (100% EF)");

    private final String value;

    ProfileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}