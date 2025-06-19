package com.btireland.talos.ethernet.engine.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public enum ActionFlag {

    P("New Access"),
    CH("Existing Access"),
    L("");

    @Getter
    private final String value;

    @JsonCreator
    public static ActionFlag fromString(String key) {
        for(ActionFlag type : ActionFlag.values()) {
            if(type.name().equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid SiteType provided : " + key + ". Valid values are : " + Stream.of(ActionFlag.values()).map(ActionFlag::name).collect(Collectors.toList()));
    }
}
