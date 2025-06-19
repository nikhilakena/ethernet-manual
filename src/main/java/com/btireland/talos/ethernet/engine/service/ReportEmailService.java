package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.dto.EmailAttachmentDTO;
import com.btireland.talos.ethernet.engine.exception.ReportGenerationException;
import com.btireland.talos.ethernet.engine.util.OaoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ReportEmailService {

    private PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService;
    private JasperReportGeneratorService jasperReportGeneratorService;
    private MailService mailService;
    private ApplicationConfiguration applicationConfiguration;
    private OaoUtils oaoUtils;

    public ReportEmailService(PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService,
                              JasperReportGeneratorService jasperReportGeneratorService,
                              MailService mailService,
                              ApplicationConfiguration applicationConfiguration,
                              OaoUtils oaoUtils){
        this.pbtdcOrdersPersistenceService = pbtdcOrdersPersistenceService;
        this.jasperReportGeneratorService = jasperReportGeneratorService;
        this.mailService = mailService;
        this.applicationConfiguration = applicationConfiguration;
        this.oaoUtils = oaoUtils;
    }

    public void generateAndSendEmail(Optional<String> oao, Optional<LocalDate> date) throws ReportGenerationException, MessagingException {
        LocalDate reportDate = date.orElse(LocalDate.now());

        // get a list of OAOs with active orders
        LocalDateTime timeFrame = LocalDateTime.now().minusDays(applicationConfiguration.getReport().getActiveTimePeriod());
        List<String> oaos = new ArrayList<>();
        oao.ifPresentOrElse(
                (v) -> { oaos.add(v);},
                ()  -> { oaos.addAll(pbtdcOrdersPersistenceService.findOaosWithActiveOrders(timeFrame));}
        );

        if(oaos.isEmpty()){
            log.info("No oao available for sending the report email");
        }

        for(String oaoName : oaos) {
            Optional<byte[]> excelReport = jasperReportGeneratorService.generateExcel2003(reportDate, oaoName, true);
            Optional<byte[]> pdfReport = jasperReportGeneratorService.generatePDF(reportDate, oaoName, true);

            String printableOaoName = oaoUtils.translateOaoName(oaoName).toLowerCase().replaceAll(" ", "-");
            String fileName = printableOaoName + "-" + reportDate;

            List<EmailAttachmentDTO> attachments = new ArrayList<>();

            pdfReport.ifPresent(report -> attachments.add(EmailAttachmentDTO.builder()
                    .fileContent(new ByteArrayResource(report))
                    .fileName(fileName + ".pdf")
                    .contentType("application/pdf")
                    .build()));

            excelReport.ifPresent(report -> attachments.add(EmailAttachmentDTO.builder()
                    .fileContent(new ByteArrayResource(report))
                    .fileName(fileName + ".xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .build()));

            if (attachments.size() > 0) {
                try {
                    mailService.sendMessage(oaoName, reportDate.toString(), applicationConfiguration.getReport().getMailSource(), new String[]{applicationConfiguration.getReport().getMailDestination()}, attachments);
                    log.info("Successfully sent report email for the oao: " + printableOaoName);
                } catch (Exception ex) {
                    log.error("Error sending report email for the oao: " + printableOaoName, ex);
                }
            } else {
                log.info("No reports available for OAO : " + printableOaoName);
            }
        }
    }
}
