package com.btireland.talos.quote.facade.process.mapper;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.soap.orders.ObjectFactory;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrder;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderFactory;
import com.btireland.talos.quote.facade.dto.quotemanager.request.CreateQuoteRequest;
import com.btireland.talos.quote.facade.process.mapper.quotemanager.QuoteManagerRequestMapper;
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

@UnitTest
public class QuoteManagerRequestMapperTest {

  ObjectMapper objectMapper = new ObjectMapper();

  public static final String TEST_DATA_DIR = "/data/QuoteFacade/QuoteManagerRequestMapperTest/";

  @Test
  @DisplayName("Validate QBTDCOrder xml object is correctly mapped to Quote Manager Request")
  void mapQbtdcOrderToQuoteManagerRequest_whenQbtdcOrderIsAvailable_returnsQuoteManagerRequest() throws SOAPException,
          JAXBException, IOException, TalosNotFoundException {
    QBTDCOrder qbtdcOrder = QBTDCOrderFactory.qbtdcOrder();
    CreateQuoteRequest expected = getCreateQuoteRequest();
    CreateQuoteRequest actual = QuoteManagerRequestMapper.createQuoteManagerRequest(qbtdcOrder, 1L, "sky");
    Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  private CreateQuoteRequest getCreateQuoteRequest() throws IOException {
   return objectMapper.readValue(IOUtils.toString(new ClassPathResource(TEST_DATA_DIR + "CreateQuoteRequest.json",
            this.getClass()).getInputStream(),
        StandardCharsets.UTF_8), CreateQuoteRequest.class);
  }

  public QBTDCOrder qbtdcOrder() throws SOAPException, JAXBException, IOException {
    SOAPMessage message = MessageFactory.newInstance().createMessage(null, new ClassPathResource(TEST_DATA_DIR + "QBTDC-ValidOrder.xml", QBTDCOrderFactory.class).getInputStream());
    JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    QBTDCOrder qbtdcOrder = ((JAXBElement<QBTDCOrder>) jaxbContext.createUnmarshaller().unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
    return qbtdcOrder;
  }
}
