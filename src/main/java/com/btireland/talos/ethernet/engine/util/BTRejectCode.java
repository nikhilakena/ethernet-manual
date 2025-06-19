package com.btireland.talos.ethernet.engine.util;

public enum BTRejectCode {
    REJECT_CODE_105("105" ,"Order Undeliverable" );

    private final String rejectCode;
    private final String rejectReason;

    BTRejectCode(String rejectCode, String rejectReason) {
        this.rejectCode = rejectCode;
        this.rejectReason = rejectReason;
    }


    public String getRejectCode() {
        return rejectCode;
    }

    public String getRejectReason() {
        return rejectReason;
    }


}
