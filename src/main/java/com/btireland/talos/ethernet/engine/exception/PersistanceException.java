package com.btireland.talos.ethernet.engine.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@ToString
@EqualsAndHashCode(callSuper = true)
@SoapFault(faultCode = FaultCode.SERVER, faultStringOrReason = "WS10: Unexpected Failure.")
public class PersistanceException extends Exception {

    public PersistanceException(String message) {
        super(message);
    }

    public PersistanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
