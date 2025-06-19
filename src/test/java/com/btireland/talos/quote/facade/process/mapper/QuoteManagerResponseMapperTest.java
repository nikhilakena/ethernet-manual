package com.btireland.talos.quote.facade.process.mapper;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.soap.orders.ObjectFactory;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrderResponse;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderFactory;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.quotemanager.response.CreateQuoteResponse;
import com.btireland.talos.quote.facade.process.mapper.quotemanager.QuoteManagerResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@UnitTest
public class QuoteManagerResponseMapperTest {

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("Validate Quote Manager Complete Response is correctly mapped to QBTDCOrder Complete response xml object")
  void mapQuoteManagerCompleteResponseTo_QbtdcOrderResponseWhenQuoteManagerResponseIsAvailable_returnsQbtdcOrderResponse() throws SOAPException,
          JAXBException, IOException, TalosNotFoundException {
    CreateQuoteResponse createQuoteResponse = getCreateQuoteCompleteResponse();
    QBTDCOrderResponse expected = getQbtdcOrderCompleteResponse();
    QuoteOrderMapEntity quoteOrderMap = getQuoteOrderMap();
    QBTDCOrderResponse actual = QuoteManagerResponseMapper
        .createQBTDCCompletionResponse(createQuoteResponse, quoteOrderMap, getNotification("C"), 10L);
    Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @DisplayName("Validate Quote Manager Reject Response is correctly mapped to QBTDCOrder Reject response xml object")
  void mapQuoteManagerRejectResponseTo_QbtdcOrderResponseWhenQuoteManagerResponseIsAvailable_returnsQbtdcOrderResponse() throws SOAPException,
      JAXBException, IOException {
    CreateQuoteResponse createQuoteResponse = getCreateQuoteRejectResponse();
    QBTDCOrderResponse expected = getQbtdcOrderRejectResponse();
    QBTDCOrderResponse actual = QuoteManagerResponseMapper.createQBTDCOrderRejectResponse(createQuoteResponse, getQuoteOrderMap(), getNotification("R"), 10L);
    Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  private CreateQuoteResponse getCreateQuoteCompleteResponse() throws IOException {
   return objectMapper.readValue(IOUtils.toString(new ClassPathResource("/data/QuoteFacade/QMResponseMapperTest/QuoteCompleteResponse.json",
            this.getClass()).getInputStream(),
        StandardCharsets.UTF_8), CreateQuoteResponse.class);
  }

  private QBTDCOrderResponse getQbtdcOrderCompleteResponse() throws SOAPException, JAXBException, IOException {
    SOAPMessage message = MessageFactory.newInstance().createMessage(null, new ClassPathResource("/data/QuoteFacade/QMResponseMapperTest/QbtdcCompleteResponse.xml", QBTDCOrderFactory.class).getInputStream());
    JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    return ((JAXBElement<QBTDCOrderResponse>) jaxbContext.createUnmarshaller().unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
  }

  private CreateQuoteResponse getCreateQuoteRejectResponse() throws IOException {
    return objectMapper.readValue(IOUtils.toString(new ClassPathResource("/data/QuoteFacade/QMResponseMapperTest/QuoteRejectResponse.json",
            this.getClass()).getInputStream(),
        StandardCharsets.UTF_8), CreateQuoteResponse.class);
  }

  private QBTDCOrderResponse getQbtdcOrderRejectResponse() throws SOAPException, JAXBException, IOException {
    SOAPMessage message = MessageFactory.newInstance().createMessage(null, new ClassPathResource("/data/QuoteFacade/QMResponseMapperTest/QbtdcRejectResponse.xml", QBTDCOrderFactory.class).getInputStream());
    JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    return ((JAXBElement<QBTDCOrderResponse>) jaxbContext.createUnmarshaller().unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
  }

  private Notifications getNotification(String notificationType) {
    Notifications notification = new Notifications();
    notification.setCreatedAt(LocalDateTime.parse("2023-03-15T10:33:46"));
    notification.setType(notificationType);
    notification.setTransactionId(684346);
    return notification;
  }

  private QuoteOrderMapEntity getQuoteOrderMap() {
    QuoteOrderMapEntity quoteOrderMap = new QuoteOrderMapEntity("83758","SKY","AUTOGENERATE", true, "SKY");
    quoteOrderMap.setQuoteGroupId(11L);
    quoteOrderMap.setOrderNumber("BT-QBTDC-1");
    quoteOrderMap.setQuoteItemMapList(getQuoteItemMap(quoteOrderMap));
    return quoteOrderMap;
  }

  private List<QuoteItemMapEntity> getQuoteItemMap(QuoteOrderMapEntity quoteOrderMap) {
    return Collections.singletonList(new QuoteItemMapEntity(1L,1L, quoteOrderMap));
  }
}
