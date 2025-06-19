package com.btireland.talos.quote.facade.process.processor;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

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
import com.btireland.talos.quote.facade.util.TestFactory;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
@Import({DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
class QuoteProcessorIntegrationTest {

  @Autowired
  QuoteProcessor quoteProcessor;

  private static WireMockServer wireMock = new WireMockServer(9980);

  @BeforeAll
  public static void beforeAll() {
    wireMock.start();
  }

  @BeforeEach
  public void startWireMock() {
    wireMock.resetAll();
  }

  @AfterAll
  public static void stopWiremock() {
    wireMock.stop();
  }

  @Test
  @DisplayName("Create a QBTDC order with Completed status using Quote Facade")
  @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG",
      "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
      "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
      , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
      "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
      "flyway_schema_history"
  })
  @ExpectedDataSet(value = "/data/QuoteFacade/QuoteProcessorIntegrationTest/QbtdcOrderCompleted-result-ds.yml")
  void createCompleteQuoteResponseWhenQuoteRequestIsAvailable_returnsQbtdcOrderResponse()
      throws IOException, JAXBException, SOAPException, PersistanceException {
    //GIVEN
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

    QBTDCOrderResponse expected = getQbtdcOrderCompleteResponse();

    //WHEN
    QBTDCOrderResponse actual = quoteProcessor.createQuote(getQbtdcOrderRequest("QbtdcValidOrderRequestWithoutMail" +
                                                                                        ".xml"), "SKY");

    //THEN
    //Validate the actual xml object with expected object
    Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @DisplayName("Create a Rejected QBTDC order with Reject status using Quote Facade")
  @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG",
      "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
      "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
      , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
      "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
      "flyway_schema_history"
  })
  @ExpectedDataSet(value = "/data/QuoteFacade/QuoteProcessorIntegrationTest/QbtdcOrderRejected-result-ds.yml")
  void createRejectQuoteResponseWhenQuoteRequestIsAvailable_returnsQbtdcOrderResponse()
      throws IOException, JAXBException, SOAPException, PersistanceException {
    //GIVEN
    wireMock.stubFor(post(urlPathEqualTo("/api/v1/wag/qbtdc-orders"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(QBTDCOrderDTOFactory.asJson(TestFactory.getOrderManagerResponse()))));

    wireMock.stubFor(post(urlPathEqualTo("/api/v1/qbtdc/quote"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(IOUtils.toString(
                new ClassPathResource("/data/QuoteFacade/QuoteProcessorIntegrationTest/QuoteRejectResponse.json",
                    this.getClass()).getInputStream(),
                StandardCharsets.UTF_8))));

    wireMock.stubFor(post(urlPathEqualTo("/api/v1/notification"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(NotificationFactory.asJson(
                Notifications.builder().transactionId(684346L).type("R")
                    .createdAt(LocalDateTime.parse("2023-03-15T10:33:46")).build()))));

    QBTDCOrderResponse expected = getQbtdcOrderRejectResponse();

    //WHEN
    QBTDCOrderResponse actual = quoteProcessor.createQuote(getQbtdcOrderRequest("QbtdcValidOrderRequest.xml"), "SKY");

    //THEN
    //Validate actual xml object with the expected xml object
    Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @DisplayName("Create a QBTDC order with WSA status using Quote Facade")
  @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG",
      "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
      "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
      , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
      "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
      "flyway_schema_history"
  })
  @ExpectedDataSet(value = "/data/QuoteFacade/QuoteProcessorIntegrationTest/QbtdcOrderWSA-result-ds.yml")
  void createWSAQuoteResponseWhenQuoteRequestIsAvailable_returnsQbtdcOrderResponse()
      throws IOException, JAXBException, SOAPException, PersistanceException {
    //GIVEN
    wireMock.stubFor(post(urlPathEqualTo("/api/v1/wag/qbtdc-orders"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(QBTDCOrderDTOFactory.asJson(TestFactory.getOrderManagerResponse()))));

    wireMock.stubFor(post(urlPathEqualTo("/api/v1/qbtdc/quote"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(IOUtils.toString(
                new ClassPathResource("/data/QuoteFacade/QuoteProcessorIntegrationTest/QuoteWSAResponse.json",
                    this.getClass()).getInputStream(),
                StandardCharsets.UTF_8))));

    wireMock.stubFor(post(urlPathEqualTo("/api/v1/notification"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(NotificationFactory.asJson(
                Notifications.builder().transactionId(684346L).type("WSA")
                    .createdAt(LocalDateTime.parse("2023-03-15T10:33:46")).build()))));

    QBTDCOrderResponse expected = getQbtdcOrderWSAResponse();

    //WHEN
    QBTDCOrderResponse actual = quoteProcessor.createQuote(getQbtdcOrderRequest("QbtdcValidOrderRequest.xml"), "SKY");

    //THEN
    //Validate the actual xml object with expected object
    Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  private QBTDCOrderResponse getQbtdcOrderCompleteResponse() throws SOAPException, JAXBException, IOException {
    SOAPMessage message = MessageFactory.newInstance().createMessage(null,
        new ClassPathResource("/data/QuoteFacade/QuoteProcessorIntegrationTest/QbtdcCompleteResponse.xml",
            QBTDCOrderFactory.class).getInputStream());
    JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    return ((JAXBElement<QBTDCOrderResponse>) jaxbContext.createUnmarshaller()
        .unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
  }

  private QBTDCOrderResponse getQbtdcOrderWSAResponse() throws SOAPException, JAXBException, IOException {
    SOAPMessage message = MessageFactory.newInstance().createMessage(null,
        new ClassPathResource("/data/QuoteFacade/QuoteProcessorIntegrationTest/QbtdcWSAResponse.xml",
            QBTDCOrderFactory.class).getInputStream());
    JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    return ((JAXBElement<QBTDCOrderResponse>) jaxbContext.createUnmarshaller()
        .unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
  }

  private QBTDCOrder getQbtdcOrderRequest(String filename) throws SOAPException, JAXBException, IOException {
    SOAPMessage message = MessageFactory.newInstance().createMessage(null,
        new ClassPathResource("/data/QuoteFacade/QuoteProcessorIntegrationTest/"+ filename,
            QBTDCOrderFactory.class).getInputStream());
    JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    return ((JAXBElement<QBTDCOrder>) jaxbContext.createUnmarshaller()
        .unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
  }

  private QBTDCOrderResponse getQbtdcOrderRejectResponse() throws SOAPException, JAXBException, IOException {
    SOAPMessage message = MessageFactory.newInstance().createMessage(null,
        new ClassPathResource("/data/QuoteFacade/QuoteProcessorIntegrationTest/QbtdcRejectResponse.xml",
            QBTDCOrderFactory.class).getInputStream());
    JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    return ((JAXBElement<QBTDCOrderResponse>) jaxbContext.createUnmarshaller()
        .unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
  }

}