package com.btireland.talos.ethernet.engine.soap;


import static com.btireland.talos.ethernet.engine.soap.SoapConstants.NAMESPACE_URI;
import static com.btireland.talos.quote.facade.base.constant.FeatureFlagConstants.QUOTE_FACADE_ENABLED;

import com.btireland.talos.ethernet.engine.exception.AuthenticationException;
import com.btireland.talos.ethernet.engine.soap.auth.AuthenticationHelper;
import com.btireland.talos.ethernet.engine.soap.orders.ObjectFactory;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrder;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrderResponse;
import com.btireland.talos.quote.facade.service.api.QuoteAPI;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Slf4j
@Endpoint
@Component
@Namespace(prefix = "v1", uri = "http://wag.btireland.ie/WAG_WS/")
public class QBTDCOrderEndpoint {

    private AuthenticationHelper authenticationHelper;

    private QuoteAPI quoteAPI;

    private final HttpServletRequest httpServletRequest;


    public QBTDCOrderEndpoint(AuthenticationHelper authenticationHelper,
                              QuoteAPI quoteAPI, HttpServletRequest httpServletRequest) {
        this.authenticationHelper = authenticationHelper;
        this.quoteAPI = quoteAPI;
        this.httpServletRequest = httpServletRequest;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "QBTDC")
    @ResponsePayload
    public JAXBElement<QBTDCOrderResponse> createQBTDCOrder(@RequestPayload QBTDCOrder request) throws AuthenticationException {
        try {
            log.error("in try block");
            String oao = authenticationHelper.getOperator().getOperatorOAO();
            return new ObjectFactory().createQBTDCRESPONSE(quoteAPI.createQuote(request, oao));

        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred while processing QBTDC Order request", e);
            return null;
        }
    }


}
