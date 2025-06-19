package com.btireland.talos.ethernet.engine.domain;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.util.ContactTypes;
import com.btireland.talos.ethernet.engine.util.OrderFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UnitTest
public class PbtdcOrderTest {
    private static List<Contact> defaultContacts = List.of(
                Contact.builder()
                        .firstName("Bob")
                        .lastName("Jones")
                        .number("5678")
                        .email("bobjones@company.com")
                        .role("Order Manager")
                        .build(),
                Contact.builder()
                        .firstName("John")
                        .lastName("Smith")
                        .number("1234")
                        .email("johnsmith@company.com")
                        .role("Lead Delivery Manager")
                        .build(),
                Contact.builder()
                        .firstName("Jim")
                        .lastName("McGill")
                        .number("3333")
                        .email("jimmymcgill@company.com")
                        .role("Landlord")
                        .build(),
                Contact.builder()
                        .firstName("Harold")
                        .lastName("Hamlyn")
                        .number("4444")
                        .email("harold@hhm.com")
                        .role("Main")
                        .build(),
                Contact.builder()
                    .firstName("Mike")
                    .lastName("Roberts")
                    .number("6666")
                    .email("mike@roberts.com")
                    .role("Building Manager")
                    .build()
        );

    @Test
    @DisplayName("test getLeadDeliveryManager() returns null when uninitialized")
    void testGetLeadDeliveryManagerReturnsNull(){
        Pbtdc pbtdc = Pbtdc.builder().build();
        Assertions.assertThat(pbtdc.getLeadDeliveryManager()).isNull();
    }

    @Test
    @DisplayName("test getLeadDeliveryManager() returns the lead delivery manager contact")
    void testGetLeadDeliveryManagerReturnsExpectedContact(){
        Contact leadDeliveryManagerContact = Contact.builder()
                .firstName("John")
                .lastName("Smith")
                .number("1234")
                .email("johnsmith@company.com")
                .role("Lead Delivery Manager")
                .build();

        Pbtdc pbtdc = Pbtdc.builder()
                .contacts(defaultContacts)
                .build();

        Assertions.assertThat(pbtdc.getLeadDeliveryManager()).usingRecursiveComparison().isEqualTo(leadDeliveryManagerContact);
    }

    @Test
    @DisplayName("test getOrderManager() returns null when uninitialized")
    void testGetOrderManagerReturnsNull(){
        Pbtdc pbtdc = Pbtdc.builder().build();
        Assertions.assertThat(pbtdc.getOrderManager()).isNull();
    }

    @Test
    @DisplayName("test getOrderManager() returns the expected order manager contact")
    void testGetOrderManagerReturnsExpectedContact(){
        Contact contact = Contact.builder()
                .firstName("Bob")
                .lastName("Jones")
                .number("5678")
                .email("bobjones@company.com")
                .role("Order Manager")
                .build();

        Pbtdc pbtdc = Pbtdc.builder()
                .contacts(defaultContacts)
                .build();

        Assertions.assertThat(pbtdc.getOrderManager()).usingRecursiveComparison().isEqualTo(contact);
    }


    @Test
    @DisplayName("test getOrderManagerName returns empty string when null")
    void testGetOrderManagerNameStringWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder().build();
        Assertions.assertThat(pbtdc.getOrderManagerNameAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getOrderManagerName returns expected string when lastname is null")
    void testGetOrderManagerNameStringWhenFirstNameOnlyPopulated(){
        Contact orderManagerContact = Contact.builder()
                .firstName("Bob")
                .role("Order Manager")
                .build();

        Pbtdc pbtdc = Pbtdc.builder()
                .contacts(List.of(orderManagerContact))
                .build();

        Assertions.assertThat(pbtdc.getOrderManagerNameAsString()).isEqualTo("Bob ");
    }
    @Test
    @DisplayName("test getOrderManagerName returns expected string")
    void testGetOrderManagerNameStringWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .contacts(defaultContacts)
                .build();
        Assertions.assertThat(pbtdc.getOrderManagerNameAsString()).isEqualTo("Bob Jones");
    }

