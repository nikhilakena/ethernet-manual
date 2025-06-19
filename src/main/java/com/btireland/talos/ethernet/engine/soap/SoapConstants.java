package com.btireland.talos.ethernet.engine.soap;

import javax.xml.namespace.QName;

public class SoapConstants {
    public static final String NAMESPACE_URI = "http://wag.btireland.ie/WAG_WS/";
    public static final String NAMESPACE_PAYLOAD_URI = "http://wag.btireland.ie/WAG_WS/";
    public static final String TALOS_NAMESPACE_URI = "http://talos.btireland.com/ws/schemas/v1";
    public static final QName FAULT_DETAIL_MAIN_ELEMENT_NAME = new QName(TALOS_NAMESPACE_URI, "CODE", "talos-ws");
    public static final QName FAULT_DETAIL_ELEMENT_NAME = new QName(TALOS_NAMESPACE_URI, "MESSAGE", "talos-ws");

    public static final String FAULT_VALIDATION_ERROR_CODE = "400";
    public static final String FAULT_VALIDATION_ERROR_MESSAGE = "Client request invalid";
    public static final String DEFAULT_FAULT_VALIDATION_ERROR_MESSAGE = "Validation Failed";
    public static final String WS3_FAULT_VALIDATION_ERROR_MESSAGE = "WSS3 Validationf Failed";

    public static final String QUOTE_FACADE_HEADER = "X-QUOTE-FACADE-ENABLED";

    private SoapConstants(){

    }
}
