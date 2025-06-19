package com.btireland.talos.quote.facade.process.mapper;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.notcom.Access;
import com.btireland.talos.quote.facade.dto.notcom.Address;
import com.btireland.talos.quote.facade.dto.notcom.Location;
import com.btireland.talos.quote.facade.dto.notcom.LogicalLink;
import com.btireland.talos.quote.facade.dto.notcom.NotificationRequest;
import com.btireland.talos.quote.facade.dto.notcom.QuoteItem;
import com.btireland.talos.quote.facade.dto.notcom.QuoteOrder;
import com.btireland.talos.quote.facade.dto.notcom.Site;
import com.btireland.talos.quote.facade.dto.quotemanager.response.CreateQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.SearchQuoteResponse;
import com.btireland.talos.quote.facade.process.mapper.notcom.NotcomRequestMapper;
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
class NotcomRequestMapperTest {

    private static final String TEST_DATA_DIR = "/data/QuoteFacade/NotcomRequestMapperTest/";

    @Test
    @DisplayName("Validate request to Notcom is created correctly")
    void createNotcomrequest_withOrderRequestAndQuoteManagerResponse_returnsNotComRequest() throws IOException,
            JAXBException, SOAPException, TalosNotFoundException {
        //GIVEN
        var qbtdcOrder = TestFactory.qbtdcOrder();
        CreateQuoteResponse createQuoteResponse = fetchQuoteManagerResonse();
        QuoteOrderMapEntity quoteOrderMap = TestFactory.getQuoteOrderMap();
        NotificationRequest expected = getNotcomOrderDTO();

        //WHEN
        NotificationRequest actual = NotcomRequestMapper.createNotcomOrders(createQuoteResponse, 1234L,
                                                                            quoteOrderMap);
        //THEN
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringFields("talosOrderId").isEqualTo(expected);
    }

    @Test
    @DisplayName("Validate delay notification request to Notcom is created correctly")
    void createNotcomDelayRequest_givenQuoteManagerResponseAndQuoteOrderMap_returnsDelayNotComRequest() throws IOException {
        //GIVEN
        QuoteOrderMapEntity quoteOrderMap = getOrderMap();
        SearchQuoteResponse searchQuoteResponse = getSearchQuoteResponse();
        NotificationRequest expected = getNotcomDelayOrderDTO();

        //WHEN
        NotificationRequest actual = NotcomRequestMapper.createNotcomOrderForDelay(searchQuoteResponse.getQuoteGroups().get(0),
                                                                         quoteOrderMap);

        //THEN
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private SearchQuoteResponse getSearchQuoteResponse() throws IOException {
        return new ObjectMapper().readValue(IOUtils.toString(
                new ClassPathResource(TEST_DATA_DIR + "SearchQuoteResponse" +
                                              ".json",
                                      this.getClass()).getInputStream(),
                StandardCharsets.UTF_8), SearchQuoteResponse.class);
    }

    private CreateQuoteResponse fetchQuoteManagerResonse() throws IOException {
        return new ObjectMapper().readValue(IOUtils.toString(new ClassPathResource(TEST_DATA_DIR +
                                                                                           "QuoteSuccessResponse" +
                                                                                           ".json", this.getClass()).getInputStream(), StandardCharsets.UTF_8), CreateQuoteResponse.class);
    }

    private QuoteOrderMapEntity getOrderMap() {
        return new QuoteOrderMapEntity("83758", "SKY", "BT-QBTDC-1234", false, "sky");
    }

    private NotificationRequest getNotcomDelayOrderDTO() {
        return NotificationRequest.NotificationRequestBuilder.notificationRequestBuilder()
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

    private NotificationRequest getNotcomOrderDTO() {
        return NotificationRequest.NotificationRequestBuilder.notificationRequestBuilder()
                .orderId(1234L)
                .oao("sky")
                .dataContract("WAG")
                .originatorCode("EXT")
                .operatorName("SKY")
                .operatorCode("83758")
                .resellerTransactionId("10101")
                .resellerOrderRequestDateTime("15/03/2023 10:33:46")
                .createdAt("2023-03-15T10:33:46")
                .orderNumber("BT-QBTDC-1234")
                .serviceType("QBTDC")
                .mode("sync")
                .projectKey("projectkey")
                .qbtdc(new QuoteOrder("MONTHLY", List.of(QuoteItem.QuoteItemBuilder.quoteItemBuilder()
                                                                 .status("Quoted")
                                                                 .quoteItemId(111L)
                                                                 .orderId(1234L)
                                                                 .product("WIC")
                                                                 .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                                                                 .term(5)
                                                                 .recurringPrice("1000")
                                                                 .nonRecurringPrice("1050")
                                                                 .etherflowRecurringPrice("1050")
                                                                 .etherwayRecurringPrice("1500")
                                                                 .logicalLink(new LogicalLink(ActionFlag.P, "6000",
                                                                                              "PRIMARY", "31"))
                                                                 .customerAccess(new Access(ActionFlag.P, "10",
                                                                                            "ENHANCED",
                                                                                            new Site(new Location(
                                                                                                    "A00F027",
                                                                                                    new Address("HUGH JORDAN & CO. LIMITED, UNIT 4, CONSTELLATION ROAD, AIRWAYS INDUSTRIAL ESTATE, DUBLIN 17"),
                                                                                                    "OFF-NET", "N")),
                                                                                            "OFF-NET", "ON-NET"
                                                                 ))


                                                                 .wholesalerAccess(new Access(ActionFlag.CH,
                                                                                              new Site(new Location(
                                                                                                      "INTERNET"))))

                                                                 .build()))
                ).build();
    }
}

  
