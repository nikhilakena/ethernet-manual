package com.btireland.talos.ethernet.engine.util;

public enum OrderNumberPatterns {

    NA("^NA$", "Invalid Order Number."),
    BT("^BT-", "Invalid Order Number. (Must not start with BT-)"),
    EL("^EL-", "Invalid Order Number. (Must not start with EL-)"),
    SL("^SL-", "Invalid Order Number. (Must not start with SL-)"),
    NBIL("^NBIL-", "Invalid Order Number. (Must not start with NBIL-)"),
    EPL("^EPL-", "Invalid Order Number. (Must not start with EPL-)"),
    VOIPL("^VOIPL-", "Invalid Order Number. (Must not start with VOIPL-)"),
    CA("^CA-", "Invalid Order Number. (Must not start with CA-)"),
    CN_NOT("^CN_NOT-", "Invalid Order Number. (Must not start with CN_NOT-)"),
    MSO("^MSO-", "Invalid Order Number. (Must not start with MSO-)");

    private final String pattern;
    private final String errorMessage;

    OrderNumberPatterns(String pattern, String errorMessage) {
        this.pattern = pattern;
        this.errorMessage = errorMessage;
    }

    public String getPattern() {
        return pattern;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
