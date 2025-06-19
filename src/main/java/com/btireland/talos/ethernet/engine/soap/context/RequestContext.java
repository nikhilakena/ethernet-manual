package com.btireland.talos.ethernet.engine.soap.context;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope()
public class RequestContext {
    public String getOriginalXMLRequest() {
        return originalXMLRequest;
    }

    public void setOriginalXMLRequest(String originalXMLRequest) {
        this.originalXMLRequest = originalXMLRequest;
    }

    private String originalXMLRequest;
}

