package com.btireland.talos.quote.facade.base.enumeration.internal;

public enum NotificationType {

  COMPLETE("C"),
  REJECT("R"),
  DELAY("D"),
  WSA("WSA");

  private final String value;

  NotificationType(final String newValue) {
    value = newValue;
  }

  public String getValue() {
    return value;
  }

}
