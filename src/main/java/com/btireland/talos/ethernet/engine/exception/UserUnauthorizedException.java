package com.btireland.talos.ethernet.engine.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class UserUnauthorizedException extends Exception{

    public UserUnauthorizedException(String message) {
        super(message);
    }

    public UserUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
