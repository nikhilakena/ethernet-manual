package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.dto.*;
import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import com.btireland.talos.ethernet.engine.enums.PowerSocketType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;


public class OrdersDTOFactory {

    public static OrdersDTO defaultOrdersDTO() {

        return OrdersDTO.builder()
                .orderId(445566L)
                .oao("sky")
                .agent("Test Agent")
                .agentEmail("test@test.com")
                .accountNumber("1234567890")
                .orderNumber("BT-PBTDC-328953")
                .supplierOrderId("ELIG_25000_42309853498539532")
                .operatorName("SKY")
                .operatorCode("83758")
                .organisationName("Test Organisation")
                .resellerTransactionId("123")
                .resellerOrderRequestDateTime("29/07/2020 10:38:17")
                .uniqueId("A123")
                .serviceClass("WEC")
                .connectionType("ETHERWAY_DIVERSE")
                .btGroupRef("BT-PBTDC-328953-G1")
                .statusNotes("This is a test Note")
                .internalTrackingOrderReference("TALOS-328953-PBTDC-1")
                .pbtdc(PbtdcDTO.builder()
                        .logicalLink(LogicalLinkDTO.builder()
                                .circuitReference("Test Reference")
                                .profile("PREMIUM_100")
                                .action(ActionFlag.P)
                                .serviceDetails(Arrays.asList(ServiceDetailsDTO.builder()
                                                .serviceName("CPE_WAN_IP")
                                                .serviceValue("89.188.167.55/31")
                                                .action(ActionFlag.P).build(),
                                        ServiceDetailsDTO.builder()
                                                .serviceName("GATEWAY_IP")
                                                .serviceValue("89.188.167.90/31")
                                                .action(ActionFlag.P)
                                                .build()))
                                .bandWidth("900")
                                .vlan("1067")
                                .build())
                        .customerAccess(AccessDTO.builder()
                                .changedAt(LocalDateTime.parse("2020-11-11T09:44:10.777317"))
                                .circuitReference("Test AEnd Reference")
                                .serviceClass("BT_ETHERWAY")
                                .action(ActionFlag.CH)
                                .site(SiteDTO.builder()
                                        .location(LocationDTO.builder()
                                                .id("A4000032")
                                                .address(AddressDTO.builder()
                                                        .locationLine1("Line 1")
                                                        .locationLine2("Line 2")
                                                        .locationLine3("Line 3")
                                                        .city("City 1")
                                                        .county("County 1")
                                                        .fullAddress("3 Mourne View, Dublin Road, Dundalk, Co. Louth")
                                                        .build())
                                                .build())
                                        .commsRoom(COMMSRoomDTO.builder()
                                                .details("tst1_180422_1067_CR")
                                                .powerSocketType(PowerSocketType.fromString("3-Pin"))
                                                .build())
                                        .build())
                                .terminatingEquipment(TerminatingEquipmentDTO.builder()
                                        .port("500")
                                        .presentation("LC 1310 Single Mode Fibre")
                                        .portSetting("AEnd Config")
                                        .portSpeed("100")
                                        .cableManager(true)
                                        .build())
                                .accessInstall(AccessInstallDTO.builder()
                                        .contacts(new HashSet<ContactDTO>(Arrays.asList(ContactDTO.builder()
                                                .role("Main")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .number("9999999999")
                                                .email("test1@test.com").build(),
                                                ContactDTO.builder()
                                                        .role("Secondary")
                                                        .firstName("First Name")
                                                        .lastName("Last Name")
                                                        .number("8888888888")
                                                        .email("test2@test.com").build(),
                                                ContactDTO.builder()
                                                        .role("Landlord")
                                                        .firstName("Landlord First Name")
                                                        .lastName("Landlord Last Name")
                                                        .number("7777777777")
                                                        .email("test3@test.com").build(),
                                                ContactDTO.builder()
                                                        .role("Building Manager")
                                                        .firstName("BM First Name")
                                                        .lastName("BM Last Name")
                                                        .number("6666666666")
                                                        .email("test4@test.com").build())))
                                        .siteMethodInsuranceCert(true)
                                        .siteInduction(null).build())
                                .bandWidth("1")
                                .sla("Standard")
                                .build())
                        .wholesalerAccess(AccessDTO.builder()
                                .changedAt(LocalDateTime.parse("2020-11-12T09:44:10.777317"))
                                .circuitReference("tst10672")
                                .serviceClass("BT_ETHERWAY")
                                .action(ActionFlag.CH)
                                .site(SiteDTO.builder()
                                        .location(LocationDTO.builder()
                                                .id("BT Citywest")
                                                .address(AddressDTO.builder()
                                                        .locationLine1("Line 1")
                                                        .locationLine2("Line 2")
                                                        .locationLine3("Line 3")
                                                        .city("City 2")
                                                        .county("County 2")
                                                        .build())
                                                .build())
                                        .build())
                                .terminatingEquipment(TerminatingEquipmentDTO.builder()
                                        .port("600")
                                        .presentation("BEnd")
                                        .portSetting("BEnd Config")
                                        .portSpeed("200")
                                        .build())
                                .build())
                        .quote(QuoteDTO.builder()
                                .quoteItemId(1067L)
                                .term(1)
                                .orderNumber("BT-QBTDC-328917")
                                .projectKey("NUA_C_1404")
                                .aendTargetAccessSupplier("OFF-NET")
                                .nonRecurringPrice("1,000")
                                .recurringPrice("8,354")
                                .recurringFrequency("ANNUAL")
                                .ipRange(31)
                                .build())
                        .build()).build();
    }

    public static OrdersDTO ordersDTOWithHandoverCode() {
        OrdersDTO ordersDTO = defaultOrdersDTO();
        ordersDTO.getPbtdc().getWholesalerAccess().getSite().getLocation().setId("BT_CITYWEST");
        return ordersDTO;
    }

    public static OrdersDTO defaultWICOrdersDTO(){
        OrdersDTO ordersDTO = defaultOrdersDTO();
        ordersDTO.setServiceClass("WIC");
        ordersDTO.getPbtdc().getCustomerAccess().setBandWidth(null);
        return ordersDTO;
    }
}
