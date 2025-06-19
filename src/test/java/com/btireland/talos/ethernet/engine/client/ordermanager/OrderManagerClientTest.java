package com.btireland.talos.ethernet.engine.client.ordermanager;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.OrderManagerClient;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCOrderDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QBTDCOrderDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QuoteDetailsDTO;
import com.btireland.talos.ethernet.engine.exception.BadRequestException;
import com.btireland.talos.ethernet.engine.util.PBTDCOrderDTOFactory;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import com.btireland.talos.quote.facade.dto.ordermanager.QuoteOrder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@IntegrationTest
@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("OrderManagerClientTest")
class OrderManagerClientTest {


    @Autowired
    private OrderManagerClient client;

    @Pact(provider = "OrderManager", consumer = "ethernet-engine")
    public RequestResponsePact createPactForQuoteAPI(PactDslWithProvider builder){
        RequestResponsePact requestResponsePact=builder
                .uponReceiving("get quote details")
                .path("/api/v1/wag/quote/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(QBTDCOrderDTOFactory.defaultQBTDCResponsePactFormat())
                .toPact();

        return requestResponsePact;

    }

    @Pact(provider = "OrderManager", consumer = "ethernet-engine")
    public RequestResponsePact createPactForOrderPersistanceAPI(PactDslWithProvider builder) throws Exception {
        RequestResponsePact requestResponsePact=builder
                .uponReceiving("post pbtdc order details")
                .path("/api/v1/wag/pbtdc-orders")
                .method("POST")
                .body(PBTDCOrderDTOFactory.defaultPBTDCOrderRequestAsJson())
                .willRespondWith()
                .status(201)
                .body(PBTDCOrderDTOFactory.defaultPBTDCOrderDTOResponsePactFormat())
                .toPact();

        return requestResponsePact;

    }

    @Pact(provider = "OrderManager", consumer = "ethernet-engine")
    public RequestResponsePact createPactForQBTDCOrderPersistanceAPI(PactDslWithProvider builder) throws Exception {
        RequestResponsePact requestResponsePact=builder
                .uponReceiving("post qbtdc order details")
                .path("/api/v1/wag/qbtdc-orders")
                .method("POST")
                .body(QBTDCOrderDTOFactory.defaultQBTDCOrderRequestAsJson())
                .willRespondWith()
                .status(201)
                .body(QBTDCOrderDTOFactory.defaultQBTDCOrderDTOResponsePactFormat())
                .toPact();

        return requestResponsePact;

    }

    @PactTestFor(pactMethod = "createPactForQuoteAPI", port = "9980")
    @Test
    @DisplayName("call OrderManager Quote endpoint, check that the QBTDC json is returned correctly")
    void testOrderManagerForQuotePact(){
        QuoteDetailsDTO expected = QBTDCOrderDTOFactory.defaultQuoteDetailsDTO();
        QuoteDetailsDTO actual=client.fetchQuote(1L);
        // Check that the object is what we expect, so json is read correctly
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @PactTestFor(pactMethod = "createPactForOrderPersistanceAPI", port = "9980")
    @Test
    @DisplayName("call OrderManager Order endpoint, check that the PBTDCOrderDTO json is returned correctly")
    void testOrderManagerForPBTDCOrderPact() throws BadRequestException, IOException {
        PBTDCOrderDTO expected = PBTDCOrderDTOFactory.savedPBTDCResponse();
        PBTDCOrderDTO actual=client.createPBTDCOrder(PBTDCOrderDTOFactory.defaultPBTDCOrderDTOWithOriginalRequest());
        // Check that the object is what we expect, so json is read correctly
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringActualNullFields().isEqualTo(expected);
    }

    @PactTestFor(pactMethod = "createPactForQBTDCOrderPersistanceAPI", port = "9980")
    @Test
    @DisplayName("call OrderManager Order endpoint, check that the QBTDCOrderDTO json is returned correctly")
    void testOrderManagerForQBTDCOrderPact() throws BadRequestException, IOException {
        QuoteOrder expected = QBTDCOrderDTOFactory.savedQBTDCResponse();
        QuoteOrder actual=client.createQBTDCOrder(QBTDCOrderDTOFactory.defaultQBTDCOrderDTOWithOriginalRequest());
        // Check that the object is what we expect, so json is read correctly
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringActualNullFields().isEqualTo(expected);
    }
}
