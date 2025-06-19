package com.btireland.talos.quote.facade.base.enumeration.internal;

public enum NetworkType {
    ON_NET("ON-NET","BT On-Net"),
    OFF_NET("OFF-NET","3rd Party Supplier");

    private final String value;
    private final String prompt;

    NetworkType(final String newValue, final String prompt) {
        value = newValue;
        this.prompt = prompt;
    }
    public String getValue() { return value; }

    public String getPrompt() {
        return prompt;
    }
}
