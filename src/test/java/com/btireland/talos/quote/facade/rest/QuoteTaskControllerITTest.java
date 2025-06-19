package com.btireland.talos.quote.facade.rest;

import static com.btireland.talos.quote.facade.base.constant.FeatureFlagConstants.QUOTE_FACADE_ENABLED;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.taskService;
import static org.springframework.hateoas.MediaTypes.HAL_FORMS_JSON_VALUE;
import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.exception.PersistanceException;
import com.btireland.talos.ethernet.engine.soap.orders.ObjectFactory;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrder;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrderResponse;
import com.btireland.talos.ethernet.engine.util.NotificationFactory;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderFactory;
import com.btireland.talos.quote.facade.base.constant.RestMapping;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemTask;
import com.btireland.talos.quote.facade.process.processor.QuoteProcessor;
import com.btireland.talos.quote.facade.util.TestFactory;
import com.btireland.talos.quote.facade.workflow.QuoteProcessConstants;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


@IntegrationTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {QUOTE_FACADE_ENABLED + "=true"})
@ActiveProfiles("test")
@Import({DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
public class QuoteTaskControllerITTest {

    public static final String TEST_DATA_DIR = "/data/QuoteFacade/QuoteTaskControllerIntegrationTest/";

    @LocalServerPort
    private int port;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private QuoteProcessor quoteProcessor;

    private static final WireMockServer wireMock = new WireMockServer(9980);

    @BeforeAll
    private static void beforeAll() {
        wireMock.start();
    }

    @BeforeEach
    private void startWireMock() {
        wireMock.resetAll();
    }
    @AfterAll
    private static void afterAll() {
        wireMock.stop();
    }


    @Test
    @DataSet(cleanBefore = true, executeScriptsBefore = {"/data/CucumberTest/reset_sequences.sql"},
        skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO",
            "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF",
            "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
        })
    @DisplayName("Change assginee return 204")
    @ExpectedDataSet(value = "/data/QuoteFacade/QuoteTaskControllerIntegrationTest/ChangeAssignee-result-ds.yml")
    void testChangeAssignee_whenChangeeAssigneeREquestRecevied_returns204Status()
        throws IOException, JAXBException, SOAPException, PersistanceException {

        //GIVEN
        placeQuoteOrder();
        Task task = taskService().createTaskQuery().singleResult();
        QuoteItemTask quoteItemTask = QuoteItemTask.QuoteItemTaskBuilder.newQuoteItemTaskBuilder()
            .withTaskId(task.getId())
            .withAssignee("TestUser").build();

        //WHEN
        RestAssured.given()
            .port(this.port)
            .contentType(HAL_FORMS_JSON_VALUE)
            .body(quoteItemTask)
            .when()
            .put(RestMapping.QUOTE_TASK_BASE_PATH + RestMapping.QUOTE_TASK_CHANGE_ASSIGNEE_PATH, task.getId())
            .then()
            .assertThat()
            .statusCode(204);

        //THEN
        //ASSERTION is done on the dataset
    }

    @Test
    @DataSet(cleanBefore = true, executeScriptsBefore = {"/data/CucumberTest/reset_sequences.sql"},
            skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP",
                    "ACT_ID_INFO",
                    "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
                    "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF",
                    "ACT_RE_DECISION_REQ_DEF",
                    "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
                    "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
                    "flyway_schema_history"
            })
    @DisplayName("Get Quote details returns successfully")
    void testGetQuoteDetails_whenWorkflowAtUserTask_returnsQuoteDetails()
            throws IOException, JAXBException, SOAPException, PersistanceException {

        //GIVEN
        wireMock.stubFor(get(urlPathEqualTo("/api/v1/qbtdc/quote/1"))
                                 .willReturn(aResponse()
                                                     .withHeader("Content-Type", "application/json")
                                                     .withBody(getQuoteResponse())));

        placeQuoteOrder();
        Task task = taskService().createTaskQuery().singleResult();
        String expectedResponse = IOUtils.toString(new ClassPathResource(TEST_DATA_DIR + "QuoteDetailsHALResponse.json",
                                                                         this.getClass()).getInputStream(),
                                                   StandardCharsets.UTF_8).replace("{taskId}", task.getId()).replace(
                "{port}", String.valueOf(this.port));


        //WHEN
        String response = RestAssured.given()
                .port(this.port)
                .contentType(HAL_FORMS_JSON_VALUE)
                .when()
                .get(RestMapping.QUOTE_TASK_BASE_PATH + RestMapping.OFFLINE_PRICING_GET_QUOTE_PATH,
                     task.getId())
                .then()
                .assertThat()
                .statusCode(200).extract().body().asPrettyString();

        //THEN
        Assertions.assertThat(response).isEqualToIgnoringWhitespace(expectedResponse);

    }

    private String getQuoteResponse() throws IOException {
        return IOUtils.toString(
                new ClassPathResource(TEST_DATA_DIR + "GetQuoteResponse" +
                                              ".json",
                                      this.getClass()).getInputStream(),
                StandardCharsets.UTF_8);
    }

    private void placeQuoteOrder() throws IOException, JAXBException, SOAPException, PersistanceException {
        wireMock.stubFor(post(urlPathEqualTo("/api/v1/wag/qbtdc-orders"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(QBTDCOrderDTOFactory.asJson(TestFactory.getOrderManagerResponse()))));

        wireMock.stubFor(post(urlPathEqualTo("/api/v1/qbtdc/quote"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(IOUtils.toString(
                    new ClassPathResource("/data/QuoteFacade/QuoteTaskControllerIntegrationTest/QuoteWSAResponse.json",
                        this.getClass()).getInputStream(),
                    StandardCharsets.UTF_8))));

        wireMock.stubFor(post(urlPathEqualTo("/api/v1/notification"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(NotificationFactory.asJson(
                    Notifications.builder().transactionId(684346L).type("WSA")
                        .createdAt(LocalDateTime.parse("2023-03-15T10:33:46")).build()))));


        //WHEN
        QBTDCOrderResponse actual = quoteProcessor.createQuote(getQbtdcOrderRequest(), "SKY");

        ProcessInstance process = runtimeService.createProcessInstanceQuery().processDefinitionKey(
            QuoteProcessConstants.PROC_DEF_KEY).processInstanceBusinessKey("1").singleResult();
        Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> assertThat(process).isWaitingAt(QuoteProcessConstants.ACT_ID_GENERATE_QUOTE_PROCESS));
    }


    private QBTDCOrder getQbtdcOrderRequest() throws SOAPException, JAXBException, IOException {
        SOAPMessage message = MessageFactory.newInstance().createMessage(null,
            new ClassPathResource("/data/QuoteFacade/QuoteTaskControllerIntegrationTest/QuoteValidAsyncOrderRequest.xml",
                QBTDCOrderFactory.class).getInputStream());
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        return ((JAXBElement<QBTDCOrder>) jaxbContext.createUnmarshaller()
            .unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
    }


}
