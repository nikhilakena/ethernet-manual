package com.btireland.talos.ethernet.engine.exception;

/**
 * When notification processing fails and requires manual intervention
 */
public class PBTDCOrderClientException extends Exception implements NoRetryInBpmn {

    public PBTDCOrderClientException(String message) {
        super(message);
    }

    public PBTDCOrderClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
