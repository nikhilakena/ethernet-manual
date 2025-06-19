package com.btireland.talos.ethernet.engine.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 * Exception while generating a Jasper report
 */
@ToString
@EqualsAndHashCode(callSuper = true)
@SoapFault(faultCode = FaultCode.CLIENT)
public class ReportGenerationException extends Exception {
    public ReportGenerationException(String message) {
        super(message);
    }
    public ReportGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
