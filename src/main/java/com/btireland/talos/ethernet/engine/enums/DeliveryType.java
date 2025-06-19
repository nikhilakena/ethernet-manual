package com.btireland.talos.ethernet.engine.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum DeliveryType {
    ON_NET("BT On-Net", "ON-NET"),
    OFF_NET("3rd Party Supplier", "OFF-NET");

    private static Map<String, String> deliveryTypeMap;
    @Getter
    private final String value;
    @Getter
    private final String prompt;

    DeliveryType(String prompt, String value) {
        this.prompt = prompt;
        this.value = value;
    }

    private static void initializeMapping() {
        deliveryTypeMap = new HashMap<>();
        for (DeliveryType deliveryType : DeliveryType.values()) {
            deliveryTypeMap.put(deliveryType.getValue(), deliveryType.getPrompt());
        }
    }

    public static String getDisplayNameByCode(String deliveryType) {
        if (deliveryTypeMap == null) {
            initializeMapping();
        }
        return deliveryTypeMap.get(deliveryType);

    }
}
