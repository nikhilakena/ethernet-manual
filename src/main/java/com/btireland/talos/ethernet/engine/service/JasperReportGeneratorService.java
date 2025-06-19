package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.config.JasperConfiguration;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportDTO;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportSetDTO;
import com.btireland.talos.ethernet.engine.exception.ReportGenerationException;
import com.btireland.talos.ethernet.engine.report.PBTDCReportExporterFactory;
import com.btireland.talos.ethernet.engine.report.PBTDCReportExporterType;

import com.btireland.talos.ethernet.engine.util.OaoUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class JasperReportGeneratorService {
    private static final String oaoParameter = "oao";
    private static final String reportDateParameter = "reportDate";
    private static final String reportDateParameterPattern = "dd-MM-yyyy";
    private static final String includeTermsAndConditionsPattern = "includeTermsAndConditions";
    private PBTDCReportPersistenceService pbtdcReportPersistenceService;

    private PBTDCReportExporterFactory pbtdcReportExporterFactory;

    private JasperConfiguration jasperConfiguration;

    private JasperReport compiledReport;

    private JasperFillManager jasperFillManager;

    private JasperCompileManager jasperCompileManager;

    private ApplicationConfiguration applicationConfiguration;

    private OaoUtils oaoUtils;

    public JasperReportGeneratorService(PBTDCReportPersistenceService pbtdcReportPersistenceService,
                                        PBTDCReportExporterFactory pbtdcReportExporterFactory,
                                        JasperFillManager jasperFillManager,
                                        JasperCompileManager jasperCompileManager,
                                        JasperConfiguration jasperConfiguration,
                                        ApplicationConfiguration applicationConfiguration,
                                        OaoUtils oaoUtils

    ) {
        this.pbtdcReportPersistenceService = pbtdcReportPersistenceService;
        this.pbtdcReportExporterFactory = pbtdcReportExporterFactory;
        this.jasperConfiguration = jasperConfiguration;
        this.jasperFillManager = jasperFillManager;
        this.jasperCompileManager = jasperCompileManager;
        this.applicationConfiguration = applicationConfiguration;
        this.oaoUtils = oaoUtils;

        /* Compile the JRXML Jasper Report into the internal representation. We only need to do this once
           at startup since it will never change while the service is running.
         */
        try {
            this.compiledReport = this.jasperCompileManager.compile(jasperConfiguration.getJasperReport());
        } catch (JRException | IOException e){
            throw new IllegalStateException("Fatal error while compiling report :" + e.getMessage());
        }
    }

    public Optional<byte[]> generateExcel2003(LocalDate date, String oao, Boolean internal) throws ReportGenerationException {
        return generateReport(date, oao, internal, PBTDCReportExporterType.JASPER_EXPORTER_XLSX);
    }

    public Optional<byte[]> generateExcel9703(LocalDate date, String oao, Boolean internal) throws ReportGenerationException {
        return generateReport(date, oao, internal, PBTDCReportExporterType.JASPER_EXPORTER_XLS);
    }

    public Optional<byte[]> generatePDF(LocalDate date, String oao, Boolean internal) throws ReportGenerationException {
        return generateReport(date, oao, internal, PBTDCReportExporterType.JASPER_EXPORTER_PDF);
    }

    public Optional<byte[]> generateJSON(LocalDate date, String oao, Boolean internal) throws ReportGenerationException {
        return generateReport(date, oao, internal, PBTDCReportExporterType.JASPER_EXPORTER_JSON);
    }

    private Optional<byte[]> generateReport(
            LocalDate date, String oao, Boolean internal, PBTDCReportExporterType type) throws ReportGenerationException {
        Optional<PBTDCOrderReportSetDTO> reportSet = pbtdcReportPersistenceService.get(date, oao.toLowerCase());

        if(reportSet.isPresent()) {
            var reportParameters = this.jasperConfiguration.getJasperFillParameters();
            reportParameters.put(reportDateParameter, date.format(DateTimeFormatter.ofPattern(reportDateParameterPattern)));
            reportParameters.put(oaoParameter, oaoUtils.translateOaoName(oao));
            reportParameters.put(includeTermsAndConditionsPattern, isIncludeTermsAndConditions(oao));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                Exporter exporter = pbtdcReportExporterFactory.get(type, outputStream);
                List<PBTDCOrderReportDTO> reportEntries;
                if(internal == true) {
                    reportEntries = reportSet.get().getInternalReportEntries();
                }else {
                    reportEntries = reportSet.get().getExternalReportEntries();
                }
                exporter.setExporterInput(new SimpleExporterInput(this.jasperFillManager.fill(
                        this.compiledReport,
                        reportParameters,
                        new JRBeanCollectionDataSource(reportEntries))));
                exporter.exportReport();
            } catch (JRException exception) {
                throw new ReportGenerationException("Jasper exception while generating report : " + exception.getMessage());
            }
            return Optional.of(outputStream.toByteArray());
        }else {
            return Optional.empty();
        }
    }

    private Boolean isIncludeTermsAndConditions(String oao){
        if(applicationConfiguration.getOaoDetails().containsKey(oao)){
            return applicationConfiguration.getOaoDetails().get(oao).getEnableKciTCBanner();
        }else{
            return false;
        }
    }
}
