package com.btireland.talos.ethernet.engine.soap;


import static com.btireland.talos.ethernet.engine.soap.SoapConstants.QUOTE_FACADE_HEADER;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QBTDCOrderDTO;
import com.btireland.talos.ethernet.engine.config.DatabaseConfiguration;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.util.NotificationFactory;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import com.btireland.talos.quote.facade.util.TestFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.tngtech.keycloakmock.api.TokenConfig;
import com.tngtech.keycloakmock.junit5.KeycloakMockExtension;
import io.restassured.RestAssured;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;


/**
 * Test the controller layer only. @WebMvcTest will start a minimal spring context with Web related stuff. Other things like regular services or databases component
 * are not started. So, you need to mock your controller dependencies. That allows us to test this layer in isolation.
 * ExampleOrderFacade is mocked and defined in TestRestConfiguration.class
 * <p>
 * We use rest-assured to make the test easy to write.
 */

@ActiveProfiles("test")
@IntegrationTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatabaseConfiguration.class, DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
class QBTDCOrderEndpointTest {

    private static final String BASE_URL = "/ws";
    private static final String SOAP_ACTION_HEADER = "SOAPAction";
    private static final String SOAP_ACTION_ACCEPT = "http://wag.btireland.ie/WAG_WS/";
    private static final String XML_CONTENT_TYPE = "text/xml";
    private static final String TEST_DATA_DIR = "/data/QBTDCOrderEndpointTest/";

    @RegisterExtension
    static KeycloakMockExtension keyCloakMock = new KeycloakMockExtension();

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private QBTDCOrderEndpoint qbtdcOrderEndpoint;

    @LocalServerPort
    private int port;

    private static WireMockServer wireMock = new WireMockServer(9980);

    @Autowired
    private EmbeddedActiveMQResource mqServer;

    @BeforeAll
    private static void beforeAll() {
        wireMock.start();
    }

    @AfterAll
    private static void afterAll() {
        wireMock.stop();
    }

