package com.btireland.talos.ethernet.engine.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Collectors;
import java.util.stream.Stream;

//TBD See how SiteType is used in inventory manager
public enum PowerSocketType {

    THREEPIN("3-PIN"),

    KETTLE("KETTLE");

    public String getSocketTypeName() {
        return socketTypeName;
    }

    private String socketTypeName;

    PowerSocketType(String socketTypeName) {
        this.socketTypeName = socketTypeName;
    }

    @JsonCreator
    public static PowerSocketType fromString(String key) {
        for(PowerSocketType type : PowerSocketType.values()) {
            if(type.socketTypeName.equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid SiteType provided : " + key + ". Valid values are : " + Stream.of(PowerSocketType.values()).map(PowerSocketType::name).collect(Collectors.toList()));
    }
}
