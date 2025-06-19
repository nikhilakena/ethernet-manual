package com.btireland.talos.ethernet.engine.util;


import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.*;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.dto.ordermanager.Order;
import com.btireland.talos.quote.facade.dto.ordermanager.Qbtdcs;
import com.btireland.talos.quote.facade.dto.ordermanager.Quote;
import com.btireland.talos.quote.facade.dto.ordermanager.QuoteOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.remondis.cdc.consumer.pactbuilder.ConsumerExpects;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

public class QBTDCOrderDTOFactory {

    public static String asJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(object);
    }

    public static QuoteDetailsDTO defaultQuoteDetailsDTO() {
        return QuoteDetailsDTO.builder()
                .orders(defaultOrdersDTO())
                .qbtdcs(defaultQbtdcsDTO())
                .quote(defaultQuoteDTO())
                .build();
    }

    public static QuoteDTO defaultQuoteDTO() {
        return QuoteDTO.builder()
                .id(1L)
                .orderId(1234L)
                .oao("sky")
                .product("WEC")
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                .term(2)
                .ipRange(500)
                .recurringPrice("4379.2")
                .nonRecurringPrice("1000")
                .logicalActionFlag("P")
                .logicalBandwidth("30")
                .logicalProfile("PREMIUM_100")
                .aendEircode("A00F027")
                .aendMultiEircode("N")
                .aendAddress("BERKLEY RECRUITMENT (GROUP) LTD., UNIT 3, WATERGOLD BUILDING, DOUGLAS, CORK")
                .aendTargetAccessSupplier("ON-NET")
                .aendNetworkStatus("ON-NET")
                .aendActionFlag("P")
                .aendBandwidth("10")
                .aendSla("ENHANCED")
                .bendHandOverNode("EQUINIX_DB1")
                .bendActionFlag("CH")
                .build();
    }

    public static QbtdcsDTO defaultQbtdcsDTO() {
        return QbtdcsDTO.builder()
                .id(1L)
                .orderId(1234L)
                .applicationDate("10/05/2021")
                .mode("async")
                .recurringFrequency("MONTHLY")
                .build();
    }

    public static OrdersDTO defaultOrdersDTO() {
        return OrdersDTO.builder()
                .orderId(1234L)
                .agent("btpricing")
                .orderNumber("BT-QBTDC-1234")
                .orderServiceType("QBTDC")
                .createdAt("2020-12-17 09:48:25")
                .changedAt("2020-12-17 09:48:27")
                .oao("sky")
                .workflowStatus("In System")
                .dataContractName("WAG")
                .operatorName("SKY")
                .operatorCode("65843")
                .orderRequestDateTime("2020-12-17 09:48:23")
                .orderStatus("complete")
                .originatorCode("EXT")
                .transactionId("10101")
                .projectKey("xyz")
                .build();
    }

    public static QuoteOrder defaultQBTDCOrderDTO() {
        return new QuoteOrder(defaultOrdersDTORequest(),defaultQbtdcsDTORequest(),defaultQuoteDTOListRequest());

    }


    public static Order defaultOrdersDTORequest() {
        return Order.OrderBuilder.orderBuilder()
                .oao("sky")
                .obo("sky")
                .operatorName("SKY")
                .operatorCode("83758")
                .dataContractName("WAG")
                .originatorCode("EXT")
                .transactionId("10101")
                .orderRequestDateTime("25/05/2022 05:17:28")
                .orderNumber("1")
                .orderServiceType("QBTDC")
                .orderStatus("Talos Pending")
                .workflowStatus("Talos In System")
                .projectKey("xyz")
                .build();
    }

    public static Qbtdcs defaultQbtdcsDTORequest() {
        return new Qbtdcs("async","25/05/2022","ANNUAL");
    }


    public static List<Quote> defaultQuoteDTOListRequest() {
        return List.of(defaultQuoteItemDTORequest());
    }

    private static Quote defaultQuoteItemDTORequest() {
        return Quote.QuoteBuilder.quoteBuilder()
                .product("WIC")
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                .term(5)
                .ipRange(31)
                .logicalActionFlag("P")
                .logicalBandwidth("6000")
                .logicalProfile("PRIMARY")
                .aendEircode("A00F027")
                .aendReqAccessSupplier("ON-NET")
                .aendActionFlag("P")
                .aendBandwidth("10")
                .aendSla("ENHANCED")
                .bendHandOverNode("INTERNET")
                .bendActionFlag("CH")
                .build();
    }

    private static QuoteDTO defaultQuoteItemDTORequestForAsynCompletion() {
        return QuoteDTO.builder()
                .term(5)
                .ipRange(31)
                .logicalActionFlag("P")
                .logicalBandwidth("500")
                .logicalProfile("PREMIUM_100")
                .aendEircode("A00F027")
                .aendReqAccessSupplier("ON-NET")
                .aendActionFlag("P")
                .aendBandwidth("10")
                .aendSla("ENHANCED")
                .bendHandOverNode("EQUINIX_DB1")
                .bendActionFlag("CH")
                .offlineQuoted("Y")
                .build();
    }


    public static DslPart defaultQBTDCResponsePactFormat() {
        return ConsumerExpects.type(QuoteDetailsDTO.class)
                .ignoreMissingValues()
                .build(new PactDslJsonBody(), defaultQuoteDetailsDTO());

    }

    public static QuoteOrder savedQBTDCResponse() {
        return defaultQBTDCOrderDTO();
    }



    public static String defaultQBTDCOrderRequestAsJson() throws Exception {
        return asJson(defaultQBTDCOrderDTOWithOriginalRequest());
    }

    public static QuoteOrder defaultQBTDCOrderDTOWithOriginalRequest() throws IOException {
        QuoteOrder quoteOrder = defaultQBTDCOrderDTO();
        return quoteOrder;
    }


    public static DslPart defaultQBTDCOrderDTOResponsePactFormat() throws IOException {
        var qbtdcOrderDTO = savedQBTDCResponse();
        return LambdaDsl.newJsonBody((o) -> o
                .object("orders", (orders) -> {
                    orders.numberType("orderId", qbtdcOrderDTO.getOrders().getOrderId());
                    orders.stringType("orderNumber", qbtdcOrderDTO.getOrders().getOrderNumber());
                })
                .object("qbtdcs", (qbtdcs) -> {
                    qbtdcs.numberType("id", qbtdcOrderDTO.getQbtdcs().getId());
                })
                .minArrayLike("quoteList",1,quote ->{
                    quote.numberType("id", qbtdcOrderDTO.getQuoteList().get(0).getId());
                }))
                .build();
    }

}
