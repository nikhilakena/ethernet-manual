package com.btireland.talos.quote.facade.base.enumeration.internal;

public enum ActionType {
    PROVIDE("P","New Access"),
    MODIFY("CH","Existing Access"),
    CEASE("C", "");

    private final String value;
    private final String displayText;

    ActionType(final String newValue, String displayText) {
        value = newValue;
        this.displayText = displayText;
    }
    public String getValue() {
        return value;
    }

    public String getDisplayText() {
        return displayText;
    }
}
