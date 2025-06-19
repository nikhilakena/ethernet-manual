package com.btireland.talos.quote.facade.util;

import com.btireland.talos.ethernet.engine.client.asset.ordermanager.OrdersDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QBTDCOrderDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QuoteDTO;
import com.btireland.talos.ethernet.engine.dto.QuoteItemEmailDTO;
import com.btireland.talos.ethernet.engine.soap.orders.ObjectFactory;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrder;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.NetworkType;
import com.btireland.talos.quote.facade.base.enumeration.internal.PriceType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ProfileType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteStateType;
import com.btireland.talos.quote.facade.base.enumeration.internal.RecurringChargePeriodType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ServiceClassType;
import com.btireland.talos.quote.facade.base.enumeration.internal.SlaType;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuotePriceResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.email.QuoteEmailAEndResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.email.QuoteEmailLogicalResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.email.QuoteEmailResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.email.QuoteGroupEmailResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.springframework.core.io.ClassPathResource;

public class TestFactory {

    public static QBTDCOrder qbtdcOrder() throws SOAPException, JAXBException, IOException {
        SOAPMessage message = MessageFactory.newInstance().createMessage(null, new ClassPathResource("/data/QuoteFacade/QuoteRequestMapperTest/QBTDC-ValidOrder.xml", TestFactory.class).getInputStream());
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        QBTDCOrder qbtdcOrder =
                ((JAXBElement<QBTDCOrder>) jaxbContext.createUnmarshaller().unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
        return qbtdcOrder;
    }

    public static QBTDCOrderDTO getOrderManagerResponse() {
        QBTDCOrderDTO qbtdcOrderDTO = new QBTDCOrderDTO();
        qbtdcOrderDTO.setOrders(new OrdersDTO());
        qbtdcOrderDTO.getOrders().setOrderNumber("BT-QBTDC-1");
        qbtdcOrderDTO.getOrders().setOrderId(100L);
        QuoteDTO quoteDTO = new QuoteDTO();
        quoteDTO.setId(10L);
        qbtdcOrderDTO.setQuoteList(Collections.singletonList(quoteDTO));
        return qbtdcOrderDTO;
    }

    public static QuoteOrderMapEntity getQuoteOrderMap(){
        QuoteOrderMapEntity quoteOrderMap = new QuoteOrderMapEntity("83758", "SKY",
                                                          "AUTOGENERATE",Boolean.TRUE, "SKY");
        quoteOrderMap.setOrderNumber("BT-QBTDC-1234");
        quoteOrderMap.setQuoteItemMapList(List.of(new QuoteItemMapEntity(1L,111L,quoteOrderMap)));
        return quoteOrderMap;
    }

    public static QuoteGroupEmailResponse getQuoteGroupEmailResponse() {
        QuoteEmailAEndResponse aEndResponse = new QuoteEmailAEndResponse("D17XD78",
            "HUGH JORDAN; CO. LIMITED, UNIT 4, CONSTELLATION ROAD, AIRWAYS INDUSTRIAL ESTATE, DUBLIN 17",
            10F, NetworkType.OFF_NET, SlaType.STANDARD);
        QuoteEmailLogicalResponse logicalResponse = new QuoteEmailLogicalResponse(31L, 1000F, ProfileType.PRIMARY);
        List<QuotePriceResponse> quotePriceResponses = new ArrayList<>(
            List.of(new QuotePriceResponse(RecurringChargePeriodType.MONTHLY, PriceType.RECURRING, "500"),
                new QuotePriceResponse(null, PriceType.NON_RECURRING, "2000")));
        QuoteEmailResponse quoteEmailResponse = new QuoteEmailResponse(1L, QuoteStateType.QUOTED, 1, aEndResponse, logicalResponse, "INTERNET",
            quotePriceResponses, null, "Test Notes", "Test delay reason", ConnectionType.ETHERWAY_DIVERSE, "G1");
        QuoteGroupEmailResponse quoteGroupEmailResponse = new QuoteGroupEmailResponse(1L,
            Collections.singletonList(quoteEmailResponse), ServiceClassType.WIC, LocalDateTime.parse("2022-05-25T05:17:28"),
            LocalDateTime.parse("2022-05-25T05:17:28"));
        return quoteGroupEmailResponse;
    }

    public static QuoteGroupEmailResponse getQuoteGroupEmailResponseForWEC() {
        QuoteEmailAEndResponse aEndResponse = new QuoteEmailAEndResponse("D17XD78",
            "HUGH JORDAN; CO. LIMITED, UNIT 4, CONSTELLATION ROAD, AIRWAYS INDUSTRIAL ESTATE, DUBLIN 17",
            null, null, null);
        QuoteEmailLogicalResponse logicalResponse = new QuoteEmailLogicalResponse(null, 1000F, ProfileType.PRIMARY);
        List<QuotePriceResponse> quotePriceResponses = new ArrayList<>(
            List.of(new QuotePriceResponse(RecurringChargePeriodType.MONTHLY, PriceType.RECURRING, "500"),
                new QuotePriceResponse(null, PriceType.NON_RECURRING, "2000")));
        QuoteEmailResponse quoteEmailResponse = new QuoteEmailResponse(1L, QuoteStateType.QUOTED, 1, aEndResponse, logicalResponse, "INTERNET",
            quotePriceResponses, null, "Test Notes", "Test delay reason", ConnectionType.ETHERWAY_DIVERSE, "G1");
        QuoteGroupEmailResponse quoteGroupEmailResponse = new QuoteGroupEmailResponse(1L,
            Collections.singletonList(quoteEmailResponse), ServiceClassType.WEC, LocalDateTime.parse("2022-05-25T05:17:28"),
            LocalDateTime.parse("2022-05-25T05:17:28"));
        return quoteGroupEmailResponse;
    }

    public static QuoteOrderMapEntity getQuoteOrderMapWithDelayStatus(){
        QuoteOrderMapEntity quoteOrderMap = getQuoteOrderMap();
        quoteOrderMap.setStatus(QuoteOrderMapStatus.DELAY);
        return quoteOrderMap;
    }


    public static List<QuoteItemEmailDTO> getQuoteItemEmailList() {
        List<QuoteItemEmailDTO> quoteItemEmailList = new ArrayList<>();
        QuoteItemEmailDTO quoteItemEmailDTO = new QuoteItemEmailDTO();
        quoteItemEmailDTO.setQuoteId(111L);
        quoteItemEmailDTO.setTerm("1 Year");
        quoteItemEmailDTO.setSla("Standard SLA");
        quoteItemEmailDTO.setServiceClass("WIC");
        quoteItemEmailDTO.setConnectionType("Etherway Diverse");
        quoteItemEmailDTO.setRecurringFrequency("Monthly");
        quoteItemEmailDTO.setRecurringPrice("500");
        quoteItemEmailDTO.setNonRecurringPrice("2000");
        quoteItemEmailDTO.setLogicalLinkProfile("Primary (0% AF)");
        quoteItemEmailDTO.setLogicalLinkBandwidth("1000 Mb");
        quoteItemEmailDTO.setHandoff("Internet");
        quoteItemEmailDTO.setBandwidth("10 Gb");
        quoteItemEmailDTO.setAEndTargetAccessSupplier("3rd Party Supplier");
        quoteItemEmailDTO.setAEndEircode("D17XD78");
        quoteItemEmailDTO.setAEndAddress("HUGH JORDAN; CO. LIMITED, UNIT 4, CONSTELLATION ROAD, AIRWAYS INDUSTRIAL ESTATE, DUBLIN 17");
        quoteItemEmailDTO.setIpRange("31");
        quoteItemEmailDTO.setReason("Test Notes");
        quoteItemEmailDTO.setStatus("Quoted");
        quoteItemEmailDTO.setGroup("G1");

        quoteItemEmailList.add(quoteItemEmailDTO);
        return  quoteItemEmailList;
    }

    public static List<QuoteItemEmailDTO> getQuoteItemEmailListForWEC() {
        List<QuoteItemEmailDTO> quoteItemEmailList = new ArrayList<>();
        QuoteItemEmailDTO quoteItemEmailDTO = new QuoteItemEmailDTO();
        quoteItemEmailDTO.setQuoteId(111L);
        quoteItemEmailDTO.setTerm("1 Year");
        quoteItemEmailDTO.setServiceClass("WEC");
        quoteItemEmailDTO.setConnectionType("Etherway Diverse");
        quoteItemEmailDTO.setRecurringFrequency("Monthly");
        quoteItemEmailDTO.setRecurringPrice("500");
        quoteItemEmailDTO.setNonRecurringPrice("2000");
        quoteItemEmailDTO.setLogicalLinkProfile("Primary (0% AF)");
        quoteItemEmailDTO.setLogicalLinkBandwidth("1000 Mb");
        quoteItemEmailDTO.setHandoff("Internet");
        quoteItemEmailDTO.setBandwidth("Existing");
        quoteItemEmailDTO.setAEndEircode("D17XD78");
        quoteItemEmailDTO.setAEndAddress("HUGH JORDAN; CO. LIMITED, UNIT 4, CONSTELLATION ROAD, AIRWAYS INDUSTRIAL ESTATE, DUBLIN 17");
        quoteItemEmailDTO.setReason("Test Notes");
        quoteItemEmailDTO.setStatus("Quoted");
        quoteItemEmailDTO.setGroup("G1");

        quoteItemEmailList.add(quoteItemEmailDTO);
        return  quoteItemEmailList;
    }

    public static List<QuoteItemEmailDTO> getQuoteItemEmailListForDelay() {
        QuoteItemEmailDTO quoteItemEmailDTO = new QuoteItemEmailDTO();
        quoteItemEmailDTO.setQuoteId(111L);
        quoteItemEmailDTO.setTerm("1 Year");
        quoteItemEmailDTO.setSla("Standard SLA");
        quoteItemEmailDTO.setServiceClass("WIC");
        quoteItemEmailDTO.setConnectionType("Etherway Diverse");
        quoteItemEmailDTO.setRecurringFrequency("Monthly");
        quoteItemEmailDTO.setLogicalLinkProfile("Primary (0% AF)");
        quoteItemEmailDTO.setLogicalLinkBandwidth("1000 Mb");
        quoteItemEmailDTO.setHandoff("Internet");
        quoteItemEmailDTO.setBandwidth("10 Gb");
        quoteItemEmailDTO.setAEndTargetAccessSupplier("3rd Party Supplier");
        quoteItemEmailDTO.setAEndEircode("D17XD78");
        quoteItemEmailDTO.setAEndAddress("HUGH JORDAN; CO. LIMITED, UNIT 4, CONSTELLATION ROAD, AIRWAYS INDUSTRIAL ESTATE, DUBLIN 17");
        quoteItemEmailDTO.setIpRange("31");
        quoteItemEmailDTO.setReason("Test delay reason");
        quoteItemEmailDTO.setStatus("Delayed");
        quoteItemEmailDTO.setGroup("G1");

        return Collections.singletonList(quoteItemEmailDTO);
    }

}
