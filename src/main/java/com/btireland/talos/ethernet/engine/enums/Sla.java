package com.btireland.talos.ethernet.engine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public enum Sla {
    STANDARD("Standard"),
    ENHANCED("Enhanced");

    @Getter
    private final String value;

    private static Map<String, String> slaMap;

    private static void initializeMapping() {
        slaMap = new HashMap<>();
        for (Sla frequencyType : Sla.values()) {
            slaMap.put(frequencyType.name(),frequencyType.getValue());
        }
    }

    public static String getDisplayNameByCode(String frequencyCode) {
        if (slaMap == null) {
            initializeMapping();
        }
        return slaMap.get(frequencyCode);

    }

}
