package com.btireland.talos.ethernet.engine.client.seal;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.client.asset.seal.SealClient;
import com.btireland.talos.ethernet.engine.util.EmailDTOFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@IntegrationTest
@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SealClientTest")
class SealClientTest {

    @Autowired
    private SealClient client;

    @Pact(provider = "Seal", consumer = "ethernet-engine")
    public RequestResponsePact createPactForSeal(PactDslWithProvider builder) throws JsonProcessingException {
        RequestResponsePact requestResponsePact = builder
                .uponReceiving("post email data to seal")
                .path("/api/v1/pbtdc-order")
                .body(EmailDTOFactory.emailDTOJson())
                .method("POST")
                .willRespondWith()
                .status(200)
                .body("success", MediaType.TEXT_PLAIN_VALUE)
                .toPact();

        return requestResponsePact;

    }

    @PactTestFor(pactMethod = "createPactForSeal", port = "9980")
    @Test
    @DisplayName("call Seal endpoint, check that the json is returned correctly")
    void testSealForPBTDCOrder() {
        String actual = null;
        try {
            actual = client.createOrder(EmailDTOFactory.defaultEmailDTO());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        // Check that the object is what we expect, so json is read correctly
        Assertions.assertThat(actual).isEqualTo("success");
    }

}
