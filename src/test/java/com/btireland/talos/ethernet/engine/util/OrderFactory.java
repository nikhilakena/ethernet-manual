package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.domain.*;
import com.btireland.talos.ethernet.engine.enums.ActionFlag;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class OrderFactory {
    /*public static Pbtdc defaultPbtdcOrders() {
        return Pbtdc.builder()
                .wagOrderId(123L)
                .id(2L)
                .refQuoteItemId(1L)
                .orderNumber("BT-PBTDC-123")
                .internalTrackingOrderReference("TALOS-123-PBTDC-1")
                .oao("sky")
                .serviceType("PBTDC")
                .businessStatus(PBTDCBusinessStatus.builder().build())
                .customerAccess(Access.builder().site(Site.builder().build()).build())
                .build();
    }*/

    public static Pbtdc defaultPbtdcOrders() {
        return Pbtdc.builder()
                .wagOrderId(123L)
                .id(2L)
                .refQuoteItemId(1L)
                .orderNumber("BT-PBTDC-123")
                .internalTrackingOrderReference("TALOS-123-PBTDC-1")
                .oao("sky")
                .serviceType("PBTDC")
                .businessStatus(PBTDCBusinessStatus.builder().build())
                .customerAccess(Access.builder().build())
                .build();
    }


    public static Pbtdc ordersWithRejectDetails() {
        return Pbtdc.builder()
                .wagOrderId(123L)
                .id(2L)
                .orderNumber("BT-PBTDC-123")
                .internalTrackingOrderReference("TALOS-123-PBTDC-1")
                .resellerTransactionId("100")
                .dataContract("WAG")
                .originatorCode("EXT")
                .lastNotificationType("U")
                .resellerOrderRequestDateTime(LocalDateTime.of(2022, Month.FEBRUARY,3,10,43,55))
                .createdAt(LocalDateTime.of(2022, Month.FEBRUARY,3,15,10,56))
                .operatorName("SKY")
                .operatorCode("83758")
                .oao("sky")
                .supplierOrder(SupplierOrder.builder()
                        .siebelNumber("1-EY1JVR")
                        .orderNumber("TALOS-123-PBTDC-1")
                        .build())
                .serviceType("PBTDC")
                .rejectionDetails(RejectionDetails.builder().rejectCode(BTRejectCode.REJECT_CODE_105.getRejectCode()).rejectReason("Cancellation").build())
                .businessStatus(PBTDCBusinessStatus.builder().build())
                .build();
    }

    public static Pbtdc orderWithAccessInstall() {
        List<Contact> contacts = Arrays.asList(Contact.builder().firstName("xyz").role("Main").build(),
                Contact.builder().firstName("xyz").role("Landlord").build(),
                Contact.builder().firstName("xyz").role("Building Manager").build(),
                Contact.builder().firstName("xyz").role("Secondary").build()
        );
        Pbtdc pbtdcOrders = defaultPbtdcOrders();
        pbtdcOrders.setCustomerAccess(Access.builder()
                .accessInstall(AccessInstall.builder()
                        .contacts(contacts)
                        .build())
                .build());
        return pbtdcOrders;
    }

    public static Pbtdc enrichedPbtdcOrdersWithQuoteItem() {
        return Pbtdc.builder()
                .wagOrderId(123L)
                .id(2L)
                .refQuoteItemId(1L)
                .serviceClass("WEC")
                .orderNumber("BT-PBTDC-123")
                .internalTrackingOrderReference("TALOS-123-PBTDC-1")
                .oao("sky")
                .serviceType("PBTDC")
                .businessStatus(PBTDCBusinessStatus.builder().build())
                .customerAccess(Access.builder()
                        .bandWidth("10")
                        .sla("ENHANCED")
                        .site(Site.builder().location(Location.builder().type("EIRCODE").id("A00F027")
                                .address(Address.builder().fullAddress("BERKLEY RECRUITMENT (GROUP) LTD., UNIT 3, WATERGOLD BUILDING, DOUGLAS, CORK").build())
                                .build()).build())
                        .build())
                .logicalLink(LogicalLink.builder()
                        .action(ActionFlag.P)
                        .profile("PREMIUM_100")
                        .bandWidth("30")
                        .build())
                .wholesalerAccess(Access.builder()
                        .action(ActionFlag.CH)
                        .site(Site.builder().location(Location.builder().type("HANDOVER").id("EQUINIX_DB1").build()).build())
                        .build())
                .quote(Quote.builder()
                        .quoteItemId(1L)
                        .term(2)
                        .ipRange(500)
                        .logicalBandwidth("30")
                        .recurringPrice("4379.2")
                        .nonRecurringPrice("1000")
                        .recurringFrequency("MONTHLY")
                        .orderNumber("BT-QBTDC-1234")
                        .projectKey("xyz")
                        .aendTargetAccessSupplier("ON-NET")
                        .build())
                .build();
    }

    public static RejectionDetails rejectionDetailsForDuplicateOrder(){
        return RejectionDetails.builder()
                .orders(defaultPbtdcOrders())
                .rejectCode("760")
                .rejectReason("Active Order already in progress for the Quote Item Reference ID")
                .build();

    }

    public static RejectionDetails rejectionDetailsForInvalidQuote(){
        return RejectionDetails.builder()
                .orders(defaultPbtdcOrders())
                .rejectCode("759")
                .rejectReason("Invalid Quote Item reference")
                .build();

    }

    public static RejectionDetails rejectionDetailsForInvalidBtGroupRef(){
        return RejectionDetails.builder()
                .orders(defaultPbtdcOrders())
                .rejectCode("759")
                .rejectReason("Invalid BT Group Ref")
                .build();
    }

    public static Pbtdc ordersWithOrderManagerDetails() {
        return Pbtdc.builder()
                .wagOrderId(123L)
                .id(2L)
                .orderNumber("BT-PBTDC-123")
                .internalTrackingOrderReference("TALOS-123-PBTDC-1")
                .resellerTransactionId("100")
                .dataContract("WAG")
                .originatorCode("EXT")
                .lastNotificationType("U")
                .resellerOrderRequestDateTime(LocalDateTime.of(2022, Month.FEBRUARY,3,10,43,55))
                .createdAt(LocalDateTime.of(2022, Month.FEBRUARY,3,15,10,56))
                .operatorName("SKY")
                .operatorCode("83758")
                .oao("sky")
                .supplierOrder(SupplierOrder.builder()
                        .siebelNumber("1-EY1JVR")
                        .orderNumber("TALOS-123-PBTDC-1")
                        .build())
                .serviceType("PBTDC")
                .contacts(Arrays.asList(Contact.builder()
                        .firstName("first")
                        .lastName("last")
                        .email("test@test.com")
                        .role("Order Manager").build()))
                .businessStatus(PBTDCBusinessStatus.builder().build())
                .build();
    }

    public static Pbtdc ordersWithCustomerDelayDetails() {
        return Pbtdc.builder()
                .wagOrderId(123L)
                .id(2L)
                .orderNumber("BT-PBTDC-123")
                .internalTrackingOrderReference("TALOS-123-PBTDC-1")
                .resellerTransactionId("100")
                .dataContract("WAG")
                .originatorCode("EXT")
                .lastNotificationType("U")
                .resellerOrderRequestDateTime(LocalDateTime.of(2022, Month.FEBRUARY,3,10,43,55))
                .createdAt(LocalDateTime.of(2022, Month.FEBRUARY,3,15,10,56))
                .operatorName("SKY")
                .operatorCode("83758")
                .oao("sky")
                .supplierOrder(SupplierOrder.builder()
                        .siebelNumber("1-EY1JVR")
                        .orderNumber("TALOS-123-PBTDC-1")
                        .build())
                .serviceType("PBTDC")
                .customerDelay(CustomerDelay.builder()
                        .startDate(LocalDateTime.parse("2020-11-11T09:44:10.777317"))
                        .endDate(LocalDateTime.parse("2020-11-12T09:44:10.777317"))
                        .reason("Test Reason").build())
                .businessStatus(PBTDCBusinessStatus.builder().build())
                .build();
    }


}
