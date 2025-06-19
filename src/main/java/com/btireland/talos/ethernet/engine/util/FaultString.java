package com.btireland.talos.ethernet.engine.util;

public enum FaultString {

    WS01("SOAP Version mismatch. Expected SOAP Namespace to be 'http://schemas.xmlsoap.org/soap/envelope/"),
    WS02("Unexpected 'MustUnderstand' imperative found in SOAP Header."),
    WS03("Invalid Request."),
    WS05("Authentication Failed."),
    WS06("Inappropriate Request."),
    WS07("Request Timed Out."),
    WS08("Policy Breach."),
    WS09("Process Error."),
    WS10("Unexpected Failure."),
    WS11("Service Temporarily Unavailable.");


    private final String faultString;

    FaultString(String faultString) {
        this.faultString = faultString;
    }

    public String getFaultString() {
        return faultString;
    }
}
