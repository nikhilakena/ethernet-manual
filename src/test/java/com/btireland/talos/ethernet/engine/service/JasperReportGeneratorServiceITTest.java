package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.config.JasperConfiguration;
import com.btireland.talos.ethernet.engine.dto.OaoDetailDTO;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportDTO;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportSetDTO;
import com.btireland.talos.ethernet.engine.e2e.SampleReportSetFactory;
import com.btireland.talos.ethernet.engine.report.PBTDCReportExporterFactory;
import com.btireland.talos.ethernet.engine.util.OaoUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class JasperReportGeneratorServiceITTest {
    @MockBean
    private PBTDCReportPersistenceService pbtdcReportPersistenceService;
    @Autowired
    private PBTDCReportExporterFactory pbtdcReportExporterFactory;
    @Autowired
    private JasperConfiguration jasperConfiguration;
    @Autowired
    private JasperFillManager jasperFillManager;
    @Autowired
    private JasperCompileManager jasperCompileManager;
    @Autowired
    OaoUtils oaoUtils;
    @MockBean
    private ApplicationConfiguration applicationConfiguration;

    @MockBean
    private OrderValidationHelperService orderValidationHelperService;

    private JasperReportGeneratorService jasperReportGeneratorService;

    private static final String expectedTandCValue = "Please note all customer delays will mean the original fibre" +
            " delivery dates provided by BT and/or the access suppliers will be impacted and your delivery date (CCD)" +
            " will be re-forecasted. The ability to expedite the delivery will also be impacted.\n\nPlease note that in" +
            " line with our T&Cs, orders that are in customer delay before the service start for greater than an" +
            " aggregate of 60 business days will be deemed to be cancelled unless a revised start date can be mutually" +
            " agreed between the parties. In the case of deemed cancellation, full connection charges (where applicable)" +
            " may be charged in addition to any cancellation charges set out in the applicable schedule.";


    @BeforeAll
    void initializeReportGenerator(){
        this.jasperReportGeneratorService = new JasperReportGeneratorService(
                pbtdcReportPersistenceService,
                pbtdcReportExporterFactory,
                jasperFillManager,
                jasperCompileManager,
                jasperConfiguration,
                applicationConfiguration,
                oaoUtils);
    }

    // A quirk of the Jasper report generator is that it seems to compute how many characters
    // will fit in each field based on the font size. Since the font used is not a fixed-with font,
    // the report will appear to truncate characters at random depending on which characters are
    // present.
    // To test this deterministically we generate report input based on a series of ! symbols.
    // If a string contains wider characters, there is a chance that values will be truncated.

    @Test
    @DisplayName("test that long strings are returned in the report output")
    void testJasperReportGeneration() throws Exception{
        JSONArray expectedJson = new JSONArray()
                .put(new JSONObject()
                        .put("Date Received By Delivery", "!".repeat(10))
                        .put("Order Entry and Validation Status", "!".repeat(18))
                        .put("Planning Status", "!".repeat(18))
                        .put("Access Installation", "!".repeat(18))
                        .put("Testing CPE Installation", "!".repeat(18))
                        .put("Service Complete and Operational", "!".repeat(8))
                        .put("Delivery On Track", "!".repeat(4))
                        .put("Indicative Delivery Date", "!".repeat(10))
                        .put("Delivery Date", "!".repeat(10))
                        .put("Notes", "!".repeat(300))
                        .put("Reference IDs", "!".repeat(500))
                        .put("Product", "!".repeat(16))
                        .put("Delivery Type", "!".repeat(12))
                        .put("A-End Site Details", "!".repeat(200))
                        .put("B-End Site Details", "!".repeat(200))
                        .put("A-End Site Contact", "!".repeat(120))
                        .put("B-End Site Contact", "!".repeat(120))
                        .put("Access Speed", "!".repeat(10))
                        .put("Port Speed", "!".repeat(10))
                        .put("A-End Presentation", "!".repeat(10))
                        .put("B-End Presentation", "!".repeat(10))
                        .put("Order Manager Name", "!".repeat(40))
                        .put("Order Manager Email", "!".repeat(40))
                        .put("Site Name", "!".repeat(50))
                );

        PBTDCOrderReportDTO sampleOrderReportDTO = PBTDCOrderReportDTO.builder()
                .dateReceivedByDelivery(expectedJson.getJSONObject(0).getString("Date Received By Delivery"))
                .orderEntryAndValidationStatus(expectedJson.getJSONObject(0).getString("Order Entry and Validation Status"))
                .planningStatus(expectedJson.getJSONObject(0).getString("Planning Status"))
                .accessInstallation(expectedJson.getJSONObject(0).getString("Access Installation"))
                .testingCpeInstallation(expectedJson.getJSONObject(0).getString("Testing CPE Installation"))
                .serviceCompleteAndOperational(expectedJson.getJSONObject(0).getString("Service Complete and Operational"))
                .deliveryOnTrack(expectedJson.getJSONObject(0).getString("Delivery On Track"))
                .indicativeDeliveryDate(expectedJson.getJSONObject(0).getString("Indicative Delivery Date"))
                .deliveryDate(expectedJson.getJSONObject(0).getString("Delivery Date"))
                .notes(expectedJson.getJSONObject(0).getString("Notes"))
                .referenceIds(expectedJson.getJSONObject(0).getString("Reference IDs"))
                .product(expectedJson.getJSONObject(0).getString("Product"))
                .deliveryType(expectedJson.getJSONObject(0).getString("Delivery Type"))
                .aendSiteDetails(expectedJson.getJSONObject(0).getString("A-End Site Details"))
                .bendSiteDetails(expectedJson.getJSONObject(0).getString("B-End Site Details"))
                .aendContactDetails(expectedJson.getJSONObject(0).getString("A-End Site Contact"))
                .bendContactDetails(expectedJson.getJSONObject(0).getString("B-End Site Contact"))
                .accessSpeed(expectedJson.getJSONObject(0).getString("Access Speed"))
                .portSpeed(expectedJson.getJSONObject(0).getString("Port Speed"))
                .aendPresentation(expectedJson.getJSONObject(0).getString("A-End Presentation"))
                .bendPresentation(expectedJson.getJSONObject(0).getString("B-End Presentation"))
                .orderManager(expectedJson.getJSONObject(0).getString("Order Manager Name"))
                .orderManagerEmail(expectedJson.getJSONObject(0).getString("Order Manager Email"))
                .siteName(expectedJson.getJSONObject(0).getString("Site Name"))
                .build();

        PBTDCOrderReportSetDTO samplePbtdcOrderReportSetDTO = PBTDCOrderReportSetDTO.builder()
                        .internalReportEntries(List.of(sampleOrderReportDTO))
                        .externalReportEntries(List.of(sampleOrderReportDTO))
                        .build();

        Mockito.when(pbtdcReportPersistenceService.get(Mockito.any(), Mockito.anyString()))
                .thenReturn(Optional.of(samplePbtdcOrderReportSetDTO));

        String response = new String(
                jasperReportGeneratorService.generateJSON(LocalDate.of(2022, 01, 01), "acme", true).get());

        JSONArray responseJSON = new JSONArray(response);

        JSONAssert.assertEquals(expectedJson, responseJSON, false);
    }

    @Test
    @DisplayName("test that the optional T&Cs are not displayed when the flag is false")
    void testJasperReportGenerationWithoutTCs() throws Exception {
        PBTDCOrderReportSetDTO reportSet = SampleReportSetFactory.getSampleReportSet();

        Mockito.when(pbtdcReportPersistenceService.get(Mockito.any(), Mockito.anyString()))
                .thenReturn(Optional.of(reportSet));

        String response = new String(
                jasperReportGeneratorService.generateJSON(LocalDate.of(2022, 01, 01), "acme", true).get());

        JSONArray responseJSON = new JSONArray(response);

        Assertions.assertThat(responseJSON.getJSONObject(0).has("termsAndConditions")).isEqualTo(false);
        System.out.println(responseJSON);
    }

    @Test
    @DisplayName("test that the optional T&Cs are not displayed when the flag is true")
    void testJasperReportGenerationWithTCs() throws Exception {
        PBTDCOrderReportSetDTO reportSet = SampleReportSetFactory.getSampleReportSet();

        Mockito.when(pbtdcReportPersistenceService.get(Mockito.any(), Mockito.anyString()))
                .thenReturn(Optional.of(reportSet));

        Mockito.when(applicationConfiguration.getOaoDetails()).thenReturn(
                Map.of("acme",
                OaoDetailDTO.builder()
                        .name("acme")
                        .enableKciTCBanner(true)
                        .build())
        );

        String response = new String(
                jasperReportGeneratorService.generateJSON(LocalDate.of(2022, 01, 01), "acme", true).get());

        JSONArray responseJSON = new JSONArray(response);
        System.out.println(responseJSON);
        Assertions.assertThat(responseJSON.getJSONObject(0).getString("termsAndConditions")).isEqualTo(expectedTandCValue);

    }

    @Test
    @DisplayName("test PDF Generation")
    void testJasperReportGenerationForPDF() throws Exception{
        JSONArray expectedJson = new JSONArray()
            .put(new JSONObject()
                .put("Date Received By Delivery", "!".repeat(10))
                .put("Order Entry and Validation Status", "!".repeat(18))
                .put("Planning Status", "!".repeat(18))
                .put("Access Installation", "!".repeat(18))
                .put("Testing CPE Installation", "!".repeat(18))
                .put("Service Complete and Operational", "!".repeat(8))
                .put("Delivery On Track", "!".repeat(4))
                .put("Indicative Delivery Date", "!".repeat(10))
                .put("Delivery Date", "!".repeat(10))
                .put("Notes", "!".repeat(300))
                .put("Reference IDs", "!".repeat(500))
                .put("Product", "!".repeat(16))
                .put("Delivery Type", "!".repeat(12))
                .put("A-End Site Details", "!".repeat(200))
                .put("B-End Site Details", "!".repeat(200))
                .put("A-End Site Contact", "!".repeat(120))
                .put("B-End Site Contact", "!".repeat(120))
                .put("Access Speed", "!".repeat(10))
                .put("Port Speed", "!".repeat(10))
                .put("A-End Presentation", "!".repeat(10))
                .put("B-End Presentation", "!".repeat(10))
                .put("Order Manager Name", "!".repeat(40))
                .put("Order Manager Email", "!".repeat(40))
                .put("Site Name", "!".repeat(50))
            );

        PBTDCOrderReportDTO sampleOrderReportDTO = PBTDCOrderReportDTO.builder()
            .dateReceivedByDelivery(expectedJson.getJSONObject(0).getString("Date Received By Delivery"))
            .orderEntryAndValidationStatus(expectedJson.getJSONObject(0).getString("Order Entry and Validation Status"))
            .planningStatus(expectedJson.getJSONObject(0).getString("Planning Status"))
            .accessInstallation(expectedJson.getJSONObject(0).getString("Access Installation"))
            .testingCpeInstallation(expectedJson.getJSONObject(0).getString("Testing CPE Installation"))
            .serviceCompleteAndOperational(expectedJson.getJSONObject(0).getString("Service Complete and Operational"))
            .deliveryOnTrack(expectedJson.getJSONObject(0).getString("Delivery On Track"))
            .indicativeDeliveryDate(expectedJson.getJSONObject(0).getString("Indicative Delivery Date"))
            .deliveryDate(expectedJson.getJSONObject(0).getString("Delivery Date"))
            .notes(expectedJson.getJSONObject(0).getString("Notes"))
            .referenceIds(expectedJson.getJSONObject(0).getString("Reference IDs"))
            .product(expectedJson.getJSONObject(0).getString("Product"))
            .deliveryType(expectedJson.getJSONObject(0).getString("Delivery Type"))
            .aendSiteDetails(expectedJson.getJSONObject(0).getString("A-End Site Details"))
            .bendSiteDetails(expectedJson.getJSONObject(0).getString("B-End Site Details"))
            .aendContactDetails(expectedJson.getJSONObject(0).getString("A-End Site Contact"))
            .bendContactDetails(expectedJson.getJSONObject(0).getString("B-End Site Contact"))
            .accessSpeed(expectedJson.getJSONObject(0).getString("Access Speed"))
            .portSpeed(expectedJson.getJSONObject(0).getString("Port Speed"))
            .aendPresentation(expectedJson.getJSONObject(0).getString("A-End Presentation"))
            .bendPresentation(expectedJson.getJSONObject(0).getString("B-End Presentation"))
            .orderManager(expectedJson.getJSONObject(0).getString("Order Manager Name"))
            .orderManagerEmail(expectedJson.getJSONObject(0).getString("Order Manager Email"))
            .siteName(expectedJson.getJSONObject(0).getString("Site Name"))
            .build();

        PBTDCOrderReportSetDTO samplePbtdcOrderReportSetDTO = PBTDCOrderReportSetDTO.builder()
            .internalReportEntries(List.of(sampleOrderReportDTO))
            .externalReportEntries(List.of(sampleOrderReportDTO))
            .build();

        Mockito.when(pbtdcReportPersistenceService.get(Mockito.any(), Mockito.anyString()))
            .thenReturn(Optional.of(samplePbtdcOrderReportSetDTO));

        String response = new String(
            jasperReportGeneratorService.generatePDF(LocalDate.of(2022, 01, 01), "acme", true).get());

        Assertions.assertThat(response).isNotNull();
    }
}
