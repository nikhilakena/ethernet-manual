package com.btireland.talos.quote.facade.process.processor;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import com.btireland.talos.core.common.rest.exception.checked.TalosInternalErrorException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.client.asset.notcom.NotificationClient;
import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import com.btireland.talos.ethernet.engine.mq.CerberusDataSyncMessageProducer;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import com.btireland.talos.quote.facade.base.constant.RestMapping;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.dto.notcom.Access;
import com.btireland.talos.quote.facade.dto.notcom.Address;
import com.btireland.talos.quote.facade.dto.notcom.Location;
import com.btireland.talos.quote.facade.dto.notcom.LogicalLink;
import com.btireland.talos.quote.facade.dto.notcom.NotificationRequest;
import com.btireland.talos.quote.facade.dto.notcom.QuoteItem;
import com.btireland.talos.quote.facade.dto.notcom.QuoteOrder;
import com.btireland.talos.quote.facade.dto.notcom.RejectionDetails;
import com.btireland.talos.quote.facade.dto.notcom.Site;
import com.btireland.talos.quote.facade.util.TestFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
@Import({DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
class QuoteWorkflowProcessorIntegrationTest {

    public static final String TEST_DATA_DIR = "/data/QuoteFacade/QuoteWorkflowProcessorIntegrationTest/";
    private static final WireMockServer wireMock = new WireMockServer(9980);
    private static final GreenMail mailServer = new GreenMail(ServerSetupTest.SMTP);

    @Autowired
    QuoteWorkflowProcessor quoteWorkflowProcessor;

    @MockBean
    NotificationClient notificationClient;

    @MockBean
    CerberusDataSyncMessageProducer cerberusDataSyncMessageProducer;

    @Captor
    ArgumentCaptor<Notifications> notificationCaptor;

    @BeforeAll
    public static void beforeAll() {
        wireMock.start();
        mailServer.start();
    }

    @AfterAll
    public static void stopWiremock() {
        wireMock.stop();
        mailServer.stop();
    }

    @BeforeEach
    public void startWireMock() {
        wireMock.resetAll();
    }

    @Test
    @DisplayName("Search delay notification for a quote group")
    @DataSet(cleanBefore = true, value = TEST_DATA_DIR + "QuoteMap-ds.yml", executeScriptsBefore = {"/data" +
            "/CucumberTest" +
            "/reset_sequences.sql"}, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG",
            "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
            , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "sendDelay-result-ds.yml")
    void sendDelayNotification_givenQuoteGroupId_notificationIsSentToNotCom() throws TalosNotFoundException,
            IOException, TalosInternalErrorException {
        //GIVEN
        wireMock.stubFor(get(urlPathEqualTo(RestMapping.SEARCH_QUOTE))
                                 .willReturn(aResponse()
                                                     .withHeader("Content-Type", "application/json")
                                                     .withBody(getSearchQuoteResponse())));
        OrdersDTO expected = getNotcomOrderDTO();

        //WHEN
        quoteWorkflowProcessor.sendDelayedNotification(1L);

        //THEN
        verify(notificationClient).createNotification(notificationCaptor.capture());
        Notifications delayNotification = notificationCaptor.getValue();
        OrdersDTO ordersDTO = new ObjectMapper().readValue(new String(delayNotification.getContent()), OrdersDTO.class);
        assertEquals(expected,ordersDTO);

    }

    @Test
    @DisplayName("Test auto email is sent for complete quote")
    @DataSet(cleanBefore = true, value = TEST_DATA_DIR + "QuoteMapComplete-ds.yml", executeScriptsBefore = {"/data" +
            "/CucumberTest/reset_sequences.sql"}, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY",
            "ACT_GE_SCHEMA_LOG",
            "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
            , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    void sendEmail_givenQuoteGroupId_emailIsSent() throws TalosNotFoundException, TalosInternalErrorException,
            JsonProcessingException {
        //GIVEN
        wireMock.stubFor(get(urlPathEqualTo("/api/v1/qbtdc/quote/mail/1"))
                                 .willReturn(aResponse()
                                                     .withHeader("Content-Type", "application/json")
                                                     .withBody(QBTDCOrderDTOFactory.asJson(TestFactory.getQuoteGroupEmailResponse()))));
        //WHEN
        quoteWorkflowProcessor.sendQuoteEmail(1L);

        //THEN
        assertTrue(mailServer.getReceivedMessages().length>0);

    }

    private String getSearchQuoteResponse() throws IOException {
        return IOUtils.toString(
                new ClassPathResource(TEST_DATA_DIR + "SearchQuoteResponse" +
                                              ".json",
                                      this.getClass()).getInputStream(),
                StandardCharsets.UTF_8);
    }

    private OrdersDTO getNotcomOrderDTO() {
        return OrdersDTO.builder()
                .oao("sky")
                .dataContract("WAG")
                .originatorCode("EXT")
                .operatorName("SKY")
                .operatorCode("83758")
                .resellerTransactionId("10101")
                .resellerOrderRequestDateTime("07/04/2023 11:50:54")
                .createdAt("2023-04-07T11:50:54")
                .orderNumber("BT-QBTDC-1234")
                .serviceType("QBTDC")
                .delayReason("Delayed Due to Customer")
                .build();
    }

    @Test
    @DisplayName("Send Complete notification for a quote group")
    @DataSet(cleanBefore = true, value = TEST_DATA_DIR + "QuoteMapAsync-ds.yml", executeScriptsBefore = {"/data" +
        "/CucumberTest" +
        "/reset_sequences.sql"}, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG",
        "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
        "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
        , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
        "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
        "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "sendCompletion-result-ds.yml")
    void sendCompleteNotification_givenQuoteGroupId_notificationIsSentToNotCom() throws TalosNotFoundException,
            IOException, TalosInternalErrorException {
        //GIVEN
        wireMock.stubFor(get(urlPathEqualTo("/api/v1/qbtdc/quote/details/1"))
                                 .willReturn(aResponse()
                                                     .withHeader("Content-Type", "application/json")
                                                     .withBody(getQuoteGroupResponse())));
        NotificationRequest expected = getNotcomOrderDTOForAsyncComplete();
        ArgumentCaptor<OrdersDTO> ordersDTOCaptor = ArgumentCaptor.forClass(OrdersDTO.class);


        //WHEN
        quoteWorkflowProcessor.sendCompleteNotification(1L);

        //THEN
        verify(notificationClient).createNotification(notificationCaptor.capture());
        Notifications delayNotification = notificationCaptor.getValue();
        NotificationRequest actual = new ObjectMapper().readValue(new String(delayNotification.getContent()),
                                                             NotificationRequest.class);
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

        verify(cerberusDataSyncMessageProducer).sendOrderData(ordersDTOCaptor.capture());
        OrdersDTO dto = ordersDTOCaptor.getValue();
        assertEquals("BT-QBTDC-1234",dto.getOrderNumber());
        assertEquals("sky",dto.getOao());
        assertEquals("C",dto.getLastNotificationType());
    }

    private String getQuoteGroupResponse() throws IOException {
        return IOUtils.toString(
                new ClassPathResource(TEST_DATA_DIR + "GetQuoteGroupResponse" +
                                              ".json",
                                      this.getClass()).getInputStream(),
                StandardCharsets.UTF_8);
    }

    private NotificationRequest getNotcomOrderDTOForAsyncComplete() {
        return NotificationRequest.NotificationRequestBuilder.notificationRequestBuilder()
                .oao("sky")
                .dataContract("WAG")
                .originatorCode("EXT")
                .operatorName("SKY")
                .operatorCode("83758")
                .resellerTransactionId("XXX")
                .projectKey("VODA_01")
                .resellerOrderRequestDateTime("08/01/2021 19:13:14")
                .createdAt("2021-01-08T19:13:14")
                .orderNumber("BT-QBTDC-1234")
                .serviceType("QBTDC")
                .mode("async")
                .talosOrderId(1L)
                .qbtdc(new QuoteOrder("MONTHLY",
                                      Arrays.asList(QuoteItem.QuoteItemBuilder.quoteItemBuilder()
                                                            .customerAccess(new Access(ActionFlag.P, "10",
                                                                                       "STANDARD",
                                                                                       new Site(new Location(
                                                                                               "A00F027")), null,
                                                                                       "OFF-NET", "WIC"
                                                            ))

                                                            .wholesalerAccess(new Access(ActionFlag.CH, new Site(
                                                                    new Location("INTERNET"))))

                                                            .logicalLink(new LogicalLink(ActionFlag.P, "10000",
                                                                                         "PRIMARY", "31"))

                                                            .notes("Rejection Notes")
                                                            .offlineQuoted("N")
                                                            .product("WIC")
                                                            .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                                                            .quoteItemId(1L)
                                                            .rejectionDetails(new RejectionDetails(
                                                                    "EIRCODE_INVALID", "Eircode is invalid"))
                                                            .status("Rejected")
                                                            .term(3)
                                                            .build(),

                                                    QuoteItem.QuoteItemBuilder.quoteItemBuilder()
                                                            .customerAccess(new Access(ActionFlag.P, "10", "STANDARD",
                                                                                       new Site(new Location("A00F028",
                                                                                                             new Address("HUGH JORDAN & CO. LIMITED, UNIT 4, " +
                                                                                                                                 "CONSTELLATION ROAD, AIRWAYS" +
                                                                                                                                 " INDUSTRIAL ESTATE, DUBLIN " +
                                                                                                                                 "17"), "OFF-NET", "N")),
                                                                                       "OFF-NET", "OFF-NET", "WIC"
                                                            ))


                                                            .wholesalerAccess(new Access(ActionFlag.CH,
                                                                                         new Site(new Location(
                                                                                                 "INTERNET"))))
                                                            .logicalLink(new LogicalLink(ActionFlag.P, "10000",
                                                                                         "PRIMARY", "31"))
                                                            .etherflowRecurringPrice("600")
                                                            .etherwayRecurringPrice("400")
                                                            .nonRecurringPrice("2000")
                                                            .offlineQuoted("Y")
                                                            .product("WIC")
                                                            .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                                                            .quoteItemId(2L)
                                                            .recurringPrice("1000")
                                                            .status("Quoted")
                                                            .term(3)
                                                            .build())))
                .build();
    }
}
