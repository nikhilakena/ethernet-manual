package com.btireland.talos.quote.facade.process.mapper;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.dto.QuoteItemEmailDTO;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.process.mapper.quotemanager.QuoteGroupEmailResponseMapper;
import com.btireland.talos.quote.facade.util.TestFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UnitTest
class QuoteGroupEmailResponseMapperTest {

  private static final String TEST_DATA_DIR = "/data/QuoteFacade/QuoteRequestMapperTest/";


  @Test
  @DisplayName("Validate QuoteGroupEmailResponse is correctly mapped to List of QuoteItemEmailDTO")
  void mapQuoteGroupEmailResponseToQuoteItemEmailDTO_whenQuoteGroupEmailREsponseIsAvailable_returnsListOfQuoteItemEmailDTO()
          throws SOAPException, JAXBException, IOException, TalosNotFoundException {
    var quoteGroupEmailResponse = TestFactory.getQuoteGroupEmailResponse();
    var expected = TestFactory.getQuoteItemEmailList();
    QuoteOrderMapEntity quoteOrderMap = TestFactory.getQuoteOrderMap();
    quoteOrderMap.setQuoteGroupId(1L);
    Map<String, String> handOffMap = new HashMap<>();
    handOffMap.put("INTERNET", "Internet");
    List<QuoteItemEmailDTO> actual= QuoteGroupEmailResponseMapper.createQuoteItemEmailList(quoteGroupEmailResponse,
        quoteOrderMap, handOffMap);
    Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @DisplayName("Validate QuoteGroupEmailResponse for WEC is correctly mapped to List of QuoteItemEmailDTO")
  void mapQuoteGroupEmailResponseForWECToQuoteItemEmailDTO_whenQuoteGroupEmailResponseIsAvailable_returnsListOfQuoteItemEmailDTO()
      throws SOAPException, JAXBException, IOException, TalosNotFoundException {
    //GIVEN
    var quoteGroupEmailResponse = TestFactory.getQuoteGroupEmailResponseForWEC();
    var expected = TestFactory.getQuoteItemEmailListForWEC();
    QuoteOrderMapEntity quoteOrderMap = TestFactory.getQuoteOrderMap();
    quoteOrderMap.setQuoteGroupId(1L);
    Map<String, String> handOffMap = new HashMap<>();
    handOffMap.put("INTERNET", "Internet");

    //WHEN
    List<QuoteItemEmailDTO> actual = QuoteGroupEmailResponseMapper.createQuoteItemEmailList(quoteGroupEmailResponse,
        quoteOrderMap, handOffMap);

    //THEN
    Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @DisplayName("Validate QuoteGroupEmailResponse For Delayed Quote is correctly mapped to List of QuoteItemEmailDTO")
  void mapQuoteGroupEmailResponseForDelyedQuoteToQuoteItemEmailDTO_whenQuoteGroupEmailResponseIsAvailable_returnsListOfQuoteItemEmailDTO()
      throws SOAPException, JAXBException, IOException, TalosNotFoundException {
    //GIVEN
    var quoteGroupEmailResponse = TestFactory.getQuoteGroupEmailResponse();
    var expected = TestFactory.getQuoteItemEmailListForDelay();
    QuoteOrderMapEntity quoteOrderMap = TestFactory.getQuoteOrderMapWithDelayStatus();
    quoteOrderMap.setQuoteGroupId(1L);
    Map<String, String> handOffMap = new HashMap<>();
    handOffMap.put("INTERNET", "Internet");

    //WHEN
    List<QuoteItemEmailDTO> actual= QuoteGroupEmailResponseMapper.createQuoteItemEmailList(quoteGroupEmailResponse,
        quoteOrderMap, handOffMap);

    //THEN
    Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }


}
