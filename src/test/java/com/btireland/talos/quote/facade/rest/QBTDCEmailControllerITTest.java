package com.btireland.talos.quote.facade.rest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import com.btireland.talos.quote.facade.service.api.QuoteEmailAPI;
import com.btireland.talos.quote.facade.util.TestFactory;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import io.restassured.RestAssured;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@IntegrationTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
public class QBTDCEmailControllerITTest {
    private static final String TEST_DATA_DIR = "/data/QBTDCEmailControllerITTest/";
    private static final String BASE_URL = "/api/v1/dc/email/qbtdc";

    private static final GreenMail mailServer = new GreenMail(ServerSetupTest.SMTP);

    @LocalServerPort
    private int port;

    @Autowired
    private QuoteEmailAPI quoteEmailAPI;

    private static final WireMockServer wireMock = new WireMockServer(9980);

    @BeforeAll
    private static void startServer() {
        mailServer.start();
        wireMock.start();
    }

    @AfterAll
    private static void stopServer() {
        mailServer.stop();
        wireMock.stop();
    }

    @Test
    @DataSet(value = TEST_DATA_DIR + "qbtdc-order.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY",
            "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
            , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @DisplayName("Test qbtdc email generation returns a 202")
    void testEmailGeneration_whenRequestIsReceived_Returns202Status() throws Exception {
        ReflectionTestUtils.setField(quoteEmailAPI, "quoteFacadeEnabled", false);
        RestAssured.given()
                .port(this.port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(IOUtils.toString(new ClassPathResource(TEST_DATA_DIR + "EmailRequest.json",
                                                             this.getClass()).getInputStream(),
                                       StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL + "/send")
                .then()
                .assertThat()
                .statusCode(HttpStatus.ACCEPTED_202);
        Assertions.assertThat(mailServer.getReceivedMessages())
                .isNotEmpty();
    }

    @Test
    @DataSet(value = TEST_DATA_DIR + "qbtdc-order.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY",
        "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
        "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
        , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
        "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
        "flyway_schema_history"
    })
    @DisplayName("Test qbtdc email generation using quote facade returns a 202")
    void testEmailGenerationUsingQuoteFacade_whenRequestIsReceived_Returns202Status() throws Exception {
        wireMock.stubFor(get(urlPathEqualTo("/api/v1/qbtdc/quote/mail/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(QBTDCOrderDTOFactory.asJson(TestFactory.getQuoteGroupEmailResponse()))));

        ReflectionTestUtils.setField(quoteEmailAPI, "quoteFacadeEnabled", true);
        RestAssured.given()
            .port(port)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(IOUtils.toString(new ClassPathResource(TEST_DATA_DIR + "EmailRequest.json",
                    this.getClass()).getInputStream(),
                StandardCharsets.UTF_8))
            .when()
            .post(BASE_URL + "/send")
            .then()
            .assertThat()
            .statusCode(HttpStatus.ACCEPTED_202);
        Assertions.assertThat(mailServer.getReceivedMessages())
            .isNotEmpty();
    }

}
