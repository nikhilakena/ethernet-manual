package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.dto.QuoteItemEmailDTO;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

public class ContextFactory {

    public static Context getCompleteEmailContext() {
        Context context = new Context();

        context.setVariable("customer", "sky");
        context.setVariable("btReference", "BT-QBTDC-1");
        context.setVariable("quoteInitiatedDate", "25/05/2022");
        context.setVariable("quoteDate", "25/05/2022");
        context.setVariable("status", "Complete");
        context.setVariable("connectionType", "ETHERWAY_DIVERSE");

        List<QuoteItemEmailDTO> quoteItems = new ArrayList<>();
        QuoteItemEmailDTO quoteItem = QuoteItemEmailDTOFactory.completeQuoteItemEmailDTO();

        quoteItem.setTerm("5 Years");
        quoteItem.setBandwidth("10 Gb");
        quoteItem.setSla("Enhanced SLA");
        quoteItem.setAEndTargetAccessSupplier("BT On-Net");
        quoteItem.setHandoff("Equinix DB1 - Unit 4027 Citywest");
        quoteItem.setLogicalLinkBandwidth("500 Mb");
        quoteItem.setLogicalLinkProfile("Premium 100 (100% AF)");
        quoteItem.setRecurringFrequency("Annual");
        quoteItem.setConnectionType("ETHERWAY_DIVERSE");
        quoteItem.setGroup("G1");
        quoteItems.add(quoteItem);

        context.setVariable("quoteItems", quoteItems);

        return context;
    }

    public static Context getCompleteEmailContextNullMailProps() {
        Context context = new Context();

        context.setVariable("customer", "sky");
        context.setVariable("btReference", "BT-QBTDC-1");
        context.setVariable("quoteInitiatedDate", "25/05/2022");
        context.setVariable("quoteDate", "25/05/2022");
        context.setVariable("status", "Complete");
        context.setVariable("connectionType", "ETHERWAY_DIVERSE");

        List<QuoteItemEmailDTO> quoteItems = new ArrayList<>();
        QuoteItemEmailDTO quoteItem = QuoteItemEmailDTOFactory.completeQuoteItemEmailDTO();

        quoteItem.setTerm("1 Year");
        quoteItem.setBandwidth("Existing");
        quoteItem.setSla(null);
        quoteItem.setAEndTargetAccessSupplier("BT On-Net");
        quoteItem.setHandoff("Equinix DB1 - Unit 4027 Citywest");
        quoteItem.setLogicalLinkBandwidth(null);
        quoteItem.setLogicalLinkProfile(null);
        quoteItem.setRecurringFrequency(null);
        quoteItem.setGroup("G1");


        quoteItems.add(quoteItem);

        context.setVariable("quoteItems", quoteItems);

        return context;
    }

    public static Context getDelayEmailContext() {
        Context context = new Context();

        context.setVariable("customer", "sky");
        context.setVariable("btReference", "BT-QBTDC-1");
        context.setVariable("quoteInitiatedDate", "25/05/2022");
        context.setVariable("quoteDate", null);
        context.setVariable("status", "Delayed");
        context.setVariable("connectionType", "ETHERWAY_DIVERSE");

        List<QuoteItemEmailDTO> quoteItems = new ArrayList<>();
        QuoteItemEmailDTO quoteItem = QuoteItemEmailDTOFactory.delayQuoteItemEmailDTO();

        quoteItem.setTerm("5 Years");
        quoteItem.setBandwidth("10 Gb");
        quoteItem.setSla("Enhanced SLA");
        quoteItem.setAEndTargetAccessSupplier("BT On-Net");
        quoteItem.setHandoff("Equinix DB1 - Unit 4027 Citywest");
        quoteItem.setLogicalLinkBandwidth("500 Mb");
        quoteItem.setLogicalLinkProfile("Premium 100 (100% AF)");
        quoteItem.setRecurringFrequency("Annual");
        quoteItem.setGroup("G1");
        quoteItem.setConnectionType("ETHERWAY_DIVERSE");

        quoteItems.add(quoteItem);

        context.setVariable("quoteItems", quoteItems);

        return context;
    }
}
