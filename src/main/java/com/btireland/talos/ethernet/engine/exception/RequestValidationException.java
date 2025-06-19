package com.btireland.talos.ethernet.engine.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 * Used there is an validation error like order number
 */
@ToString
@EqualsAndHashCode(callSuper = true)
@SoapFault(faultCode = FaultCode.CLIENT)
public class RequestValidationException extends Exception {

    public RequestValidationException(String message) {
        super(message);
    }

    public RequestValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
