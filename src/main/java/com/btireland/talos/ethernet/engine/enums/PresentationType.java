package com.btireland.talos.ethernet.engine.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Collectors;
import java.util.stream.Stream;

//TBD See how SiteType is used in inventory manager
public enum PresentationType {

	LC1310,
	LC850,
	RJ45;

	@JsonCreator
	public static PresentationType fromString(String key) {
	    for(PresentationType type : PresentationType.values()) {
	        if(type.name().equalsIgnoreCase(key)) {
	            return type;
	        }
	    }
        throw new IllegalArgumentException("Invalid SiteType provided : " + key + ". Valid values are : " + Stream.of(PresentationType.values()).map(PresentationType::name).collect(Collectors.toList()));
	}
}
