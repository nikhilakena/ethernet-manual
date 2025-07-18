package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.domain.*;
import com.btireland.talos.ethernet.engine.enums.ActionFlag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class PbtdcFactory {

    public static Pbtdc defaultPbtdc() {
        return Pbtdc.builder()
                .logicalLink(LogicalLink.builder()
                        .circuitReference("Test Reference")
                        .serviceClass("BT_ETHERFLOW/VOICE")
                        .action(ActionFlag.P)
                        .serviceDetails(Arrays.asList(ServiceDetails.builder()
                                        .serviceName("CPE_WAN_IP")
                                        .serviceValue("89.188.167.55/31")
                                        .action(ActionFlag.P).build(),
                                ServiceDetails.builder()
                                        .serviceName("GATEWAY_IP")
                                        .serviceValue("89.188.167.90/31")
                                        .action(ActionFlag.P)
                                        .build()))
                        .bandWidth("5000")
                        .vlan("Sample Vlan")
                        .build())
                .customerAccess(Access.builder()
                        .changedAt(LocalDateTime.parse("2020-11-11T09:44:10.777317"))
                        .circuitReference("Test AEnd Reference")
                        .serviceClass("BT_ETHERWAY")
                        .action(ActionFlag.CH)
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("A4000032")
                                        .address(Address.builder()
                                                .locationLine1("Line 1")
                                                .locationLine2("Line 2")
                                                .locationLine3("Line 3")
                                                .city("City 1")
                                                .county("County 1")
                                                .build())
                                        .build())
                                .build())
                        .terminatingEquipment(TerminatingEquipment.builder()
                                .port("500")
                                .presentation("AEnd")
                                .portSetting("AEnd Config")
                                .portSpeed("100")
                                .build())
                        .build())
                .wholesalerAccess(Access.builder()
                        .changedAt(LocalDateTime.parse("2020-11-12T09:44:10.777317"))
                        .circuitReference("Test BEnd Reference")
                        .serviceClass("BT_ETHERWAY")
                        .action(ActionFlag.CH)
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("A4000032")
                                        .address(Address.builder()
                                                .locationLine1("Line 1")
                                                .locationLine2("Line 2")
                                                .locationLine3("Line 3")
                                                .city("City 2")
                                                .county("County 2")
                                                .build())
                                        .build())
                                .build())
                        .terminatingEquipment(TerminatingEquipment.builder()
                                .port("600")
                                .presentation("BEnd")
                                .portSetting("BEnd Config")
                                .portSpeed("200")
                                .build())
                        .build())
                .build();
    }

    public static Pbtdc initialPbtdcOrder() {
        return Pbtdc.builder()
                .createdAt(LocalDateTime.parse("2022-02-03T10:43:35"))
                .changedAt(LocalDateTime.parse("2022-02-03T10:43:35"))
                .wagOrderId(1L)
                .orderNumber("BT-PBTDC-123")
                .oao("string")
                .obo("string")
                .dataContract("WAG")
                .originatorCode("EXT")
                .resellerTransactionId("100")
                .resellerOrderRequestDateTime(LocalDateTime.parse("2022-02-03T10:43:35"))
                .applicationDate(LocalDate.parse("2022-02-03"))
                .operatorName("SKY")
                .operatorCode("83758")
                .serviceType("PBTDC")
                .accountNumber("string")
                .organisationName("ABC Limited")
                .refQuoteItemId(1L)
                .orderStatus("Created")
                .workflowStatus("PENDING VALIDATION")
                .interventionFlag(false)
                .contacts(Arrays.asList(Contact.builder()
                        .firstName("Alex")
                        .lastName("Cronin")
                        .number("3243535")
                        .email("alex@abc.com")
                        .role("Lead Delivery Manager")
                        .build()))
                .version(1)
                .build();
    }
}
