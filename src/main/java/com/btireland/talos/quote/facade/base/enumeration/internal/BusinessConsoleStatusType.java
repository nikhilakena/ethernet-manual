package com.btireland.talos.quote.facade.base.enumeration.internal;

public enum BusinessConsoleStatusType {

  CLAIMED("Claimed"),
  DELAY("Delayed"),
  //It will be in a WSA status in OrderMapStatus when Business Console status value is created
  WSA("Created");

  private final String value;

  BusinessConsoleStatusType(final String newValue) {
    value = newValue;
  }

  public String getValue() {
    return value;
  }

}
