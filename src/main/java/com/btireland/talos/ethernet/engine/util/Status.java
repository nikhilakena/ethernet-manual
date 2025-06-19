package com.btireland.talos.ethernet.engine.util;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    OPEN("open"), CLOSED("closed");

    private String label;

    private Status(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
