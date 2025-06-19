package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.rest.exception.checked.TalosForbiddenException;
import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.domain.*;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportDTO;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportSetDTO;
import com.btireland.talos.ethernet.engine.dto.PbtdcReportDateView;
import com.btireland.talos.ethernet.engine.dto.ReportAvailabilityDTO;
import com.btireland.talos.ethernet.engine.exception.BadRequestException;
import com.btireland.talos.ethernet.engine.exception.ReportGenerationException;
import com.btireland.talos.ethernet.engine.util.ContactTypes;
import com.btireland.talos.ethernet.engine.util.ReportAvailabilityDTOFactory;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class PBTDCReportGeneratorServiceTest {

    @Mock
    private PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService;

    @Mock
    private PBTDCReportPersistenceService pbtdcReportPersistenceService;
    @Mock
    private ApplicationConfiguration applicationConfiguration;

    @Mock
    PbtdcReportDateView pbtdcReportDateView;

    @InjectMocks
    private PBTDCReportGeneratorService pbtdcReportGeneratorService;

    @Captor
    ArgumentCaptor<PBTDCOrderReportSetDTO> pbtdcReportSetArgumentCaptor;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    ArgumentCaptor<LocalDate> localDateArgumentCaptor;

    private PBTDCOrderReportDTO.PBTDCOrderReportDTOBuilder basicOrderReportDTOBuilder(){

        return PBTDCOrderReportDTO.builder()
                .orderManager("")
                .orderManagerEmail("")
                .customerName("")
                .siteName("")
                .dateReceivedByDelivery("")
                .product("")
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getPrompt())
                .referenceIds("")
                .deliveryType("New Delivery")
                .aendSiteDetails("")
                .aendContactDetails("")
                .bendSiteDetails("")
                .bendContactDetails("")
                .accessSpeed("")
                .portSpeed("")
                .aendPresentation("")
                .bendPresentation("")
                .notes("")
                .indicativeDeliveryDate("")
                .deliveryDate("")
                .deliveryOnTrack("")
                .orderEntryAndValidationStatus("")
                .planningStatus("")
                .accessInstallation("")
                .testingCpeInstallation("")
                .serviceCompleteAndOperational("");
    }

    private Pbtdc.PbtdcBuilder getBasicPBTDCOrderBuilder(){
        return Pbtdc.builder()
                // Customer Name (name and code)
                .operatorName("Test customer")
                .operatorCode("TC")
                .serviceClass("WEC")
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                .organisationName("Test Customer Site Name")
                // NOG order number
                .orderNumber("2233")
                .supplierOrder(SupplierOrder.builder()
                        .siebelNumber("siebel_order_number")
                        .build())

                // BT Logical VLAN
                .logicalLink(LogicalLink.builder()
                        .vlan("1111")
                        // Product
                        .serviceClass("WEC")
                        .build())

                // Lead Delivery Manager
                .contacts(new ArrayList<>(List.of(
                        Contact.builder()
                                .role(ContactTypes.LEAD_DELIVERY_MANAGER.getContactOwner())
                                .firstName("lead_firstname")
                                .lastName("lead_lastname")
                                .number("lead_number")
                                .email("lead_email")
                                .build()
                        ))
                )

                // Customer Internal Details
                .customerInternalDetails(List.of(
                        CustomerInternalDetail.builder()
                                .name("key1")
                                .value("value1")
                                .build(),
                        CustomerInternalDetail.builder()
                                .name("key2")
                                .value("value2")
                                .build(),
                        CustomerInternalDetail.builder()
                                .name("key3")
                                .value("value3")
                                .build(),
                        CustomerInternalDetail.builder()
                                .name("key4")
                                .value("value4")
                                .build(),
                        CustomerInternalDetail.builder()
                                .name("key5")
                                .value("value5")
                                .build()
                ))

                // Site Details - B End
                .wholesalerAccess(Access.builder()
                        .site(Site.builder()
                                .location(Location.builder()
                                        .id("b_end_site_identifier")
                                        .build())
                                .build())
                        .terminatingEquipment(TerminatingEquipment.builder()
                                // Presentation A End
                                .presentation("LC90")
                                .build())
                        .build())

                .customerAccess(Access.builder()
                        // Site Details - A End
                        .site(Site.builder()
                                .location(Location.builder()
                                        .address(Address.builder()
                                                .fullAddress("default_address")
                                                .build())
                                        .id("default_eircode")
                                        .build())
                                // Site Name
                                .name("Test Customer Site Name")
                                .build())
                        .terminatingEquipment(TerminatingEquipment.builder()
                                // Presentation A End
                                .presentation("LC45")
                                .build())
                        .accessInstall(AccessInstall.builder()
                                // Contact Details - A End
                                .contacts(new ArrayList<>(List.of(
                                        Contact.builder()
                                                .role(ContactTypes.LEAD_DELIVERY_MANAGER.getContactOwner())
                                                .firstName("lead_firstname")
                                                .lastName("lead_lastname")
                                                .number("lead_number")
                                                .email("lead_email")
                                                .build(),
                                        Contact.builder()
                                                .role(ContactTypes.MAIN_CONTACT.getContactOwner())
                                                .firstName("main_firstname")
                                                .lastName("main_lastname")
                                                .number("main_number")
                                                .email("main_email")
                                                .build(),
                                        Contact.builder()
                                                .role(ContactTypes.LANDLORD.getContactOwner())
                                                .firstName("ll_firstname")
                                                .lastName("ll_lastname")
                                                .number("ll_number")
                                                .email("ll_email")
                                                .build(),
                                        Contact.builder()
                                                .role(ContactTypes.BUILDING_MANAGER.getContactOwner())
                                                .firstName("b-mgr_firstname")
                                                .lastName("b-mgr_lastname")
                                                .number("b-mgr_number")
                                                .email("b-mgr_email")
                                                .build()
                                        ))
                                )
                                .build())
                        .build())

                // Indicative Delivery Date
                .estimatedCompletionDate(LocalDate.of(2022, 9, 3));
    }

    @Test
    @DisplayName("Confirm the report content with an empty order")
    void testReportContentWhenOrderEmpty() throws ReportGenerationException, TalosForbiddenException {
        PBTDCOrderReportDTO expectedInternalReport = this.basicOrderReportDTOBuilder().build();

        Mockito.when(pbtdcOrdersPersistenceService.findOaosWithActiveOrders(any(LocalDateTime.class))).thenReturn(
                List.of("acme")
        );

        Mockito.when(pbtdcOrdersPersistenceService.findActiveOrdersByOao(anyString(), any(LocalDateTime.class))).thenReturn(
                List.of(Pbtdc.builder().connectionType(ConnectionType.ETHERWAY_STANDARD.getValue()).build())
        );

        pbtdcReportGeneratorService.generate(Optional.of(LocalDate.now()));

        Mockito.verify(pbtdcReportPersistenceService).save(stringArgumentCaptor.capture(),
                localDateArgumentCaptor.capture(),
                pbtdcReportSetArgumentCaptor.capture());

        PBTDCOrderReportSetDTO resultReportSet = pbtdcReportSetArgumentCaptor.getValue();

        Assertions.assertThat(stringArgumentCaptor.getValue()).isEqualTo("acme");
        Assertions.assertThat(localDateArgumentCaptor.getValue()).isEqualTo(LocalDate.now());

        // verify that the report record matches what we expect

        Assertions.assertThat( resultReportSet.getInternalReportEntries().get(0)).usingRecursiveComparison().isEqualTo(expectedInternalReport);
    }

    @Test
    @DisplayName("Confirm the report content with a basic order in place")
    void testReportContentWhenBasicOrderSubmitted() throws ReportGenerationException, TalosForbiddenException {
        String expectedReferenceIds = new StringBuilder()
                .append("NOG Order Number : 2233").append('\n')
                .append("Siebel Order Ref : siebel_order_number").append('\n')
                .append("BT Logical VLAN : 1111").append('\n')
                .append("Company Lead Delivery Manager : lead_firstname lead_lastname lead_number lead_email").append('\n')
                .append("key1 : value1").append('\n')
                .append("key2 : value2").append('\n')
                .append("key3 : value3").append('\n')
                .append("key4 : value4").append('\n')
                .append("key5 : value5").append('\n')
                .toString();

        PBTDCOrderReportDTO expectedInternalReport = this.basicOrderReportDTOBuilder()
                .referenceIds(expectedReferenceIds)
                .siteName("Test Customer Site Name")
                .product("BT Etherflow")
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getPrompt())
                .deliveryType("New Delivery")
                .aendSiteDetails("default_address\ndefault_eircode")
                .bendSiteDetails("b_end_site_identifier")
                .aendContactDetails("Main : main_firstname main_lastname / main_email / main_number" + "\n")
                .aendPresentation("LC45")
                .bendPresentation("LC90")
                .indicativeDeliveryDate("03-09-2022")
                .build();

        Pbtdc testOrder = getBasicPBTDCOrderBuilder()
                .build();

        Mockito.when(pbtdcOrdersPersistenceService.findOaosWithActiveOrders(any(LocalDateTime.class))).thenReturn(
                List.of("acme")
        );

        Mockito.when(pbtdcOrdersPersistenceService.findActiveOrdersByOao(anyString(), any(LocalDateTime.class))).thenReturn(
                List.of(testOrder)
        );

        pbtdcReportGeneratorService.generate(Optional.of(LocalDate.now()));

        Mockito.verify(pbtdcReportPersistenceService).save(stringArgumentCaptor.capture(),
                localDateArgumentCaptor.capture(),
                pbtdcReportSetArgumentCaptor.capture());

        PBTDCOrderReportSetDTO resultReportSet = pbtdcReportSetArgumentCaptor.getValue();

        Assertions.assertThat(stringArgumentCaptor.getValue()).isEqualTo("acme");
        Assertions.assertThat(localDateArgumentCaptor.getValue()).isEqualTo(LocalDate.now());

        // verify that the report record matches what we expect

        Assertions.assertThat( resultReportSet.getInternalReportEntries().get(0)).usingRecursiveComparison().isEqualTo(expectedInternalReport);
    }

    @Test
    @DisplayName("Confirm the report content with a barebones order plus an Accept notification")
    void testReportContentWhenBasicOrderReceivesAcceptNotification() throws ReportGenerationException,
            TalosForbiddenException {
        //accept updates : Order Manager name/email; date received by delivery; siebel order ref; delivery date; indicative delivery date
        String expectedReferenceIds = new StringBuilder()
                .append("Siebel Order Ref : siebel_number").append('\n')
                .toString();

        PBTDCOrderReportDTO expectedInternalReport = this.basicOrderReportDTOBuilder()
                .referenceIds(expectedReferenceIds)
                .orderManager("ordermfn ordermln")
                .orderManagerEmail("orderm@company.com")
                .dateReceivedByDelivery("12-12-2022")
                .deliveryDate("11-11-2022")
                .indicativeDeliveryDate("11-11-2022")
                .deliveryType("New Delivery")
                .portSpeed("25")
                .accessSpeed("35")
                .build();

        Pbtdc testOrder = Pbtdc.builder()
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                .businessStatus(PBTDCBusinessStatus.builder()
                        .deliveryDate(LocalDate.of(2022, 11, 11))
                        .build())
                .contacts(List.of(
                    Contact.builder()
                        .firstName("ordermfn")
                        .lastName("ordermln")
                        .email("orderm@company.com")
                        .role(ContactTypes.ORDER_MANAGER.getContactOwner())
                        .build()
                ))
                .dateReceivedByDelivery(LocalDateTime.of(2022, 12, 12, 12, 12))
                .supplierOrder(SupplierOrder.builder()
                        .siebelNumber("siebel_number").build())
                .estimatedCompletionDate(LocalDate.of(2022, 11, 11))
                .quote(Quote.builder()
                        .logicalBandwidth("25")
                        .aendBandwidth("35")
                        .build())
                .build();

        Mockito.when(pbtdcOrdersPersistenceService.findOaosWithActiveOrders(any(LocalDateTime.class))).thenReturn(
                new ArrayList<>(List.of("acme"))
        );

        Mockito.when(pbtdcOrdersPersistenceService.findActiveOrdersByOao(anyString(), any(LocalDateTime.class))).thenReturn(
                new ArrayList<>(List.of(testOrder))
        );

        pbtdcReportGeneratorService.generate(Optional.of(LocalDate.now()));

        Mockito.verify(pbtdcReportPersistenceService).save(stringArgumentCaptor.capture(),
                localDateArgumentCaptor.capture(),
                pbtdcReportSetArgumentCaptor.capture());

        PBTDCOrderReportSetDTO resultReportSet = pbtdcReportSetArgumentCaptor.getValue();

        // verify that the report record matches what we expect

        Assertions.assertThat( resultReportSet.getInternalReportEntries().get(0)).usingRecursiveComparison().isEqualTo(expectedInternalReport);
    }

    // test when a notes notification is received.
    //      notes updates : notes notification

    @Test
    @DisplayName("Confirm the report content with a basic order in place and a Notes notification")
    void testReportContentWhenBasicOrderPlusNotesNotification() throws ReportGenerationException,
            TalosForbiddenException {
        PBTDCOrderReportDTO expectedInternalReport = this.basicOrderReportDTOBuilder()
                .notes("(Dated 28-07-2022 11:10:09) My Notes")
                .build();

        Pbtdc testOrder = Pbtdc.builder()
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                .notes("My Notes")
                .notesReceivedDate(LocalDateTime.of(2022, 7, 28, 11, 10, 9))
                .build();

        Mockito.when(pbtdcOrdersPersistenceService.findOaosWithActiveOrders(any(LocalDateTime.class))).thenReturn(
                List.of("acme")
        );

        Mockito.when(pbtdcOrdersPersistenceService.findActiveOrdersByOao(anyString(), any(LocalDateTime.class))).thenReturn(
                List.of(testOrder)
        );

        pbtdcReportGeneratorService.generate(Optional.of(LocalDate.now()));

        Mockito.verify(pbtdcReportPersistenceService).save(stringArgumentCaptor.capture(),
                localDateArgumentCaptor.capture(),
                pbtdcReportSetArgumentCaptor.capture());

        PBTDCOrderReportSetDTO resultReportSet = pbtdcReportSetArgumentCaptor.getValue();

        Assertions.assertThat(stringArgumentCaptor.getValue()).isEqualTo("acme");
        Assertions.assertThat(localDateArgumentCaptor.getValue()).isEqualTo(LocalDate.now());

        // verify that the report record matches what we expect

        Assertions.assertThat( resultReportSet.getInternalReportEntries().get(0)).usingRecursiveComparison().isEqualTo(expectedInternalReport);
    }

    // test when a CF notification is received.
    //      CF updates : delivery date, delivery on track field

    @Test
    @DisplayName("Confirm the report content with a basic order in place and a CF notification")
    void testReportContentWhenBasicOrderPlusCFNotification() throws ReportGenerationException, TalosForbiddenException {
        PBTDCOrderReportDTO expectedInternalReport = this.basicOrderReportDTOBuilder()
                .deliveryType("New Delivery")
                .deliveryDate("02-02-2022")
                .deliveryOnTrack("Yes")
                .build();

        Pbtdc testOrder = Pbtdc.builder()
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                .businessStatus(PBTDCBusinessStatus.builder()
                        .deliveryOnTrack(true)
                        .deliveryDate(LocalDate.of(2022, 02, 02))
                        .build())
                .build();

        Mockito.when(pbtdcOrdersPersistenceService.findOaosWithActiveOrders(any(LocalDateTime.class))).thenReturn(
                List.of("acme")
        );

        Mockito.when(pbtdcOrdersPersistenceService.findActiveOrdersByOao(anyString(), any(LocalDateTime.class))).thenReturn(
                List.of(testOrder)
        );

        pbtdcReportGeneratorService.generate(Optional.of(LocalDate.now()));

        Mockito.verify(pbtdcReportPersistenceService).save(stringArgumentCaptor.capture(),
                localDateArgumentCaptor.capture(),
                pbtdcReportSetArgumentCaptor.capture());

        PBTDCOrderReportSetDTO resultReportSet = pbtdcReportSetArgumentCaptor.getValue();

        Assertions.assertThat(stringArgumentCaptor.getValue()).isEqualTo("acme");
        Assertions.assertThat(localDateArgumentCaptor.getValue()).isEqualTo(LocalDate.now());

        // verify that the report record matches what we expect

        Assertions.assertThat( resultReportSet.getInternalReportEntries().get(0)).usingRecursiveComparison().isEqualTo(expectedInternalReport);
    }

    // test when a CI notification is received.
    //      updates : logical BT circuit ID; A/B End Etherway Circuit ID

    @Test
    @DisplayName("Confirm the report content with a basic order in place and a CI notification")
    void testReportContentWhenBasicOrderPlusCINotification() throws ReportGenerationException, TalosForbiddenException {
        String expectedReferenceIds = new StringBuilder()
                .append("Logical BT Circuit ID : LBTCI\n")
                .append("A End Etherway BT Circuit ID : ABTCI\n")
                .append("B End Etherway BT Circuit ID : BBTCI\n")
                .toString();

        PBTDCOrderReportDTO expectedInternalReport = this.basicOrderReportDTOBuilder()
                .referenceIds(expectedReferenceIds)
                .product("BT Etherflow")
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getPrompt())
                .build();

        Pbtdc testOrder = Pbtdc.builder()
                .serviceClass("WEC")
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                .logicalLink(LogicalLink.builder()
                        .id(1L)
                        .serviceClass("WEC")
                        .circuitReference("LBTCI")
                        .build())
                .customerAccess(Access.builder()
                        .id(2L)
                        .circuitReference("ABTCI")
                        .build())
                .wholesalerAccess(Access.builder()
                        .id(3L)
                        .circuitReference("BBTCI")
                        .build())
                .build();

        Mockito.when(pbtdcOrdersPersistenceService.findOaosWithActiveOrders(any(LocalDateTime.class))).thenReturn(
                List.of("acme")
        );

        Mockito.when(pbtdcOrdersPersistenceService.findActiveOrdersByOao(anyString(), any(LocalDateTime.class))).thenReturn(
                List.of(testOrder)
        );

        pbtdcReportGeneratorService.generate(Optional.of(LocalDate.now()));
        Mockito.verify(pbtdcReportPersistenceService).save(stringArgumentCaptor.capture(),
                localDateArgumentCaptor.capture(),
                pbtdcReportSetArgumentCaptor.capture());

        PBTDCOrderReportSetDTO resultReportSet = pbtdcReportSetArgumentCaptor.getValue();

        Assertions.assertThat(stringArgumentCaptor.getValue()).isEqualTo("acme");
        Assertions.assertThat(localDateArgumentCaptor.getValue()).isEqualTo(LocalDate.now());

        // verify that the report record matches what we expect

        Assertions.assertThat( resultReportSet.getInternalReportEntries().get(0)).usingRecursiveComparison().isEqualTo(expectedInternalReport);
    }
    
    // test when a PBTDC Complete notification is received.
    // updates :
    /*
        A End Site Details - should override the details taken from the order.
        Contact Details B End
        Access Speed
        Port Speed
        Presentation A End
        Presentation B End
     */

    @Test
    @DisplayName("Confirm the report content with a basic order in place and a Complete notification")
    void testReportContentWhenBasicOrderPlusCompleteNotification() throws ReportGenerationException,
            TalosForbiddenException {
        PBTDCOrderReportDTO expectedInternalReport = this.basicOrderReportDTOBuilder()
                .aendSiteDetails("location1\nlocation2\nlocation3\nmycity\nmycounty\neircode")
                .bendSiteDetails("b_end_site_identifier\nlocation1\nlocation2\nlocation3\nmycity\nmycounty")
                .bendContactDetails("Main : bendmain_firstname main_lastname / main_email / main_number" + "\n")                .accessSpeed("100M")
                .portSpeed("100")
                .accessSpeed("35")
                .aendPresentation("customerPresentation")
                .bendPresentation("wholesalePresentation")
                .build();

        Pbtdc testOrder = Pbtdc.builder()
                .connectionType(ConnectionType.ETHERWAY_STANDARD.getValue())
                .customerAccess(Access.builder()
                        .site(Site.builder()
                                .location(Location.builder()
                                        .address(Address.builder()
                                                .locationLine1("location1")
                                                .locationLine2("location2")
                                                .locationLine3("location3")
                                                .city("mycity")
                                                .county("mycounty")
                                                .build())
                                        .id("eircode")
                                        .build())
                                .build())
                        .terminatingEquipment(TerminatingEquipment.builder()
                                .portSpeed("20M")
                                .presentation("customerPresentation")
                                .build()
                        )
                       .build())
                .wholesalerAccess(Access.builder()
                        .accessInstall(AccessInstall.builder()
                                // Contact Details - B End
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
                        .site(Site.builder()
                                .location(Location.builder()
                                        .address(Address.builder()
                                                .locationLine1("location1")
                                                .locationLine2("location2")
                                                .locationLine3("location3")
                                                .city("mycity")
                                                .county("mycounty")
                                                .build())
                                        .id("b_end_site_identifier")
                                        .build())
                                .build())
                        .terminatingEquipment(TerminatingEquipment.builder()
                                .presentation("wholesalePresentation")
                                .build()
                        )
                        .build())
                .logicalLink(LogicalLink.builder()
                        .bandWidth("100")
                        .build())
                .quote(Quote.builder()
                        .logicalBandwidth("25")
                        .aendBandwidth("35")
                        .build())
                .build();

        Mockito.when(pbtdcOrdersPersistenceService.findOaosWithActiveOrders(any(LocalDateTime.class))).thenReturn(
                List.of("acme")
        );

        Mockito.when(pbtdcOrdersPersistenceService.findActiveOrdersByOao(anyString(), any(LocalDateTime.class))).thenReturn(
                List.of(testOrder)
        );

        pbtdcReportGeneratorService.generate(Optional.of(LocalDate.now()));

        Mockito.verify(pbtdcReportPersistenceService).save(stringArgumentCaptor.capture(),
                localDateArgumentCaptor.capture(),
                pbtdcReportSetArgumentCaptor.capture());

        PBTDCOrderReportSetDTO resultReportSet = pbtdcReportSetArgumentCaptor.getValue();

        Assertions.assertThat(stringArgumentCaptor.getValue()).isEqualTo("acme");
        Assertions.assertThat(localDateArgumentCaptor.getValue()).isEqualTo(LocalDate.now());

        // verify that the report record matches what we expect

        Assertions.assertThat( resultReportSet.getInternalReportEntries().get(0)).usingRecursiveComparison().isEqualTo(expectedInternalReport);
    }

    @Nested
    @DisplayName("Test retrieving report availability")
    class retrieveReportAvailability {

        @Test
        @DisplayName("Test retrieving report availability for date range")
        void testRetrieveReportAvailability() throws ParseException, BadRequestException {
            Mockito.when(pbtdcReportPersistenceService.getReportsAvailableDatesForDateRange(any(LocalDate.class), any(LocalDate.class), anyString())).thenReturn(
                    List.of(pbtdcReportDateView)
            );
            ApplicationConfiguration.ReportConfiguration reportConfiguration = new ApplicationConfiguration.ReportConfiguration();
            reportConfiguration.setAvailabilityDateRange(31);
            Mockito.when(applicationConfiguration.getReport()).thenReturn(reportConfiguration);
            Mockito.when(pbtdcReportDateView.getReportDate()).thenReturn(DateUtils.parseDate("03-02-2022", "dd-MM-yyyy"));
            ReportAvailabilityDTO expected = ReportAvailabilityDTOFactory.reportAvailability();
            ReportAvailabilityDTO actual = pbtdcReportGeneratorService.retrieveReportAvailability(LocalDate.parse("2022-02-01"), LocalDate.parse("2022-02-04"), "sky");
            Assertions.assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("Date range is invalid")
        void testRetrieveReportAvailability_invalidDateRange() throws  BadRequestException {

            ApplicationConfiguration.ReportConfiguration reportConfiguration = new ApplicationConfiguration.ReportConfiguration();
            reportConfiguration.setAvailabilityDateRange(31);
            Mockito.when(applicationConfiguration.getReport()).thenReturn(reportConfiguration);
            Assertions.assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> {
                        pbtdcReportGeneratorService.retrieveReportAvailability(LocalDate.parse("2022-02-01"), LocalDate.parse("2022-03-04"), "sky");
                    });
        }

        @Test
        @DisplayName("End Date is invalid")
        void testRetrieveReportAvailability_invalidEndDate() throws  BadRequestException {

            Assertions.assertThatExceptionOfType(BadRequestException.class)
                    .isThrownBy(() -> {
                        pbtdcReportGeneratorService.retrieveReportAvailability(LocalDate.parse("2022-02-05"), LocalDate.parse("2022-02-01"), "sky");
                    });
        }
    }
}
