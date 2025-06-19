package com.btireland.talos.quote.facade.base.enumeration.internal;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum ConnectionType {

    ETHERWAY_STANDARD("Etherway Standard", "ETHERWAY_STANDARD"),
    ETHERWAY_DIVERSE("Etherway Diverse", "ETHERWAY_DIVERSE"),
    ETHERWAY_DIVERSE_PLUS("Etherway Diverse Plus", "ETHERWAY_DIVERSE_PLUS");

    private static Map<String, String> connectionTypeMap;
    @Getter
    private final String value;
    @Getter
    private final String prompt;
    ConnectionType(String prompt, String value) {
        this.prompt = prompt;
        this.value = value;
    }

    private static void initializeMapping() {
        connectionTypeMap = new HashMap<>();
        for (ConnectionType connectionType : ConnectionType.values()) {
            connectionTypeMap.put(connectionType.getValue(), connectionType.getPrompt());
        }
    }

    public static String getDisplayNameByCode(String connectionType) {
        if (connectionTypeMap == null) {
            initializeMapping();
        }
        return connectionTypeMap.get(connectionType);

    }

}
