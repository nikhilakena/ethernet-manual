package com.btireland.talos.ethernet.engine.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum OfflinePricingRejectCode {
    BANDWIDTH_NOT_AVAILABLE("access_bw_exch_capacity","Access bandwidth not available off net site (1gig vs 10Gig)"),
    EIRCODE_NOT_AVAILABLE("eircode_not_available","Cannot Quote Non-Commercial Eircode"),
    EIRCODE_NOT_FOUND("eircode_not_found","Eircode Unknown"),
    EXCHANGE_NOT_AVAILABLE("af_ef_not_available","Exchange capacity not available (COS)"),
    EXCHANGE_NOT_ACTIVE("location_not_available","Exchange Not active"),
    MISCELLANEOUS("miscellaneous","Miscellaneous"),
    ACCESS_SUPPLIER_INVALID("access_supplier_invalid","Requested access supplier is invalid"),
    ACCESS_SUPPLIER_NOT_AVAILABLE("access_supplier_not_available","Requested access supplier not available"),
    SECURE_LINE_NOT_AVAILABLE("diverse_line_not_available","Line Diverse/Diverse Plus is not available");

    @Getter
    private final String rejectCode;

    @Getter
    private final String rejectReason;

    private static Map<String, String> rejectMap;


    OfflinePricingRejectCode(String rejectCode, String rejectReason) {
        this.rejectCode = rejectCode;
        this.rejectReason = rejectReason;
    }

    private static void initializeMapping() {
        rejectMap = new HashMap<>();
        for (OfflinePricingRejectCode reject : OfflinePricingRejectCode.values()) {
            rejectMap.put(reject.getRejectCode(), reject.getRejectReason());
        }
    }

    public static String getRejectReasonByCode(String rejectCode) {
        if (rejectMap == null) {
            initializeMapping();
        }
        return rejectMap.get(rejectCode);

    }
}
