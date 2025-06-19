package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.dto.QuoteItemEmailDTO;

public class QuoteItemEmailDTOFactory {

    public static QuoteItemEmailDTO completeQuoteItemEmailDTO(){
        QuoteItemEmailDTO quoteItemEmailDTO = new QuoteItemEmailDTO();

        quoteItemEmailDTO.setQuoteId(1L);
        quoteItemEmailDTO.setStatus("Quoted");
        quoteItemEmailDTO.setServiceClass("WEC");
        quoteItemEmailDTO.setConnectionType("ETHERWAY_DIVERSE");
        quoteItemEmailDTO.setTerm("5");
        quoteItemEmailDTO.setAEndEircode("A00F027");
        quoteItemEmailDTO.setAEndAddress("DUNBEGGAN, AUGHNACLIFFE, LONGFORD");
        quoteItemEmailDTO.setBandwidth("10");
        quoteItemEmailDTO.setSla("ENHANCED");
        quoteItemEmailDTO.setAEndTargetAccessSupplier("ON-NET");
        quoteItemEmailDTO.setHandoff("EQUINIX_DB1");
        quoteItemEmailDTO.setLogicalLinkBandwidth("500");
        quoteItemEmailDTO.setLogicalLinkProfile("PREMIUM_100");
        quoteItemEmailDTO.setNonRecurringPrice("2000");
        quoteItemEmailDTO.setRecurringPrice("100");
        quoteItemEmailDTO.setRecurringFrequency("ANNUAL");
        quoteItemEmailDTO.setReason("Quoted Successfully");

        return quoteItemEmailDTO;
    }

    public static QuoteItemEmailDTO completeQuoteItemEmailDTONullMailBodyProps(){
        QuoteItemEmailDTO quoteItemEmailDTO = new QuoteItemEmailDTO();

        quoteItemEmailDTO.setQuoteId(1L);
        quoteItemEmailDTO.setStatus("Quoted");
        quoteItemEmailDTO.setServiceClass("WEC");
        quoteItemEmailDTO.setConnectionType("ETHERWAY_DIVERSE");
        quoteItemEmailDTO.setTerm("1");
        quoteItemEmailDTO.setAEndEircode("A00F027");
        quoteItemEmailDTO.setAEndAddress("DUNBEGGAN, AUGHNACLIFFE, LONGFORD");
        quoteItemEmailDTO.setSla("ENHANCED");
        quoteItemEmailDTO.setAEndTargetAccessSupplier("ON-NET");
        quoteItemEmailDTO.setHandoff("EQUINIX_DB1");
        quoteItemEmailDTO.setNonRecurringPrice("2000");
        quoteItemEmailDTO.setRecurringPrice("100");
        quoteItemEmailDTO.setReason("Quoted Successfully");

        return quoteItemEmailDTO;
    }

    public static QuoteItemEmailDTO delayQuoteItemEmailDTO(){
        QuoteItemEmailDTO quoteItemEmailDTO = new QuoteItemEmailDTO();

        quoteItemEmailDTO.setQuoteId(1L);
        quoteItemEmailDTO.setStatus("Delayed");
        quoteItemEmailDTO.setServiceClass("WEC");
        quoteItemEmailDTO.setConnectionType("ETHERWAY_DIVERSE");
        quoteItemEmailDTO.setTerm("5");
        quoteItemEmailDTO.setAEndEircode("A00F027");
        quoteItemEmailDTO.setAEndAddress("DUNBEGGAN, AUGHNACLIFFE, LONGFORD");
        quoteItemEmailDTO.setBandwidth("10");
        quoteItemEmailDTO.setSla("ENHANCED");
        quoteItemEmailDTO.setAEndTargetAccessSupplier("ON-NET");
        quoteItemEmailDTO.setHandoff("EQUINIX_DB1");
        quoteItemEmailDTO.setLogicalLinkBandwidth("500");
        quoteItemEmailDTO.setLogicalLinkProfile("PREMIUM_100");
        quoteItemEmailDTO.setRecurringFrequency("ANNUAL");
        quoteItemEmailDTO.setReason("Delayed due to customer");

        return quoteItemEmailDTO;
    }
}
