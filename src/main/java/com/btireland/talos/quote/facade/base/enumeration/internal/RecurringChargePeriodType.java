package com.btireland.talos.quote.facade.base.enumeration.internal;

public enum RecurringChargePeriodType {

    MONTHLY("Monthly"),
    ANNUAL("Annual");

    private final String value;
    RecurringChargePeriodType(final String newValue) {
        value = newValue;
    }
    public String getValue() { return value; }

}
