package com.btireland.talos.ethernet.engine.exception;

/**
 * When something is wrong in Notcom notification client invocation and requires manual intervention
 */
public class NotificationClientException extends Exception {

    public NotificationClientException(String message) {
        super(message);
    }

    public NotificationClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
