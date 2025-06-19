package com.btireland.talos.quote.facade.dto.notcom;

public class RejectionDetails {

    private String rejectCode;

    private String rejectReason;

    public RejectionDetails() {
    }

    public RejectionDetails(String rejectCode, String rejectReason) {
        this.rejectCode = rejectCode;
        this.rejectReason = rejectReason;
    }

    public String getRejectCode() {
        return rejectCode;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    @Override
    public String toString() {
        return "RejectionDetails{" +
                "rejectCode='" + rejectCode + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                '}';
    }
}
