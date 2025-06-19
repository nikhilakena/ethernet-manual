package com.btireland.talos.quote.facade.process.processor;

import static com.btireland.talos.quote.facade.process.processor.OfflinePricingProcessor.CREATED_AT;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.taskService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.btireland.talos.core.common.rest.exception.checked.TalosBadRequestException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.exception.PersistanceException;
import com.btireland.talos.ethernet.engine.soap.orders.ObjectFactory;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrder;
import com.btireland.talos.ethernet.engine.util.NotificationFactory;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderFactory;
import com.btireland.talos.quote.facade.base.constant.RestMapping;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.btireland.talos.quote.facade.domain.dao.QuoteItemMapRepository;
import com.btireland.talos.quote.facade.domain.dao.QuoteOrderMapRepository;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemAcceptTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemNoBidTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemRejectTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteNotificationRequest;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteOrderResponse;
import com.btireland.talos.quote.facade.util.TestFactory;
import com.btireland.talos.quote.facade.workflow.QuoteProcessConstants;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
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
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;


@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
@Import({DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
class OfflinePricingProcessorIntegrationTest {

    public static final String TEST_DATA_DIR = "/data/QuoteFacade/OfflinePricingProcessorIntegrationTest/";
    private static final WireMockServer wireMock = new WireMockServer(9980);
    @Autowired
    private OfflinePricingProcessor offlinePricingProcessor;
    @Autowired
    private QuoteProcessor quoteProcessor;
    @Autowired
    private QuoteOrderMapRepository quoteOrderMapRepository;

    @Autowired
    private QuoteItemMapRepository quoteItemMapRepository;

    @Autowired
    private RuntimeService runtimeService;

    @BeforeAll
    public static void beforeAll() {
        wireMock.start();
    }

    @AfterAll
    public static void stopWiremock() {
        wireMock.stop();
    }

    @BeforeEach
    public void startWireMock() {
        wireMock.resetAll();
    }

  @Test
  @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
      "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
      "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
      "flyway_schema_history"
  })
  void testAcceptQuote_whenAcceptQuoteRequestReceived_thenCompleteTask() throws Exception {

    //GIVEN
    placeQuoteOrder();

    Task task = taskService().createTaskQuery().singleResult();
    String quoteId = runtimeService.getVariables(task.getExecutionId()).get(QuoteProcessConstants.VAR_NAME_QUOTE_ID).toString();

    QuoteItemAcceptTask request = new QuoteItemAcceptTask("ON-NET", "ETHERWAY_STANDARD","40", "300", "1050", "1000",
        "This is test notes");

    //WHEN
    offlinePricingProcessor.acceptQuote(request, task.getId());

    //THEN
    //Once the task is completed it will not be returned by the query
    Task updatedTask = taskService().createTaskQuery().taskId(task.getId()).singleResult();
    assertEquals(null, updatedTask);

    QuoteItemMapEntity quoteItemMap = quoteItemMapRepository.findByQuoteId(Long.valueOf(quoteId)).get();
    assertEquals(true, quoteItemMap.isOfflineQuoted());

  }

  @Test
  @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
      "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
      "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
      "flyway_schema_history"
  })
  void testRejectQuote_whenRejectQuoteRequestReceived_thenCompleteTask() throws Exception {

    //GIVEN
    placeQuoteOrder();

    Task task = taskService().createTaskQuery().singleResult();
    String quoteId = runtimeService.getVariables(task.getExecutionId()).get(QuoteProcessConstants.VAR_NAME_QUOTE_ID).toString();

    QuoteItemRejectTask request = new QuoteItemRejectTask("eircode_not_available", "This is test notes");

    //WHEN
    offlinePricingProcessor.rejectQuote(request, task.getId());

    //THEN
    //Once the task is completed it will not be returned by the query
    Task updatedTask = taskService().createTaskQuery().taskId(task.getId()).singleResult();
    assertEquals(null, updatedTask);

    QuoteItemMapEntity quoteItemMap = quoteItemMapRepository.findByQuoteId(Long.valueOf(quoteId)).get();
    assertEquals(true, quoteItemMap.isOfflineQuoted());
  }

  @Test
  @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
      "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
      "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
      "flyway_schema_history"
  })
  void testNoBidQuote_whenNoBidQuoteRequestReceived_thenCompleteTask() throws Exception {

    //GIVEN
    placeQuoteOrder();

    Task task = taskService().createTaskQuery().singleResult();
    String quoteId = runtimeService.getVariables(task.getExecutionId()).get(QuoteProcessConstants.VAR_NAME_QUOTE_ID).toString();

    QuoteItemNoBidTask request = new QuoteItemNoBidTask("This is test notes");

    //WHEN
    offlinePricingProcessor.noBidQuote(request, task.getId());

    //THEN
    //Once the task is completed it will not be returned by the query
    Task updatedTask = taskService().createTaskQuery().taskId(task.getId()).singleResult();
    assertEquals(null, updatedTask);

    QuoteItemMapEntity quoteItemMap = quoteItemMapRepository.findByQuoteId(Long.valueOf(quoteId)).get();
    assertEquals(true, quoteItemMap.isOfflineQuoted());
  }

    @Test
    @DisplayName("Search quotes that match an order number")
    @DataSet(cleanBefore = true, executeScriptsBefore = {TEST_DATA_DIR + "reset_sequences.sql"}, skipCleaningFor
            = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG",
            "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
            , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    void testSearchQuote_whenAsyncFlow_returnsListOfQuotes() throws IOException, TalosNotFoundException,
            PersistanceException, SOAPException, JAXBException {
        //GIVEN
        wireMock.stubFor(get(urlPathEqualTo(RestMapping.SEARCH_QUOTE))
                                 .willReturn(aResponse()
                                                     .withHeader("Content-Type", "application/json")
                                                     .withBody(getSearchQuoteResponse())));
        List<QuoteOrderResponse> expected = getQBTDCOrderDTOResponse();
        placeQuoteOrder();

        //WHEN
        List<QuoteOrderResponse> quoteOrderResponseList = offlinePricingProcessor.searchQuote("BT-QBTDC-1");

        //THEN
        Assertions.assertThat(quoteOrderResponseList).usingRecursiveComparison().ignoringExpectedNullFields().ignoringFields("quoteItemTaskDTOList.taskId", "createdAt").isEqualTo(expected);
    }

    @Test
    @DisplayName("Get all quotes API returns all relevant quotes")
    @DataSet(cleanBefore = true, executeScriptsBefore = {TEST_DATA_DIR + "reset_sequences.sql"}, skipCleaningFor =
            {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG",
                    "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
                    "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF",
                    "ACT_RE_DECISION_REQ_DEF"
                    , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
                    "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
                    "flyway_schema_history"
            })
    void testGetAllQuotes_whenAsyncFlow_returnsPageOfQuotes() throws IOException, TalosNotFoundException,
            PersistanceException, SOAPException, JAXBException {
        //GIVEN
        wireMock.stubFor(get(urlPathEqualTo(RestMapping.SEARCH_QUOTE))
                                 .willReturn(aResponse()
                                                     .withHeader("Content-Type", "application/json")
                                                     .withBody(getSearchQuoteResponse())));
        List<QuoteOrderResponse> expected = getQBTDCOrderDTOResponse();
        placeQuoteOrder();

        //WHEN
        Page<QuoteOrderResponse> qbtdcOrders = offlinePricingProcessor.fetchAllQuotes(PageRequest.of(0, 6,
                                                                                                Sort.by(DESC, CREATED_AT)));

        //THEN
        Assertions.assertThat(qbtdcOrders.getContent()).usingRecursiveComparison().ignoringExpectedNullFields()
                .ignoringFields("quoteItemTaskDTOList.taskId", "createdAt").isEqualTo(expected);
    }


    @Test
    @DisplayName("On receiving delay notification quote is updated")
    @DataSet(cleanBefore = true, executeScriptsBefore = {TEST_DATA_DIR +
            "reset_sequences.sql"}, skipCleaningFor
            = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG",
            "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
            , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    void updateQuote_givenDelayNotification_updatesQuote() throws TalosNotFoundException, PersistanceException,
            JAXBException, SOAPException, IOException, TalosBadRequestException {
        //GIVEN
        wireMock.stubFor(put(urlPathEqualTo("/api/v1/qbtdc/quote/notification/1"))
                                 .willReturn(aResponse()
                                                     .withHeader("Content-Type", "application/json")
                                                     .withStatus(HttpStatus.OK.value())));
        QuoteNotificationRequest delayNotification = getDelayNotification();
        placeQuoteOrder();

        //WHEN
        offlinePricingProcessor.processNotification(delayNotification, "BT-QBTDC-1", "sky");

        //THEN
        QuoteOrderMapEntity orderMap = quoteOrderMapRepository.findByOrderNumberAndSupplier("BT-QBTDC-1", "sky").get();
        Assertions.assertThat(orderMap.getStatus()).isEqualTo(QuoteOrderMapStatus.DELAY);

    }

    private QuoteNotificationRequest getDelayNotification() {
        return new QuoteNotificationRequest("D", "Delayed due to Customer");
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
                                                             new ClassPathResource(TEST_DATA_DIR + "QuoteWSAResponse" +
                                                                                           ".json",
                                                                                   this.getClass()).getInputStream(),
                                                             StandardCharsets.UTF_8))));

        wireMock.stubFor(post(urlPathEqualTo("/api/v1/notification"))
                                 .willReturn(aResponse()
                                                     .withHeader("Content-Type", "application/json")
                                                     .withBody(NotificationFactory.asJson(
                                                             Notifications.builder().transactionId(684346L).type("WSA")
                                                                     .createdAt(LocalDateTime.parse("2023-03-15T10:33" +
                                                                                                            ":46")).build()))));

      wireMock.stubFor(put(urlPathEqualTo("/api/v1/qbtdc/quote"))
          .willReturn(aResponse()
              .withHeader("Content-Type", "application/json")
              .withStatus(204)));


      quoteProcessor.createQuote(getQbtdcOrderRequest(), "SKY");

        ProcessInstance process = runtimeService.createProcessInstanceQuery().processDefinitionKey(
                QuoteProcessConstants.PROC_DEF_KEY).processInstanceBusinessKey("1").singleResult();
        Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> assertThat(process).isWaitingAt(QuoteProcessConstants.ACT_ID_GENERATE_QUOTE_PROCESS));
    }

    private QBTDCOrder getQbtdcOrderRequest() throws SOAPException, JAXBException, IOException {
        SOAPMessage message = MessageFactory.newInstance().createMessage(null,
                                                                         new ClassPathResource(TEST_DATA_DIR +
                                                                                                       "QuoteValidAsyncOrderRequest.xml",
                                                                                               QBTDCOrderFactory.class).getInputStream());
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        return ((JAXBElement<QBTDCOrder>) jaxbContext.createUnmarshaller()
                .unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
    }

    private String getSearchQuoteResponse() throws IOException {
        return IOUtils.toString(
                new ClassPathResource(TEST_DATA_DIR + "SearchQuoteResponse" +
                                              ".json",
                                      this.getClass()).getInputStream(),
                StandardCharsets.UTF_8);
    }

    private List<QuoteOrderResponse> getQBTDCOrderDTOResponse() {
      return List
          .of(new QuoteOrderResponse(1L, "Created", "BT-QBTDC-1", LocalDateTime.parse("2023-04-07T11:50:54"), "sky",
              "MONTHLY", List.of(QuoteItemTask.QuoteItemTaskBuilder.newQuoteItemTaskBuilder()
              .withAssignee(null)
              .withEircode("A00F027")
              .withProduct("WIC")
              .withConnectionType(ConnectionType.ETHERWAY_STANDARD.getPrompt())
              .withQuoteItemId(10L)
              .withTaskId("12233a334")
              .build())));
    }

}