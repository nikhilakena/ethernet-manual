package com.btireland.talos.ethernet.engine.soap;


import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.util.NotificationFactory;
import com.btireland.talos.ethernet.engine.util.PBTDCOrderDTOFactory;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.tngtech.keycloakmock.api.TokenConfig;
import com.tngtech.keycloakmock.junit5.KeycloakMockExtension;
import io.restassured.RestAssured;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


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
class PBTDCOrderEndpointTest {

    private static final String BASE_URL = "/ws";
    private static final String SOAP_ACTION_HEADER = "SOAPAction";
    private static final String SOAP_ACTION_ACCEPT = "http://wag.btireland.ie/WAG_WS/";
    private static final String XML_CONTENT_TYPE = "text/xml";

    @LocalServerPort
    private int port;

    @RegisterExtension
    public static final KeycloakMockExtension keyCloakMock = new KeycloakMockExtension();

    private static final WireMockServer wireMock = new WireMockServer(9980);

    @BeforeAll
    private static void beforeAll() {
        wireMock.start();
    }

    @AfterAll
    private static void afterAll() {
        wireMock.stop();
    }

    @Test
    @DisplayName("Send a valid PBTDC order to the SOAP web service")
    void testCreatePFIBOrder() throws IOException {
        wireMock.stubFor(post(urlPathEqualTo("/api/v1/wag/pbtdc-orders"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(PBTDCOrderDTOFactory.asJson(PBTDCOrderDTOFactory.savedPBTDCResponse()))));

        wireMock.stubFor(post(urlPathEqualTo("/api/v1/notification"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(NotificationFactory.asJson(NotificationFactory.WSANotificationResponse()))));

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
                .body(IOUtils.toString(new ClassPathResource("PBTDC-Order.xml", this.getClass()).getInputStream(), StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .body("Envelope.Body.PBTDCRESPONSE.VERSION", Matchers.equalTo("1.0"));

    }

    @Test
    @DisplayName("Send an invalid PFIB order to the SOAP web service")
    void testCreateInvalidPBTDCOrder() throws IOException {
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
                .body(IOUtils.toString(new ClassPathResource("PBTDC-InvalidOrder.xml", this.getClass()).getInputStream(), StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(500)
                .body("Envelope.Body.Fault.faultstring", Matchers.equalTo("Client request invalid"));
    }

    @Test
    @DisplayName("Send an invalid Pbtdc order with invalid OBO to the SOAP web service")
    void testCreateInvalidOboPBTDCOrder() throws IOException {
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
                .body(IOUtils.toString(new ClassPathResource("PBTDC-InvalidOBO.xml", this.getClass()).getInputStream(), StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(500)
                .body("Envelope.Body.Fault.faultstring", Matchers.equalTo("Client request invalid"));
    }

    @Test
    @DisplayName("Send a valid PBTDC order with missing OBO to the SOAP web service")
    void testCreatePFIBOrderMissingOBO() throws IOException {
        wireMock.stubFor(post(urlPathEqualTo("/api/v1/wag/pbtdc-orders"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(PBTDCOrderDTOFactory.asJson(PBTDCOrderDTOFactory.savedPBTDCResponse()))));

        wireMock.stubFor(post(urlPathEqualTo("/api/v1/notification"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(NotificationFactory.asJson(NotificationFactory.WSANotificationResponse()))));

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
                .body(IOUtils.toString(new ClassPathResource("PBTDC-MissingOBO.xml", this.getClass()).getInputStream(), StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .body("Envelope.Body.PBTDCRESPONSE.VERSION", Matchers.equalTo("1.0"));


    }


}


