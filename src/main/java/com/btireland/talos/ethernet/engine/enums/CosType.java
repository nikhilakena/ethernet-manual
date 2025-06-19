package com.btireland.talos.ethernet.engine.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum CosType {
    PREMIUM_5("PREMIUM_5" ,"Premium 5 (5% AF)"),
    PREMIUM_10("PREMIUM_10" ,"Premium 10 (10% AF)" ),
    PREMIUM_50("PREMIUM_50" ,"Premium 50 (50% AF)" ),
    PREMIUM_100("PREMIUM_100" ,"Premium 100 (100% AF)" ),
    PREMIUM_EXPRESS("PREMIUM_EXPRESS" ,"Premium Express (100% EF)" ),
    PRIMARY("PRIMARY" ,"Primary (0% AF)" );

    CosType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Getter
    private final String code;

    @Getter
    private final String value;

    private static Map<String, String> cosTypeMap;

    private static void initializeMapping() {
        cosTypeMap = new HashMap<>();
        for (CosType costType : CosType.values()) {
            cosTypeMap.put(costType.getCode(),costType.getValue());
        }
    }

    public static String getDisplayNameByCode(String frequencyCode) {
        if (cosTypeMap == null) {
            initializeMapping();
        }
        return cosTypeMap.get(frequencyCode);

    }

}
