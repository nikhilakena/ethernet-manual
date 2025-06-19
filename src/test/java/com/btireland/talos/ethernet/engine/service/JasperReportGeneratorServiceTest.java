package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.config.JasperConfiguration;
import com.btireland.talos.ethernet.engine.dto.OaoDetailDTO;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportDTO;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportSetDTO;
import com.btireland.talos.ethernet.engine.report.PBTDCReportExporterFactory;
import com.btireland.talos.ethernet.engine.report.PBTDCReportExporterType;
import com.btireland.talos.ethernet.engine.util.OaoUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.Exporter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class JasperReportGeneratorServiceTest {
    @Mock
    PBTDCReportPersistenceService pbtdcReportPersistenceService;

    @Mock
    PBTDCReportExporterFactory pbtdcReportExporterFactory;

    @Mock
    JasperFillManager jasperFillManager;

    @Mock
    JasperCompileManager jasperCompileManager;

    @Mock
    JasperConfiguration jasperConfiguration;
    @Mock
    ApplicationConfiguration applicationConfiguration;
    @Mock
    Exporter mockExporter;

    @Mock
    JasperReport mockJasperReport;

    @Mock
    OaoUtils oaoUtils;

    @Mock
    ByteArrayInputStream mockReportInputStream;

    @Captor
    ArgumentCaptor<ByteArrayInputStream> inputStreamArgumentCaptor;
    @Captor
    ArgumentCaptor<JasperReport> jasperReportArgumentCaptor;
    @Captor
    ArgumentCaptor<HashMap> jasperFillParametersCaptor;
    @Captor
    ArgumentCaptor<PBTDCReportExporterType> exporterTypeCaptor;
    @Captor
    ArgumentCaptor<JRBeanCollectionDataSource> beanCollectionDataSourceCaptor;

    private PBTDCOrderReportSetDTO dummyReportSet;

    private void setupReporterTests() throws Exception {
        PBTDCOrderReportDTO dummyOrderInt = PBTDCOrderReportDTO.builder().orderManager("test order manager internal").build();
        PBTDCOrderReportDTO dummyOrderExt = PBTDCOrderReportDTO.builder().orderManager("test order manager external").build();

        dummyReportSet = PBTDCOrderReportSetDTO.builder()
                .internalReportEntries(List.of(dummyOrderInt))
                .externalReportEntries(List.of(dummyOrderExt))
                .build();

        Mockito.when(pbtdcReportPersistenceService.get(Mockito.any(LocalDate.class), Mockito.any(String.class)))
                .thenReturn(Optional.of(dummyReportSet));

        Mockito.when(this.pbtdcReportExporterFactory.get(Mockito.any(), Mockito.any()))
                .thenReturn(mockExporter);

        Mockito.when(this.jasperConfiguration.getJasperReport())
                .thenReturn(mockReportInputStream);

        Mockito.when(this.jasperConfiguration.getJasperFillParameters())
                .thenReturn(new HashMap<>());

        Mockito.when(this.jasperCompileManager.compile(Mockito.any(ByteArrayInputStream.class)))
                .thenReturn(mockJasperReport);

        Mockito.when(this.applicationConfiguration.getOaoDetails()).thenReturn(Map.of(
                "btpricing", OaoDetailDTO.builder()
                        .name("BT Pricing")
                        .enableKciTCBanner(true)
                        .build(),
                "vfirl", OaoDetailDTO.builder()
                        .name("Vodafone Ireland")
                        .enableKciTCBanner(false)
                        .build()
            )
        );
    }

    // test the happy path for generating a PDF report
    @Test
    @DisplayName("Test generating a PDF report")
    void testGeneratePdfReport() throws Exception {
        setupReporterTests();
        Mockito.when(oaoUtils.translateOaoName("btpricing")).thenReturn("BT Pricing");

        Map<String, Object> expectedFillParameters = Map.of(
                "reportDate", "01-01-2022",
                "oao", "BT Pricing",
                "includeTermsAndConditions", true);

        JasperReportGeneratorService testJasperReportGeneratorService = new JasperReportGeneratorService(
                this.pbtdcReportPersistenceService,
                this.pbtdcReportExporterFactory,
                this.jasperFillManager,
                this.jasperCompileManager,
                this.jasperConfiguration,
                this.applicationConfiguration,
                this.oaoUtils
        );

        // Check that the report was compiled as expected.
        Mockito.verify(jasperCompileManager).compile(inputStreamArgumentCaptor.capture());

        assertEquals(mockReportInputStream, inputStreamArgumentCaptor.getValue());

        // check that parameters are passed correctly when generating the report.
        testJasperReportGeneratorService.generatePDF(LocalDate.of(2022, 01, 01), "btpricing", true);

        Mockito.verify(jasperFillManager).fill(
                jasperReportArgumentCaptor.capture(),
                jasperFillParametersCaptor.capture(),
                beanCollectionDataSourceCaptor.capture()
        );

        Assertions.assertThat(jasperFillParametersCaptor.getValue())
                .containsExactlyInAnyOrderEntriesOf(expectedFillParameters);
        assertEquals(mockJasperReport, jasperReportArgumentCaptor.getValue());
        assertEquals(beanCollectionDataSourceCaptor.getValue().getData(), dummyReportSet.getInternalReportEntries());

        Mockito.verify(pbtdcReportExporterFactory).get(
                exporterTypeCaptor.capture(),
                Mockito.any()
        );

        assertEquals(PBTDCReportExporterType.JASPER_EXPORTER_PDF, exporterTypeCaptor.getValue());
    }

    // test the happy path for generating a XLS report
    @Test
    @DisplayName("Test generating a XLS report")
    void testGenerateXlsReport() throws Exception {
        setupReporterTests();
        Mockito.when(oaoUtils.translateOaoName("vfirl")).thenReturn("Vodafone Ireland");

        Map<String, Object> expectedFillParameters = Map.of(
                "reportDate", "01-01-2022",
                "oao", "Vodafone Ireland",
                "includeTermsAndConditions", false);

        JasperReportGeneratorService testJasperReportGeneratorService = new JasperReportGeneratorService(
                this.pbtdcReportPersistenceService,
                this.pbtdcReportExporterFactory,
                this.jasperFillManager,
                this.jasperCompileManager,
                this.jasperConfiguration,
                this.applicationConfiguration,
                this.oaoUtils

        );

        // Check that the report was compiled as expected.
        Mockito.verify(jasperCompileManager).compile(inputStreamArgumentCaptor.capture());

        assertEquals(mockReportInputStream, inputStreamArgumentCaptor.getValue());

        // check that parameters are passed correctly when generating the report.
        testJasperReportGeneratorService.generateExcel9703(LocalDate.of(2022, 01, 01), "vfirl", true);

        Mockito.verify(jasperFillManager).fill(
                jasperReportArgumentCaptor.capture(),
                jasperFillParametersCaptor.capture(),
                beanCollectionDataSourceCaptor.capture()
        );

        Assertions.assertThat(jasperFillParametersCaptor.getValue())
                .containsExactlyInAnyOrderEntriesOf(expectedFillParameters);
        assertEquals(mockJasperReport, jasperReportArgumentCaptor.getValue());
        assertEquals(beanCollectionDataSourceCaptor.getValue().getData(), dummyReportSet.getInternalReportEntries());

        Mockito.verify(pbtdcReportExporterFactory).get(
                exporterTypeCaptor.capture(),
                Mockito.any()
        );

        assertEquals(PBTDCReportExporterType.JASPER_EXPORTER_XLS, exporterTypeCaptor.getValue());
    }
    // test the happy path for generating a XLSX report
    @Test
    @DisplayName("Test generating a XLSX report")
    void testGenerateXlsxReport() throws Exception {
        setupReporterTests();
        Mockito.when(oaoUtils.translateOaoName("acme")).thenReturn("Acme");

        Map<String, Object> expectedFillParameters = Map.of(
                "reportDate", "01-01-2022",
                "oao", "Acme",
                "includeTermsAndConditions", false);

        JasperReportGeneratorService testJasperReportGeneratorService = new JasperReportGeneratorService(
                this.pbtdcReportPersistenceService,
                this.pbtdcReportExporterFactory,
                this.jasperFillManager,
                this.jasperCompileManager,
                this.jasperConfiguration,
                this.applicationConfiguration,
                this.oaoUtils
        );

        // Check that the report was compiled as expected.
        Mockito.verify(jasperCompileManager).compile(inputStreamArgumentCaptor.capture());

        assertEquals(mockReportInputStream, inputStreamArgumentCaptor.getValue());

        // check that parameters are passed correctly when generating the report.
        testJasperReportGeneratorService.generateExcel2003(LocalDate.of(2022, 01, 01), "acme", true);

        Mockito.verify(jasperFillManager).fill(
                jasperReportArgumentCaptor.capture(),
                jasperFillParametersCaptor.capture(),
                beanCollectionDataSourceCaptor.capture()
        );

        Assertions.assertThat(jasperFillParametersCaptor.getValue())
                .containsExactlyInAnyOrderEntriesOf(expectedFillParameters);
        assertEquals(mockJasperReport, jasperReportArgumentCaptor.getValue());
        assertEquals(beanCollectionDataSourceCaptor.getValue().getData(), dummyReportSet.getInternalReportEntries());

        Mockito.verify(pbtdcReportExporterFactory).get(
                exporterTypeCaptor.capture(),
                Mockito.any()
        );

        assertEquals(PBTDCReportExporterType.JASPER_EXPORTER_XLSX, exporterTypeCaptor.getValue());
    }
    // test the happy path for generating a JSON report
    @Test
    @DisplayName("Test generating a JSON report")
    void testGenerateJSONReport() throws Exception {
        setupReporterTests();
        Mockito.when(oaoUtils.translateOaoName("acme")).thenReturn("Acme");

        Map<String, Object> expectedFillParameters = Map.of(
                "reportDate", "01-01-2022",
                "oao", "Acme",
                "includeTermsAndConditions", false);

        JasperReportGeneratorService testJasperReportGeneratorService = new JasperReportGeneratorService(
                this.pbtdcReportPersistenceService,
                this.pbtdcReportExporterFactory,
                this.jasperFillManager,
                this.jasperCompileManager,
                this.jasperConfiguration,
                this.applicationConfiguration,
                this.oaoUtils
        );

        // Check that the report was compiled as expected.
        Mockito.verify(jasperCompileManager).compile(inputStreamArgumentCaptor.capture());

        assertEquals(mockReportInputStream, inputStreamArgumentCaptor.getValue());

        // check that parameters are passed correctly when generating the report.
        testJasperReportGeneratorService.generateJSON(LocalDate.of(2022, 01, 01), "acme", true);

        Mockito.verify(jasperFillManager).fill(
                jasperReportArgumentCaptor.capture(),
                jasperFillParametersCaptor.capture(),
                beanCollectionDataSourceCaptor.capture()
        );

        Assertions.assertThat(jasperFillParametersCaptor.getValue())
                .containsExactlyInAnyOrderEntriesOf(expectedFillParameters);
        assertEquals(mockJasperReport, jasperReportArgumentCaptor.getValue());
        assertEquals(beanCollectionDataSourceCaptor.getValue().getData(), dummyReportSet.getInternalReportEntries());

        Mockito.verify(pbtdcReportExporterFactory).get(
                exporterTypeCaptor.capture(),
                Mockito.any()
        );

        assertEquals(PBTDCReportExporterType.JASPER_EXPORTER_JSON, exporterTypeCaptor.getValue());
    }

    @Test
    @DisplayName("Test generating a JSON report and generating external")
    void testGenerateJSONReportWithExternalOnly() throws Exception {
        setupReporterTests();
        Mockito.when(oaoUtils.translateOaoName("acme")).thenReturn("Acme");

        Map<String, Object> expectedFillParameters = Map.of(
                "reportDate", "01-01-2022",
                "oao", "Acme",
                "includeTermsAndConditions", false);

        JasperReportGeneratorService testJasperReportGeneratorService = new JasperReportGeneratorService(
                this.pbtdcReportPersistenceService,
                this.pbtdcReportExporterFactory,
                this.jasperFillManager,
                this.jasperCompileManager,
                this.jasperConfiguration,
                this.applicationConfiguration,
                this.oaoUtils
        );

        // Check that the report was compiled as expected.
        Mockito.verify(jasperCompileManager).compile(inputStreamArgumentCaptor.capture());

        assertEquals(mockReportInputStream, inputStreamArgumentCaptor.getValue());

        // check that parameters are passed correctly when generating the report.
        testJasperReportGeneratorService.generateJSON(LocalDate.of(2022, 01, 01), "acme", false);

        Mockito.verify(jasperFillManager).fill(
                jasperReportArgumentCaptor.capture(),
                jasperFillParametersCaptor.capture(),
                beanCollectionDataSourceCaptor.capture()
        );

        Assertions.assertThat(jasperFillParametersCaptor.getValue())
                .containsExactlyInAnyOrderEntriesOf(expectedFillParameters);
        assertEquals(mockJasperReport, jasperReportArgumentCaptor.getValue());
        assertEquals(beanCollectionDataSourceCaptor.getValue().getData(), dummyReportSet.getExternalReportEntries());

        System.out.println(dummyReportSet.getExternalReportEntries());

        Mockito.verify(pbtdcReportExporterFactory).get(
                exporterTypeCaptor.capture(),
                Mockito.any()
        );
    }


    // test when there is an JRException, this can happen if there is something wrong when reading the JRXML report
    @Test
    @DisplayName("Test generating a JSON report when an exception occurs while compiling the JRXML")
    void testGenerateJSONReportJRException() throws Exception {
        Mockito.when(this.jasperConfiguration.getJasperReport())
                .thenReturn(mockReportInputStream);

        Mockito.when(this.jasperCompileManager.compile(Mockito.any(ByteArrayInputStream.class)))
                .thenThrow(new JRException("test JR exception"));

        assertThrows(IllegalStateException.class, () -> new JasperReportGeneratorService(
                this.pbtdcReportPersistenceService,
                this.pbtdcReportExporterFactory,
                this.jasperFillManager,
                this.jasperCompileManager,
                this.jasperConfiguration,
                this.applicationConfiguration,
                this.oaoUtils
        ));
    }

    // test when there is an IOException, this can happen if there is something wrong when reading the JRXML report
    @Test
    @DisplayName("Test generating a JSON report when an IO exception occurs when compiling the JRXML report")
    void testGenerateJSONReportIOException() throws Exception {
        Mockito.when(this.jasperConfiguration.getJasperReport())
                .thenReturn(mockReportInputStream);

        Mockito.when(this.jasperConfiguration.getJasperReport())
                .thenThrow(new IOException("test IO exception"));

        assertThrows(IllegalStateException.class, () -> new JasperReportGeneratorService(
                this.pbtdcReportPersistenceService,
                this.pbtdcReportExporterFactory,
                this.jasperFillManager,
                this.jasperCompileManager,
                this.jasperConfiguration,
                this.applicationConfiguration,
                this.oaoUtils
        ));
    }
}
