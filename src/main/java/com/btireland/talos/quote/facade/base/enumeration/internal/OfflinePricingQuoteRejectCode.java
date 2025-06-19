package com.btireland.talos.quote.facade.base.enumeration.internal;

public enum OfflinePricingQuoteRejectCode {
    BANDWIDTH_NOT_AVAILABLE("access_bw_exch_capacity", "Access bandwidth not available off net site (1gig vs 10Gig)"),
    EIRCODE_NOT_AVAILABLE("eircode_not_available", "Cannot Quote Non-Commercial Eircode"),
    EIRCODE_NOT_FOUND("eircode_not_found", "Eircode Unknown"),
    EXCHANGE_NOT_AVAILABLE("af_ef_not_available", "Exchange capacity not available (COS)"),
    EXCHANGE_NOT_ACTIVE("location_not_available", "Exchange Not active"),
    MISCELLANEOUS("miscellaneous", "Miscellaneous"),
    ACCESS_SUPPLIER_INVALID("access_supplier_invalid", "Requested access supplier is invalid"),
    ACCESS_SUPPLIER_NOT_AVAILABLE("access_supplier_not_available", "Requested access supplier not available"),
    SECURE_LINE_NOT_AVAILABLE("diverse_line_not_available","Line Diverse/Diverse Plus is not available");

    private final String rejectCode;
    private final String rejectReason;

    OfflinePricingQuoteRejectCode(String rejectCode, String rejectReason) {
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
