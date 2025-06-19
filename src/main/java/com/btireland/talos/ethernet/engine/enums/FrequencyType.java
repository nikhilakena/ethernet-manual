package com.btireland.talos.ethernet.engine.enums;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum FrequencyType {
    ANNUAL("Annual"),
    MONTHLY("Monthly");

    FrequencyType(String value) {
        this.value = value;
    }

    private static Map<String, String> frequencyTypeMap;
    @Getter
    private final String value;

    private static void initializeMapping() {
        frequencyTypeMap = new HashMap<>();
        for (FrequencyType frequencyType : FrequencyType.values()) {
            frequencyTypeMap.put(frequencyType.name(), frequencyType.getValue());
        }
    }

    public static String getDisplayNameByCode(String frequencyCode) {
        if (frequencyTypeMap == null) {
            initializeMapping();
        }
        return frequencyTypeMap.get(frequencyCode);

    }

}
