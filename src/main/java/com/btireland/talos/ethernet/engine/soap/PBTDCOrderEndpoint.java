package com.btireland.talos.ethernet.engine.soap;


import com.btireland.talos.ethernet.engine.exception.AuthenticationException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceNotFoundException;
import com.btireland.talos.ethernet.engine.facade.PBTDCOrderFacade;
import com.btireland.talos.ethernet.engine.soap.auth.AuthenticationHelper;
import com.btireland.talos.ethernet.engine.soap.orders.ObjectFactory;
import com.btireland.talos.ethernet.engine.soap.orders.PBTDCOrder;
import com.btireland.talos.ethernet.engine.soap.orders.PBTDCOrderresponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.JAXBElement;

import static com.btireland.talos.ethernet.engine.soap.SoapConstants.NAMESPACE_URI;


@Slf4j
@Endpoint
@Component
@Namespace(prefix = "v1", uri = "http://wag.btireland.ie/WAG_WS/")
public class PBTDCOrderEndpoint {
    private PBTDCOrderFacade pbtcOrderFacade;
    private AuthenticationHelper authenticationHelper;

    public PBTDCOrderEndpoint (PBTDCOrderFacade pbtcOrderFacade,
                               AuthenticationHelper authenticationHelper){
        this.pbtcOrderFacade = pbtcOrderFacade;
        this.authenticationHelper = authenticationHelper;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "PBTDC")
    @ResponsePayload
    public JAXBElement<PBTDCOrderresponse> createPBTDCOrder(@RequestPayload PBTDCOrder request) throws AuthenticationException {
        log.debug("createPBTDCOrder::IN");
        try {
            //Remove oao hardcode once authentication code implemented
            String oao = authenticationHelper.getOperator().getOperatorOAO();

            return new ObjectFactory().createPBTDCRESPONSE(pbtcOrderFacade.createPBTDCOrder(request, oao));

        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception | OrderManagerServiceBadRequestException | OrderManagerServiceNotFoundException e) {
            log.error("Exception occurred while processing PBTDC Order request", e);
            return null;
        }
    }


}
