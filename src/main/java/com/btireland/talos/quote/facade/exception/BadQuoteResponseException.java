package com.btireland.talos.quote.facade.exception;

public class BadQuoteResponseException extends RuntimeException {

    public BadQuoteResponseException() {
        this("Pricing Exception");
    }

    public BadQuoteResponseException(String message) {
        this(message, null);
    }

    public BadQuoteResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
