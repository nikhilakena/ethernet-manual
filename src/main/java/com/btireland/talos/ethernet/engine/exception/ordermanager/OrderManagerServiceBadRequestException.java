package com.btireland.talos.ethernet.engine.exception.ordermanager;

public class OrderManagerServiceBadRequestException extends OrderManagerServiceException {

    public OrderManagerServiceBadRequestException(String message) {
        super(message);
    }

    public OrderManagerServiceBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
