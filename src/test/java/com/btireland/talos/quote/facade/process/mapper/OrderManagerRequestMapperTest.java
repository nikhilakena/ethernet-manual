package com.btireland.talos.quote.facade.process.mapper;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QBTDCOrderDTO;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.ordermanager.QuoteOrder;
import com.btireland.talos.quote.facade.dto.quotemanager.response.CreateQuoteResponse;
import com.btireland.talos.quote.facade.process.mapper.ordermanager.OrderManagerRequestMapper;
import com.btireland.talos.quote.facade.util.TestFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@UnitTest
class OrderManagerRequestMapperTest {

    private static final String TEST_DATA_DIR = "/data/QuoteFacade/QuoteRequestMapperTest/";
    private OrderManagerRequestMapper orderManagerRequestMapper;


    @Test
    @DisplayName("Validate Qbtdc request and quote manager response is correctly mapped to Order Manager request")
    void mapQuoteToOrderManagerRequest_withQuoteRequestAndQuoteManagerResponse_returnsOrderManagerRequest() throws SOAPException, JAXBException, IOException, TalosNotFoundException {
        var qbtdcOrder = TestFactory.qbtdcOrder();
        var expected = fetchQBTDCOrderDTO();
        QuoteOrderMapEntity quoteOrderMap = createQuoteOrderMap();
        CreateQuoteResponse createQuoteResponse = fetchQuoteManagerResponse();

        QuoteOrder actualOrderManagerRequest = OrderManagerRequestMapper.createOrderManagerRequest(createQuoteResponse,
                                                                                                   quoteOrderMap);
        Assertions.assertThat(actualOrderManagerRequest).usingRecursiveComparison().isEqualTo(expected);
    }

    private CreateQuoteResponse fetchQuoteManagerResponse() throws IOException {
        return new ObjectMapper().readValue(IOUtils.toString(new ClassPathResource(TEST_DATA_DIR +
                                                                                           "QuoteSuccessResponse" +
                                                                                           ".json", this.getClass()).getInputStream(), StandardCharsets.UTF_8), CreateQuoteResponse.class);
    }

    private QuoteOrder fetchQBTDCOrderDTO(){
        QuoteOrder qbtdcOrderDTO = QBTDCOrderDTOFactory.defaultQBTDCOrderDTO();
        qbtdcOrderDTO.getOrders().setOrderStatus("talos complete");
        return qbtdcOrderDTO;
    }

    private QuoteOrderMapEntity createQuoteOrderMap(){
        QuoteOrderMapEntity quoteOrderMap = new QuoteOrderMapEntity("83758", "SKY",
                                                                    "1",Boolean.FALSE, "SKY");
        quoteOrderMap.setQuoteItemMapList(List.of(new QuoteItemMapEntity(1L, 111L, quoteOrderMap)));
        return quoteOrderMap;
    }
}
