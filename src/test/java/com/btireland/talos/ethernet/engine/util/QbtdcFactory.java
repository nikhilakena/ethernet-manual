package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.domain.*;
import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QbtdcFactory {

    public static Qbtdc defaultQbtdcOrder() {
        return Qbtdc.builder()
                .createdAt(LocalDateTime.parse("2022-05-25T05:17:28"))
                .changedAt(LocalDateTime.parse("2022-05-25T05:17:28"))
                .wagOrderId(1L)
                .orderNumber("BT-QBTDC-1")
                .oao("sky")
                .obo("sky")
                .dataContract("WAG")
                .originatorCode("EXT")
                .resellerTransactionId("10101")
                .resellerOrderRequestDateTime(LocalDateTime.parse("2022-05-25T05:17:28"))
                .applicationDate(LocalDate.parse("2022-05-25"))
                .operatorName("SKY")
                .operatorCode("83758")
                .serviceType("QBTDC")
                .projectKey("xyz")
                .syncFlag("Y")
                .recurringFrequency("ANNUAL")
                .serviceClass("WEC")
                .orderStatus("Created")
                .orderStatus("Created")
                .workflowStatus("PENDING VALIDATION")
                .version(1)
                .quoteItems(List.of(quoteItem()))
                .build();

    }

    public static QbtdcQuote quoteItem() {
        return QbtdcQuote.builder()
                .term(5)
                .logicalLink(LogicalLink.builder()
                        .ipRange(31)
                        .action(ActionFlag.P)
                        .bandWidth("500")
                        .profile("PREMIUM_100")
                        .build())
                .customerAccess(Access.builder()
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("A00F027")
                                        .type("EIRCODE")
                                        .build())
                                .build())
                        .action(ActionFlag.P)
                        .supplier("ON-NET")
                        .bandWidth("10")
                        .sla("ENHANCED")
                        .build())
                .wholesalerAccess(Access.builder()
                        .action(ActionFlag.CH)
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("EQUINIX_DB1")
                                        .type("HANDOVER")
                                        .build())
                                .build())
                        .build())
                .build();

    }

    public static List<QbtdcQuote> quoteItemsWithStatusAsRejected() {
        List<QbtdcQuote> qbtdcQuoteList = new ArrayList<>();
        qbtdcQuoteList.add(QbtdcQuote.builder()
                .wagQuoteItemId(1L)
                .term(5)
                .status("Rejected")
                .group("G1")
                .logicalLink(LogicalLink.builder()
                        .ipRange(31)
                        .action(ActionFlag.P)
                        .bandWidth("500")
                        .profile("PREMIUM_100")
                        .build())
                .customerAccess(Access.builder()
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("A00F027")
                                        .type("EIRCODE")
                                        .build())
                                .build())
                        .action(ActionFlag.P)
                        .supplier("ON-NET")
                        .bandWidth("10")
                        .sla("ENHANCED")
                        .build())
                .wholesalerAccess(Access.builder()
                        .action(ActionFlag.CH)
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("EQUINIX_DB1")
                                        .type("HANDOVER")
                                        .build())
                                .build())
                        .build())
                .build());

        qbtdcQuoteList.add(QbtdcQuote.builder()
                .wagQuoteItemId(2L)
                .term(3)
                .status("Rejected")
                .group("G1")
                .logicalLink(LogicalLink.builder()
                        .ipRange(31)
                        .action(ActionFlag.P)
                        .bandWidth("300")
                        .profile("PREMIUM_100")
                        .build())
                .customerAccess(Access.builder()
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("A00F027")
                                        .type("EIRCODE")
                                        .build())
                                .build())
                        .action(ActionFlag.P)
                        .supplier("ON-NET")
                        .bandWidth("5")
                        .sla("ENHANCED")
                        .targetAccessSupplier("ON-NET")
                        .build())
                .wholesalerAccess(Access.builder()
                        .action(ActionFlag.CH)
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("EQUINIX_DB1")
                                        .type("HANDOVER")
                                        .build())
                                .build())
                        .targetAccessSupplier("ON-NET")
                        .build())
                .build());

        return qbtdcQuoteList;
    }

    public static Qbtdc qbtdcOrderWithRejectedQuoteItems() {
        return Qbtdc.builder()
                .orderNumber("BT-QBTDC-1")
                .obo("sky")
                .projectKey("xyz")
                .syncFlag("Y")
                .recurringFrequency("ANNUAL")
                .serviceClass("WEC")
                .quoteItems(quoteItemsWithStatusAsRejected())
                .build();

    }

        public static Qbtdc qbtdcForPricingEngine() {
        return Qbtdc.builder()
                .orderNumber("BT-QBTDC-1")
                .obo("sky")
                .projectKey("xyz")
                .syncFlag("Y")
                .recurringFrequency("ANNUAL")
                .serviceClass("WEC")
                .quoteItems(quoteItemsForPricingEngine())
                .build();

    }

    public static List<QbtdcQuote> quoteItemsForPricingEngine() {
        List<QbtdcQuote> qbtdcQuoteList = new ArrayList<>();
        qbtdcQuoteList.add(QbtdcQuote.builder()
                .wagQuoteItemId(1L)
                .term(5)
                .logicalLink(LogicalLink.builder()
                        .ipRange(31)
                        .action(ActionFlag.P)
                        .bandWidth("500")
                        .profile("PREMIUM_100")
                        .build())
                .customerAccess(Access.builder()
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("A00F027")
                                        .type("EIRCODE")
                                        .build())
                                .build())
                        .action(ActionFlag.P)
                        .supplier("ON-NET")
                        .bandWidth("10")
                        .sla("ENHANCED")
                        .build())
                .wholesalerAccess(Access.builder()
                        .action(ActionFlag.CH)
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("EQUINIX_DB1")
                                        .type("HANDOVER")
                                        .build())
                                .build())
                        .build())
                .build());

        qbtdcQuoteList.add(QbtdcQuote.builder()
                .wagQuoteItemId(2L)
                .term(3)
                .logicalLink(LogicalLink.builder()
                        .ipRange(31)
                        .action(ActionFlag.P)
                        .bandWidth("300")
                        .profile("PREMIUM_100")
                        .build())
                .customerAccess(Access.builder()
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("A00F027")
                                        .type("EIRCODE")
                                        .build())
                                .build())
                        .action(ActionFlag.P)
                        .supplier("ON-NET")
                        .bandWidth("5")
                        .sla("ENHANCED")
                        .targetAccessSupplier("ON-NET")
                        .build())
                .wholesalerAccess(Access.builder()
                        .action(ActionFlag.CH)
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("EQUINIX_DB1")
                                        .type("HANDOVER")
                                        .build())
                                .build())
                        .targetAccessSupplier("ON-NET")
                        .build())
                .build());

        return qbtdcQuoteList;
    }

    public static Qbtdc defaultQbtdcOrderForAsyncCompletion() {
        return Qbtdc.builder()
                .createdAt(LocalDateTime.parse("2022-05-25T05:17:28"))
                .changedAt(LocalDateTime.parse("2022-05-25T05:17:28"))
                .wagOrderId(1L)
                .orderNumber("BT-QBTDC-1")
                .oao("sky")
                .obo("sky")
                .dataContract("WAG")
                .originatorCode("EXT")
                .resellerTransactionId("10101")
                .resellerOrderRequestDateTime(LocalDateTime.parse("2022-05-25T05:17:28"))
                .applicationDate(LocalDate.parse("2022-05-25"))
                .operatorName("SKY")
                .operatorCode("83758")
                .serviceType("QBTDC")
                .projectKey("xyz")
                .syncFlag("Y")
                .recurringFrequency("ANNUAL")
                .serviceClass("WEC")
                .connectionType("ETHERWAY_DIVERSE")
                .orderStatus("Created")
                .orderStatus("Created")
                .workflowStatus("PENDING VALIDATION")
                .version(1)
                .quoteItems(List.of(quoteItemForAsyncCompletion()))
                .build();

    }

    public static QbtdcQuote quoteItemForAsyncCompletion() {
        return QbtdcQuote.builder()
                .term(5)
                .logicalLink(LogicalLink.builder()
                        .ipRange(31)
                        .action(ActionFlag.P)
                        .bandWidth("500")
                        .profile("PREMIUM_100")
                        .build())
                .customerAccess(Access.builder()
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("A00F027")
                                        .type("EIRCODE")
                                        .build())
                                .build())
                        .action(ActionFlag.P)
                        .supplier("ON-NET")
                        .bandWidth("10")
                        .sla("ENHANCED")
                        .build())
                .wholesalerAccess(Access.builder()
                        .action(ActionFlag.CH)
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("EQUINIX_DB1")
                                        .type("HANDOVER")
                                        .build())
                                .build())
                        .build())
                .offlineQuoted(true)
                .group("G1")
                .build();

    }

    public static Qbtdc qbtdcOrderAutoCompleteEmail() {
        Qbtdc qbtdc = defaultQbtdcOrderForAsyncCompletion();

        QbtdcEmailDetails qbtdcEmailDetails = new QbtdcEmailDetails(qbtdc, "joe.bloggs@bt.com");
        List<QbtdcEmailDetails> qbtdcEmailDetailsList = new ArrayList<>();
        qbtdcEmailDetailsList.add(qbtdcEmailDetails);

        qbtdc.setQbtdcEmailDetailsList(qbtdcEmailDetailsList);
        qbtdc.setLastNotificationType("C");

        Long quoteItemId = 1L;
        Long wagQuoteItemId = 1L;
        for(QbtdcQuote quoteItem: qbtdc.getQuoteItems()) {
            quoteItem.setQuoteItemId(quoteItemId);
            quoteItem.setWagQuoteItemId(wagQuoteItemId);
            quoteItem.setStatus("Quoted");
            quoteItem.setRecurringPrice("100");
            quoteItem.setNonRecurringPrice("2000");
            quoteItem.getCustomerAccess().setTargetAccessSupplier("ON-NET");
            quoteItem.getCustomerAccess().getSite().getLocation().setAddress(Address.builder().fullAddress("DUNBEGGAN, AUGHNACLIFFE, LONGFORD").build());
            quoteItem.setNotes("Quoted Successfully");
            quoteItemId++;
        }

        return qbtdc;
    }

    public static Qbtdc defaultQbtdcOrderForAsyncCompletionSingleLine() {
        return Qbtdc.builder()
                .createdAt(LocalDateTime.parse("2022-05-25T05:17:28"))
                .changedAt(LocalDateTime.parse("2022-05-25T05:17:28"))
                .wagOrderId(1L)
                .orderNumber("BT-QBTDC-1")
                .oao("sky")
                .obo("sky")
                .dataContract("WAG")
                .originatorCode("EXT")
                .resellerTransactionId("10101")
                .resellerOrderRequestDateTime(LocalDateTime.parse("2022-05-25T05:17:28"))
                .applicationDate(LocalDate.parse("2022-05-25"))
                .operatorName("SKY")
                .operatorCode("83758")
                .serviceType("QBTDC")
                .projectKey("xyz")
                .syncFlag("Y")
                .recurringFrequency("ANNUAL")
                .serviceClass("WEC")
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                .orderStatus("Created")
                .orderStatus("Created")
                .workflowStatus("PENDING VALIDATION")
                .version(1)
                .quoteItems(List.of(quoteItemForAsyncCompletion()))
                .build();

    }

    public static Qbtdc qbtdcOrderAutoCompleteForSingleLineEmail() {
        Qbtdc qbtdc = defaultQbtdcOrderForAsyncCompletionSingleLine();

        QbtdcEmailDetails qbtdcEmailDetails = new QbtdcEmailDetails(qbtdc, "joe.bloggs@bt.com");
        List<QbtdcEmailDetails> qbtdcEmailDetailsList = new ArrayList<>();
        qbtdcEmailDetailsList.add(qbtdcEmailDetails);

        qbtdc.setQbtdcEmailDetailsList(qbtdcEmailDetailsList);
        qbtdc.setLastNotificationType("C");

        Long quoteItemId = 1L;
        Long wagQuoteItemId = 1L;
        for(QbtdcQuote quoteItem: qbtdc.getQuoteItems()) {
            quoteItem.setQuoteItemId(quoteItemId);
            quoteItem.setWagQuoteItemId(wagQuoteItemId);
            quoteItem.setStatus("Quoted");
            quoteItem.setRecurringPrice("100");
            quoteItem.setNonRecurringPrice("2000");
            quoteItem.getCustomerAccess().setTargetAccessSupplier("ON-NET");
            quoteItem.getCustomerAccess().getSite().getLocation().setAddress(Address.builder().fullAddress("DUNBEGGAN, AUGHNACLIFFE, LONGFORD").build());
            quoteItem.setNotes("Quoted Successfully");
            quoteItemId++;
        }

        return qbtdc;
    }

    public static Qbtdc qbtdcOrderAutoDelayEmail() {
        Qbtdc qbtdc = defaultQbtdcOrderForAsyncCompletion();

        QbtdcEmailDetails qbtdcEmailDetails = new QbtdcEmailDetails(qbtdc, "joe.bloggs@bt.com");
        List<QbtdcEmailDetails> qbtdcEmailDetailsList = new ArrayList<>();
        qbtdcEmailDetailsList.add(qbtdcEmailDetails);

        qbtdc.setQbtdcEmailDetailsList(qbtdcEmailDetailsList);
        qbtdc.setLastNotificationType("D");
        qbtdc.setDelayReason("Delayed due to customer");

        Long quoteItemId = 1L;
        Long wagQuoteItemId = 1L;
        for(QbtdcQuote quoteItem: qbtdc.getQuoteItems()) {
            quoteItem.setQuoteItemId(quoteItemId);
            quoteItem.setWagQuoteItemId(wagQuoteItemId);
            quoteItem.setStatus("Delayed");
            quoteItem.getCustomerAccess().setTargetAccessSupplier("ON-NET");
            quoteItem.getCustomerAccess().getSite().getLocation().setAddress(Address.builder().fullAddress("DUNBEGGAN, AUGHNACLIFFE, LONGFORD").build());
            quoteItemId++;
        }

        return qbtdc;
    }

    public static Qbtdc qbtdcOrderAutoCompleteEmailNullMailBodyProps() {
        Qbtdc qbtdc = defaultQbtdcOrderForAsyncCompletion();

        QbtdcEmailDetails qbtdcEmailDetails = new QbtdcEmailDetails(qbtdc, "joe.bloggs@bt.com");
        List<QbtdcEmailDetails> qbtdcEmailDetailsList = new ArrayList<>();
        qbtdcEmailDetailsList.add(qbtdcEmailDetails);

        qbtdc.setQbtdcEmailDetailsList(qbtdcEmailDetailsList);
        qbtdc.setLastNotificationType("C");
        qbtdc.setRecurringFrequency(null);

        Long quoteItemId = 1L;
        Long wagQuoteItemId = 1L;
        for(QbtdcQuote quoteItem: qbtdc.getQuoteItems()) {
            quoteItem.setTerm(1);
            quoteItem.getCustomerAccess().setBandWidth(null);
            quoteItem.getLogicalLink().setBandWidth(null);
            quoteItem.getLogicalLink().setProfile(null);
            quoteItem.setQuoteItemId(quoteItemId);
            quoteItem.setWagQuoteItemId(wagQuoteItemId);
            quoteItem.setStatus("Quoted");
            quoteItem.setRecurringPrice("100");
            quoteItem.setNonRecurringPrice("2000");
            quoteItem.getCustomerAccess().setTargetAccessSupplier("ON-NET");
            quoteItem.getCustomerAccess().getSite().getLocation().setAddress(Address.builder().fullAddress("DUNBEGGAN, AUGHNACLIFFE, LONGFORD").build());
            quoteItem.setNotes("Quoted Successfully");
            quoteItemId++;
        }

        return qbtdc;
    }

    public static Qbtdc qbtdcOrderForEmailAll() {
        QbtdcQuote qbtdcQuote = quoteItemsWithStatusAsRejected().get(0);
        qbtdcQuote.setRejectionDetails(RejectionDetails.builder().rejectReason("Cannot be Quoted").build());
        return Qbtdc.builder()
                .orderNumber("BT-QBTDC-1")
                .oao("sky")
                .obo("sky")
                .projectKey("xyz")
                .syncFlag("Y")
                .recurringFrequency("ANNUAL")
                .serviceClass("WEC")
                .connectionType("ETHERWAY_DIVERSE")
                .createdAt(LocalDateTime.parse("2022-05-25T05:17:28"))
                .changedAt(LocalDateTime.parse("2022-05-25T05:17:28"))
                .lastNotificationType("C")
                .quoteItems(List.of(qbtdcQuote))
                .build();

    }
}