    @Test
    @DisplayName("Send an invalid QBTDC order to the SOAP web service")
    void testCreateInvalidQBTDCOrder() throws IOException {
        String token = keyCloakMock.getAccessToken(
                TokenConfig.aTokenConfig()
                        .withClaim("operatorCode", "83758")
                        .withClaim("operatorName", "SKY")
                        .withClaim("operatorOao", "sky")
                        .build());
        RestAssured.given().auth().oauth2(token)
                .port(port)
                .contentType(XML_CONTENT_TYPE)
                .header(SOAP_ACTION_HEADER, SOAP_ACTION_ACCEPT)
                .body(IOUtils.toString(new ClassPathResource("/data/QBTDCOrderEndpointTest/QBTDC-InvalidOrder.xml", this.getClass()).getInputStream(), StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(500)
                .body("Envelope.Body.Fault.faultstring", Matchers.equalTo("Client request invalid"));
    }

    @Test
    @DisplayName("Send an invalid QBTDC order With No Handover tag to the SOAP web service")
    void testCreateInvalidQBTDCOrderWithNoHandoverTag() throws IOException {
        String token = keyCloakMock.getAccessToken(
                TokenConfig.aTokenConfig()
                        .withClaim("operatorCode", "83758")
                        .withClaim("operatorName", "SKY")
                        .withClaim("operatorOao", "sky")
                        .build());
        RestAssured.given().auth().oauth2(token)
                .port(port)
                .contentType(XML_CONTENT_TYPE)
                .header(SOAP_ACTION_HEADER, SOAP_ACTION_ACCEPT)
                .body(IOUtils.toString(new ClassPathResource("/data/QBTDCOrderEndpointTest/QBTDC-InvalidOrderNoHandOver.xml", this.getClass()).getInputStream(), StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(500)
                .body("Envelope.Body.Fault.faultstring", Matchers.equalTo("Client request invalid"));
    }



    @Test
    @DisplayName("Send a valid QBTDC order to the SOAP web service")
    void testCreateQBTDCOrder() throws IOException {
        wireMock.stubFor(post(urlPathEqualTo("/api/v1/wag/qbtdc-orders"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(QBTDCOrderDTOFactory.asJson(QBTDCOrderDTOFactory.savedQBTDCResponse()))));
        wireMock.stubFor(post(urlPathEqualTo("/api/v1/notification"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(NotificationFactory.asJson(Notifications.builder().transactionId(1L).type("C").createdAt(LocalDateTime.now()).build()))));

        String token = keyCloakMock.getAccessToken(
                TokenConfig.aTokenConfig()
                        .withClaim("operatorCode", "83758")
                        .withClaim("operatorName", "SKY")
                        .withClaim("operatorOao", "sky")
                        .build());
        RestAssured.given().auth().oauth2(token)
                .port(port)
                .contentType(XML_CONTENT_TYPE)
                .header(SOAP_ACTION_HEADER, SOAP_ACTION_ACCEPT)
                .body(IOUtils.toString(new ClassPathResource("/data/QBTDCOrderEndpointTest/QBTDC-ValidOrder.xml", this.getClass()).getInputStream(), StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(200);

    }

    @Test
    @DisplayName("Send an invalid QBTDC order With No IP tag to the SOAP web service")
    void testCreateInvalidQBTDCOrderWithNoIPTag() throws IOException {
        String token = keyCloakMock.getAccessToken(
                TokenConfig.aTokenConfig()
                        .withClaim("operatorCode", "83758")
                        .withClaim("operatorName", "SKY")
                        .withClaim("operatorOao", "sky")
                        
                        .build());
        RestAssured.given().auth().oauth2(token)
                .port(port)
                .contentType(XML_CONTENT_TYPE)
                .header(SOAP_ACTION_HEADER, SOAP_ACTION_ACCEPT)

                .body(IOUtils.toString(new ClassPathResource("/data/QBTDCOrderEndpointTest/QBTDC-InvalidOrderNoHandOver.xml", this.getClass()).getInputStream(), StandardCharsets.UTF_8))
                .when()

                .post(BASE_URL)
        .then()
        .assertThat()
        .statusCode(500);
    }



    @Test
    @DisplayName("Send an invalid QBTDC order with Wrong Email to the SOAP web service")
    void testCreateInvalidQBTDCOrderWithWrongEmail() throws IOException {
        String token = keyCloakMock.getAccessToken(
                TokenConfig.aTokenConfig()
                        .withClaim("operatorCode", "83758")
                        .withClaim("operatorName", "SKY")
                        .withClaim("operatorOao", "sky")
                        .build());
        RestAssured.given().auth().oauth2(token)
                .port(port)
                .contentType(XML_CONTENT_TYPE)
                .header(SOAP_ACTION_HEADER, SOAP_ACTION_ACCEPT)
                .body(IOUtils.toString(new ClassPathResource("/data/QBTDCOrderEndpointTest/QBTDC" +
                        "-InvalidOrderWrongEmail.xml", this.getClass()).getInputStream(), StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(500)
                .body("Envelope.Body.Fault.faultstring", Matchers.equalTo("Client request invalid"));
    }

    @Test
    @DisplayName("Send a valid QBTDC order to the Quote Facade with a header")
    @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG",
            "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
            , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "QuoteFacade-CreateQBTDCOrder-result-ds.yml")

    void testCreateQBTDCOrderUsingQuoteFacade() throws IOException {
        wireMock.stubFor(post(urlPathEqualTo("/api/v1/wag/qbtdc-orders"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(QBTDCOrderDTOFactory.asJson(TestFactory.getOrderManagerResponse()))));

        wireMock.stubFor(post(urlPathEqualTo("/api/v1/qbtdc/quote"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(IOUtils.toString(
                    new ClassPathResource("/data/QuoteFacade/QuoteProcessorIntegrationTest/QuoteCompleteResponse.json",
                        this.getClass()).getInputStream(),
                    StandardCharsets.UTF_8))));

        wireMock.stubFor(post(urlPathEqualTo("/api/v1/notification"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(NotificationFactory.asJson(
                    Notifications.builder().transactionId(684346L).type("C")
                        .createdAt(LocalDateTime.parse("2023-03-15T10:33:46")).build()))));

        String token = keyCloakMock.getAccessToken(
                TokenConfig.aTokenConfig()
                        .withClaim("operatorCode", "83758")
                        .withClaim("operatorName", "SKY")
                        .withClaim("operatorOao", "sky")
                        .build());

        RestAssured.given().auth().oauth2(token)
                .port(port)
                .contentType(XML_CONTENT_TYPE)
                .header(SOAP_ACTION_HEADER, SOAP_ACTION_ACCEPT)
                .header(QUOTE_FACADE_HEADER, "TRUE")
                .body(IOUtils.toString(new ClassPathResource("/data/QBTDCOrderEndpointTest/QuoteFacade-QBTDC" +
                        "-ValidOrder.xml", this.getClass()).getInputStream(), StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(200);
    }

}