    @Test
    @DisplayName("test getOrderManagerEmail returns empty string when null")
    void testGetOrderManagerEmailString(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getOrderManagerEmailAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getOrderManagerEmail returns expected string")
    void testGetOrderManagerEmailWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .contacts(defaultContacts)
                .build();
        Assertions.assertThat(pbtdc.getOrderManagerEmailAsString()).isEqualTo("bobjones@company.com");
    }

    @Test
    @DisplayName("test getLeadDeliveryManagerString returns empty string when null")
    void testGetLeadDeliveryManagerWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getLeadDeliveryManagerAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getLeadDeliveryManagerString returns string when populated")
    void testGetLeadDeliveryManagerWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .contacts(defaultContacts)
                .build();
        Assertions.assertThat(pbtdc.getLeadDeliveryManagerAsString()).isEqualTo("John Smith 1234 johnsmith@company.com");
    }

    @Test
    @DisplayName("test getLogicalLinkServiceClass returns empty string when unpopulated")
    void testGetProductWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getProductAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getServiceClass returns string when populated with WEC")
    void testGetServiceClassWhenPopulatedWithWEC(){
        Pbtdc pbtdc = Pbtdc.builder()
                .serviceClass("WEC")
                .build();
        Assertions.assertThat(pbtdc.getProductAsString()).isEqualTo("BT Etherflow");
    }

    @Test
    @DisplayName("test getServiceClass returns string when populated with WIC")
    void testGetServiceClassWhenPopulatedWithWIC(){
        Pbtdc pbtdc = Pbtdc.builder()
                .serviceClass("WIC")
                .build();
        Assertions.assertThat(pbtdc.getProductAsString()).isEqualTo("BT Etherflow IP");
    }

    @Test
    @DisplayName("test getServiceClass returns string when populated with an unknown value")
    void testGetServiceClassWhenPopulatedWithUnknown(){
        Pbtdc pbtdc = Pbtdc.builder()
                .serviceClass("foobar")
                .build();
        Assertions.assertThat(pbtdc.getProductAsString()).isEqualTo("Unknown : foobar");
    }

    @Test
    @DisplayName("test getLogicalLinkBandwidth returns empty string when unpopulated")
    void testGetLogicalLinkBandwidthWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getLogicalLinkBandwidthAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getLogicalLinkBandwidth returns string when populated")
    void testGetLogicalLinkBandwidthWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .logicalLink(LogicalLink.builder().bandWidth("test_logical_link_bandwidth").build())
                .build();
        Assertions.assertThat(pbtdc.getLogicalLinkBandwidthAsString()).isEqualTo("test_logical_link_bandwidth");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getLogicalLinkCircuitReference returns empty string when unpopulated")
    void testGetLogicalLinkCircuitReferenceWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getLogicalLinkCircuitReferenceAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getLogicalLinkCircuitReference returns string when populated")
    void testGetLogicalLinkCircuitReferenceWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .logicalLink(LogicalLink.builder().circuitReference("test_logical_link_circuit_reference").build())
                .build();
        Assertions.assertThat(pbtdc.getLogicalLinkCircuitReferenceAsString()).isEqualTo("test_logical_link_circuit_reference");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getLogicalLinkVlan returns empty string when unpopulated")
    void testGetLogicalLinkVlanWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getLogicalLinkVlanAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getLogicalLinkVlan returns string when populated")
    void testGetLogicalLinkVlanWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .logicalLink(LogicalLink.builder().vlan("123").build())
                .build();
        Assertions.assertThat(pbtdc.getLogicalLinkVlanAsString()).isEqualTo("123");
    }
    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getCustomerAccessTerminatingEquipmentPortSetting returns empty string when unpopulated")
    void testGetCustomerAccessTerminatingEquipmentPortSettingWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getCustomerAccessTerminatingEquipmentPortSettingAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getCustomerAccessTerminatingEquipmentPortSetting returns string when populated")
    void testGetCustomerAccessTerminatingEquipmentPortSettingWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .customerAccess(Access.builder()
                        .terminatingEquipment(TerminatingEquipment.builder()
                                .portSetting("port_setting")
                                .build()
                        ).build())
                .build();
        Assertions.assertThat(pbtdc.getCustomerAccessTerminatingEquipmentPortSettingAsString()).isEqualTo("port_setting");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getCustomerAccessTerminatingEquipmentPresentation returns empty string when unpopulated")
    void testGetCustomerAccessTerminatingEquipmentPresentationWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getCustomerAccessTerminatingEquipmentPresentationAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getCustomerAccessTerminatingEquipmentPresentation returns string when populated")
    void testGetCustomerAccessTerminatingEquipmentPresentationWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .customerAccess(Access.builder()
                        .terminatingEquipment(TerminatingEquipment.builder()
                                .presentation("presentation")
                                .build()
                        ).build())
                .build();
        Assertions.assertThat(pbtdc.getCustomerAccessTerminatingEquipmentPresentationAsString()).isEqualTo("presentation");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getCustomerAccessCircuitReference returns empty string when unpopulated")
    void testGetCustomerAccessCircuitReferenceWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getCustomerAccessCircuitReferenceAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getCustomerAccessCircuitReference returns string when populated")
    void testGetCustomerAccessCircuitReferenceWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .customerAccess(Access.builder()
                        .circuitReference("circuit_reference"
                        ).build())
                .build();
        Assertions.assertThat(pbtdc.getCustomerAccessCircuitReferenceAsString()).isEqualTo("circuit_reference");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getWholesaleAccessTerminatingEquipmentPresentation returns empty string when unpopulated")
    void testGetWholesaleAccessTerminatingEquipmentPresentationWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getWholesaleAccessTerminatingEquipmentPresentationAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getWholesaleAccessTerminatingEquipmentPresentation returns string when populated")
    void testGetWholesaleAccessTerminatingEquipmentPresentationWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .wholesalerAccess(Access.builder()
                        .terminatingEquipment(TerminatingEquipment.builder()
                                .presentation("presentation")
                                .build()
                        ).build())
                .build();
        Assertions.assertThat(pbtdc.getWholesaleAccessTerminatingEquipmentPresentationAsString()).isEqualTo("presentation");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getWholesalerAccessCircuitReference returns empty string when unpopulated")
    void testGetWholesalerAccessCircuitReferenceWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getWholesalerAccessCircuitReferenceAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getWholesalerAccessCircuitReference returns string when populated")
    void testGetWholesalerAccessCircuitReferenceWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .wholesalerAccess(Access.builder()
                        .circuitReference("circuit_reference")
                        .build())
                .build();
        Assertions.assertThat(pbtdc.getWholesalerAccessCircuitReferenceAsString()).isEqualTo("circuit_reference");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getBusinessStatusDeliveryOnTrackString returns empty string when unpopulated")
    void testGetBusinessStatusDeliveryOnTrackStringWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusDeliveryOnTrackAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getBusinessStatusDeliveryOnTrackString returns string when populated")
    void testGetBusinessStatusDeliveryOnTrackStringWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .businessStatus(PBTDCBusinessStatus.builder()
                        .deliveryOnTrack(true)
                        .build())
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusDeliveryOnTrackAsString()).isEqualTo("Yes");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getBusinessStatusOrderEntryValidationStatus returns empty string when unpopulated")
    void testGetBusinessStatusOrderEntryValidationStatusWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusOrderEntryValidationStatusAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getBusinessStatusOrderEntryValidationStatus returns string when populated")
    void testGetBusinessStatusOrderEntryValidationStatusWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .businessStatus(PBTDCBusinessStatus.builder()
                        .orderEntryAndValidationStatus("validation_status")
                        .build())
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusOrderEntryValidationStatusAsString()).isEqualTo("validation_status");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getBusinessStatusPlanningStatus returns empty string when unpopulated")
    void testGetBusinessStatusPlanningStatusWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusPlanningStatusAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getBusinessStatusPlanningStatus returns string when populated")
    void testGetBusinessStatusPlanningStatusWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .businessStatus(PBTDCBusinessStatus.builder()
                        .planningStatus("planning_status")
                        .build())
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusPlanningStatusAsString()).isEqualTo("planning_status");
    }
    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getBusinessStatusAccessInstallation returns empty string when unpopulated")
    void testGetBusinessStatusAccessInstallationWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusAccessInstallationAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getBusinessStatusAccessInstallation returns string when populated")
    void testGetBusinessStatusAccessInstallationWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .businessStatus(PBTDCBusinessStatus.builder()
                        .accessInstallation("access_installation")
                        .build())
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusAccessInstallationAsString()).isEqualTo("access_installation");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getBusinessStatusTestingCpeInstallation returns empty string when unpopulated")
    void testGetBusinessStatusTestingCpeInstallationWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusTestingCpeInstallationAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getBusinessStatusTestingCpeInstallation returns string when populated")
    void testGetBusinessStatusTestingCpeInstallationWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .businessStatus(PBTDCBusinessStatus.builder()
                        .testingCpeInstallation("testing_cpe_installation")
                        .build())
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusTestingCpeInstallationAsString()).isEqualTo("testing_cpe_installation");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getBusinessStatusServiceCompleteAndOperational returns empty string when unpopulated")
    void testGetBusinessStatusServiceCompleteAndOperationalWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusServiceCompleteAndOperationalAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getBusinessStatusServiceCompleteAndOperational returns string when populated")
    void testGetBusinessStatusServiceCompleteAndOperationalWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .businessStatus(PBTDCBusinessStatus.builder()
                        .serviceCompleteAndOperational("complete_and_operational")
                        .build())
                .build();
        Assertions.assertThat(pbtdc.getBusinessStatusServiceCompleteAndOperationalAsString()).isEqualTo("complete_and_operational");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getSupplierOrderSiebelNumber returns empty string when unpopulated")
    void testGetSupplierOrderSiebelNumberWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getSupplierOrderSiebelNumberAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getSupplierOrderSiebelNumber returns string when populated")
    void testGetSupplierOrderSiebelNumberWhenPopulated(){
        Pbtdc pbtdc = Pbtdc.builder()
                .supplierOrder(
                        SupplierOrder.builder().siebelNumber("siebel_order_number").build()
                ).build();
        Assertions.assertThat(pbtdc.getSupplierOrderSiebelNumberAsString()).isEqualTo("siebel_order_number");
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getSupplierOrderSiebelNumber returns empty string when unpopulated")
    void testGetCustomerInternalDetailsWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getCustomerInternalDetailsAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getSupplierOrderSiebelNumber returns string when populated")
    void testGetCustomerInternalDetailsWhenPopulated(){
        String expectedCustomerInternalDetailsString= "name1 : value1\nname2 : value2\n";
        Pbtdc pbtdc = Pbtdc.builder()
                .customerInternalDetails(List.of(
                        CustomerInternalDetail.builder().name("name1").value("value1").build(),
                        CustomerInternalDetail.builder().name("name2").value("value2").build()
                    )
                ).build();
        Assertions.assertThat(pbtdc.getCustomerInternalDetailsAsString()).isEqualTo(expectedCustomerInternalDetailsString);
    }

    ///////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("test getCustomerAddressAsString returns empty string when unpopulated")
    void testGetCustomerAddressWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getCustomerSiteDetailsAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getCustomerAddressAsString returns address string when no Completion received yet")
    void testGetCustomerAddressWhenPopulatedQBTDCOnly(){
        String expectedCustomerAddress= "full_address\neircode";
        Pbtdc pbtdc = Pbtdc.builder()
                .customerAccess(Access.builder()
                        .site(Site.builder().location(
                                Location.builder()
                                        .id("eircode")
                                        .address(Address.builder()
                                                .fullAddress("full_address")
                                                .build()
                                        ).build()
                                ).build()
                        ).build()
                ).build();
        Assertions.assertThat(pbtdc.getCustomerSiteDetailsAsString()).isEqualTo(expectedCustomerAddress);
    }

    @Test
    @DisplayName("test getCustomerAddressAsString returns address string when Completion received")
    void testGetCustomerAddressWhenPopulatedCompletionReceived(){
        String expectedCustomerAddress= "line1\nline2\nline3\ncity\ncounty\neircode";
        Pbtdc pbtdc = Pbtdc.builder()
                .customerAccess(Access.builder()
                        .site(Site.builder().location(
                                        Location.builder()
                                                .id("eircode")
                                                .address(Address.builder()
                                                        .fullAddress("full_address")
                                                        .locationLine1("line1")
                                                        .locationLine2("line2")
                                                        .locationLine3("line3")
                                                        .city("city")
                                                        .county("county")
                                                        .build()
                                                ).build()
                                ).build()
                        ).build()
                ).build();
        Assertions.assertThat(pbtdc.getCustomerSiteDetailsAsString()).isEqualTo(expectedCustomerAddress);
    }

    ///////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("test getWholesaleSiteDetailsAsString returns empty string when unpopulated")
    void testGetWholesaleSiteDetailsWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getWholesaleSiteDetailsAsString(Map.of())).isEqualTo("");
    }

    @Test
    @DisplayName("test getWholesaleSiteDetailsAsString returns address string when no Completion received yet")
    void testGetWholesaleSiteDetailsWhenPopulated(){
        String expectedWholesaleSite= "BALLYMOUNT";
        Pbtdc pbtdc = Pbtdc.builder()
                .wholesalerAccess(Access.builder()
                        .site(Site.builder().location(
                                        Location.builder()
                                                .id("BALLYMOUNT")
                                                .build()
                                ).build()
                        ).build()
                ).build();
        Assertions.assertThat(pbtdc.getWholesaleSiteDetailsAsString(Map.of())).isEqualTo(expectedWholesaleSite);
    }

    ///////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("test getOperatorNameAsString returns empty string when unpopulated")
    void testGetOperatorNameWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getOperatorNameAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getOperatorNameAsString returns expected string when name/code are populated")
    void testGetOperatorNameWhenPopulated(){
        String expected = "myoperator / mycode";
        Pbtdc pbtdc = Pbtdc.builder()
                .operatorName("myoperator")
                .operatorCode("mycode")
                .build();
        Assertions.assertThat(pbtdc.getOperatorNameAsString()).isEqualTo(expected);
    }

    ///////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("test getCustomerContactDetailsAsString returns empty string when unpopulated")
    void testGetCustomerContactDetailsAsStringWhenNull() {
        Pbtdc pbtdc = Pbtdc.builder()
                .customerAccess(Access.builder().accessInstall(AccessInstall.builder().build()).build())
                .build();
        Assertions.assertThat(pbtdc.getCustomerContactDetailsAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getOperatorNameAsString returns expected string when fields are populated")
    void testGetCustomerContactDetailsAsStringWhenPopulated(){
        StringBuilder expected = new StringBuilder()
                .append("Main : Harold Hamlyn / harold@hhm.com / 4444").append('\n');
        Pbtdc pbtdc = Pbtdc.builder()
                .customerAccess(Access.builder().accessInstall(AccessInstall.builder().contacts(defaultContacts).build()).build())
                .build();

        Assertions.assertThat(pbtdc.getCustomerContactDetailsAsString()).isEqualTo(expected.toString());
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("test getWholesaleSiteDetailsAsString returns expected string")
    void testGetWholesaleContactDetailsAsStringWhenPopulated(){
        String expected = "Main : bendmain_firstname main_lastname / main_email / main_number" + "\n";
        Pbtdc pbtdc = Pbtdc.builder()
                .wholesalerAccess(Access.builder()
                    .accessInstall(AccessInstall.builder()
                        .contacts(new ArrayList<>(List.of(
                                Contact.builder()
                                        .role(ContactTypes.MAIN_CONTACT.getContactOwner())
                                        .firstName("bendmain_firstname")
                                        .lastName("main_lastname")
                                        .number("main_number")
                                        .email("main_email")
                                        .build()
                                ))
                        ).build())
                    .build())
                .build();
        Assertions.assertThat(pbtdc.getWholesaleContactDetailsAsString()).isEqualTo(expected);
    }

    ///////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("test dateReceivedByDeliveryAsString returns empty string when unpopulated")
    void testGetDateReceivedByDeliveryAsStringWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.dateReceivedByDeliveryAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test dateReceivedByDeliveryAsString returns expected string when fields are populated")
    void testGetDateReceivedByDeliveryAsStringWhenPopulated(){
        String expected = "07-05-2022";
        Pbtdc pbtdc = Pbtdc.builder()
                .dateReceivedByDelivery(LocalDateTime.of(2022, 05, 07, 12, 12))
                .build();
        Assertions.assertThat(pbtdc.dateReceivedByDeliveryAsString()).isEqualTo(expected);
    }

    ///////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("test getEstimatedCompletionDateAsString returns empty string when unpopulated")
    void testGetEstimatedCompletionDateAsStringWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getEstimatedCompletionDateAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getEstimatedCompletionDateAsString returns expected string when fields are populated")
    void testGetEstimatedCompletionDateAsStringWhenPopulated(){
        String expected = "08-05-2022";
        Pbtdc pbtdc = Pbtdc.builder()
                .estimatedCompletionDate(LocalDate.of(2022, 5, 8))
                .build();
        Assertions.assertThat(pbtdc.getEstimatedCompletionDateAsString()).isEqualTo(expected);
    }

    ///////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("test getDueCompletionDateAsString returns empty string when unpopulated")
    void testGetDueCompletionDateAsStringWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getDueCompletionDateAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getDueCompletionDateAsString returns expected string when fields are populated")
    void testGetDueCompletionDateAsStringWhenPopulated(){
        String expected = "07-05-2022";
        Pbtdc pbtdc = Pbtdc.builder()
                .dueCompletionDate(LocalDate.of(2022, 5, 7))
                .build();
        Assertions.assertThat(pbtdc.getDueCompletionDateAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test getDeliveryDateAsString returns empty string when unpopulated")
    void testGetDeliveryDateAsStringAsStringWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getDeliveryDateAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getDeliveryDateAsString returns expected string when fields are populated")
    void testGetDeliveryDateAsStringAsStringWhenPopulated(){
        String expected = "02-01-2022";
        Pbtdc pbtdc = Pbtdc.builder()
                .businessStatus(PBTDCBusinessStatus.builder().deliveryDate(LocalDate.of(2022, 1, 2)).build())
                .build();
        Assertions.assertThat(pbtdc.getDeliveryDateAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test getNotesDateAsString returns empty string when unpopulated")
    void testGetNotesReceivedDateAsStringWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getNotesReceivedDateAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getDeliveryDateAsString returns expected string when fields are populated")
    void testGetNotesReceivedDateAsStringWhenPopulated(){
        String expected = "02-01-2022 11:10:09";
        Pbtdc pbtdc = Pbtdc.builder()
                .notesReceivedDate(LocalDateTime.of(2022, 1, 2, 11, 10, 9))
                .build();
        Assertions.assertThat(pbtdc.getNotesReceivedDateAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test getOaoAsString returns empty string when unpopulated")
    void testGetOaoAsStringAsStringWhenNull(){
        Pbtdc pbtdc = Pbtdc.builder()
                .build();
        Assertions.assertThat(pbtdc.getOaoAsString()).isEqualTo("");
    }

    @Test
    @DisplayName("test getDeliveryDateAsString returns expected string when fields are populated")
    void testGetOaoAsStringAsStringWhenPopulated(){
        String expected = "testoao";
        Pbtdc pbtdc = Pbtdc.builder()
                .oao("testoao")
                .build();
        Assertions.assertThat(pbtdc.getOaoAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test getAccessSpeedAsString returns expected string when field is populated from quote")
    void getAccessSpeedAsStringWhenPopulatedFromQuote(){
        String expected = "30";
        Pbtdc pbtdc = Pbtdc.builder()
                .quote(Quote.builder()
                        .aendBandwidth("30").build())
                .build();
        Assertions.assertThat(pbtdc.getAccessSpeedAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test getAccessSpeedAsString returns expected string when field is populated for existing etherway")
    void getAccessSpeedAsStringWhenPopulatedForExistingEtherway(){
        String expected = "30";
        Pbtdc pbtdc = Pbtdc.builder()
                .customerAccess(Access.builder()
                        .bandWidth("30").build())
                .build();
        Assertions.assertThat(pbtdc.getAccessSpeedAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test getLogicalLinkGlanIdAsString returns expected string when field is populated")
    void getLogicalLinkGlanIdAsStringWhenPopulated(){
        String expected = "1234567890";
        Pbtdc pbtdc = Pbtdc.builder()
                .logicalLink(LogicalLink.builder()
                        .glanId("1234567890").build())
                .build();
        Assertions.assertThat(pbtdc.getLogicalLinkGlanIdAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test getLogicalLinkGlanIdAsString returns empty string when field is unpopulated")
    void getLogicalLinkGlanIdAsStringWhenUnpopulated(){
        String expected = "";
        Pbtdc pbtdc = Pbtdc.builder().build();
        Assertions.assertThat(pbtdc.getLogicalLinkGlanIdAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test getCustomerAccessGlanIdAsString returns expected string when field is populated")
    void getCustomerAccessGlanIdAsStringWhenPopulated(){
        String expected = "1234567890";
        Pbtdc pbtdc = Pbtdc.builder()
                .customerAccess(Access.builder()
                        .glanId("1234567890").build())
                .build();
        Assertions.assertThat(pbtdc.getCustomerAccessGlanIdAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test getCustomerAccessGlanIdAsString returns empty string when field is unpopulated")
    void getCustomerAccessGlanIdAsStringWhenUnpopulated(){
        String expected = "";
        Pbtdc pbtdc = Pbtdc.builder().build();
        Assertions.assertThat(pbtdc.getCustomerAccessGlanIdAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test getWholesalerAccessGlanIdAsString returns expected string when field is populated")
    void getWholesalerAccessGlanIdAsStringWhenPopulated(){
        String expected = "1234567890";
        Pbtdc pbtdc = Pbtdc.builder()
                .wholesalerAccess(Access.builder()
                        .glanId("1234567890").build())
                .build();
        Assertions.assertThat(pbtdc.getWholesalerAccessGlanIdAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test getWholesalerAccessGlanIdAsString returns empty string when field is unpopulated")
    void getWholesalerAccessGlanIdAsStringWhenUnpopulated(){
        String expected = "";
        Pbtdc pbtdc = Pbtdc.builder().build();
        Assertions.assertThat(pbtdc.getWholesalerAccessGlanIdAsString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("test pbtdc toString() does not cause stackoverflow")
    void testPbtdcToStringMethod(){
        Pbtdc pbtdc = OrderFactory.ordersWithRejectDetails();
        pbtdc.getRejectionDetails().setOrders(pbtdc);
        pbtdc.toString();
        Assertions.assertThat(pbtdc).isNotNull();
    }


}
