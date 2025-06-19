package com.btireland.talos.ethernet.engine.exception.ordermanager;

public class OrderManagerServiceNotFoundException extends OrderManagerServiceException {

    public OrderManagerServiceNotFoundException(String message) {
        super(message);
    }

    public OrderManagerServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
