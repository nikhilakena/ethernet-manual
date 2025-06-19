package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportSetDTO;
import com.btireland.talos.ethernet.engine.exception.ReportGenerationException;
import com.btireland.talos.ethernet.engine.util.OaoUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class ReportEmailServiceTest {

    @Mock
    private PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService;

    @Mock
    private JasperReportGeneratorService jasperReportGeneratorService;

    @Mock
    private MailService mailService;

    @Mock
    ApplicationConfiguration applicationConfiguration;

    @Mock
    OaoUtils oaoUtils;

    @InjectMocks
    private ReportEmailService reportEmailService;

    @Captor
    ArgumentCaptor<PBTDCOrderReportSetDTO> pbtdcReportSetArgumentCaptor;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    ArgumentCaptor<LocalDateTime> localDateTimeArgumentCaptor;

    @Captor
    ArgumentCaptor<LocalDate> localDateArgumentCaptor;

    @Captor
    ArgumentCaptor<Boolean> booleanArgumentCaptor;

    @Captor
    ArgumentCaptor<List> emailAttachmentListArgumentCaptor = ArgumentCaptor.forClass(List.class);

    @Test
    @DisplayName("Check generateAndSendEmail when oao is sent in the request")
    void testGenerateAndSendEmail() throws ReportGenerationException, MessagingException {
        byte[] excelReport ="This is an excel report".getBytes();
        byte[] pdfReport = "This is a pdf report".getBytes();
        ApplicationConfiguration.ReportConfiguration reportConfiguration = new ApplicationConfiguration.ReportConfiguration();
        reportConfiguration.setMailSource("user-sender@bt.com");
        reportConfiguration.setMailDestination("user-recipeint@bt.com");
        reportConfiguration.setActiveTimePeriod(7);
        Mockito.when(applicationConfiguration.getReport()).thenReturn(reportConfiguration);
        Mockito.when(oaoUtils.translateOaoName(anyString())).thenReturn("SKY");
        Mockito.when(jasperReportGeneratorService.generateExcel2003(any(LocalDate.class), anyString(), any(Boolean.class))).thenReturn(Optional.of(excelReport));
        Mockito.when(jasperReportGeneratorService.generatePDF(any(LocalDate.class), anyString(), any(Boolean.class))).thenReturn(Optional.of(pdfReport));

        reportEmailService.generateAndSendEmail(Optional.of("sky"), Optional.of(LocalDate.now()));

        Mockito.verify(jasperReportGeneratorService).generateExcel2003(localDateArgumentCaptor.capture(),
                stringArgumentCaptor.capture(), anyBoolean());

        Mockito.verify(jasperReportGeneratorService).generatePDF(localDateArgumentCaptor.capture(),
                stringArgumentCaptor.capture(), anyBoolean());

        Mockito.verify(mailService).sendMessage(anyString(), anyString(), anyString(), any(String[].class), anyList());

    }

    @Test
    @DisplayName("Check generateAndSendEmail when the oao is not sent in the request")
    void testGenerateAndSendEmailWithOutOaoArgument() throws ReportGenerationException, MessagingException {
        byte[] excelReport = "This is an excel report".getBytes();
        byte[] pdfReport = "This is a pdf report".getBytes();

        ApplicationConfiguration.ReportConfiguration reportConfiguration = new ApplicationConfiguration.ReportConfiguration();
        reportConfiguration.setMailSource("user-sender@bt.com");
        reportConfiguration.setMailDestination("user-recipeint@bt.com");
        reportConfiguration.setActiveTimePeriod(7);
        Mockito.when(applicationConfiguration.getReport()).thenReturn(reportConfiguration);
        Mockito.when(oaoUtils.translateOaoName(anyString())).thenReturn("SKY");
        Mockito.when(jasperReportGeneratorService.generateExcel2003(any(LocalDate.class), anyString(), any(Boolean.class))).thenReturn(Optional.of(excelReport));
        Mockito.when(jasperReportGeneratorService.generatePDF(any(LocalDate.class), anyString(), any(Boolean.class))).thenReturn(Optional.of(pdfReport));

        reportEmailService.generateAndSendEmail(Optional.of("sky"), Optional.of(LocalDate.now()));

        Mockito.verify(jasperReportGeneratorService).generateExcel2003(localDateArgumentCaptor.capture(),
                anyString(), anyBoolean());

        Mockito.verify(jasperReportGeneratorService).generatePDF(localDateArgumentCaptor.capture(),
                anyString(), anyBoolean());

        Mockito.verify(mailService).sendMessage(anyString(), anyString(), anyString(), any(String[].class), anyList());
    }

    @Test
    @DisplayName("Check generateAndSendEmail when no report available")
    void testGenerateAndSendEmailNoReportAvailable() throws ReportGenerationException, MessagingException {
        Mockito.when(jasperReportGeneratorService.generateExcel2003(any(LocalDate.class), anyString(), any(Boolean.class))).thenReturn(Optional.empty());
        Mockito.when(jasperReportGeneratorService.generatePDF(any(LocalDate.class), anyString(), any(Boolean.class))).thenReturn(Optional.empty());
        Mockito.when(oaoUtils.translateOaoName(anyString())).thenReturn("SKY");
        ApplicationConfiguration.ReportConfiguration reportConfiguration = new ApplicationConfiguration.ReportConfiguration();
        reportConfiguration.setActiveTimePeriod(7);
        Mockito.when(applicationConfiguration.getReport()).thenReturn(reportConfiguration);

        reportEmailService.generateAndSendEmail(Optional.of("sky"), Optional.of(LocalDate.now()));

        Mockito.verify(jasperReportGeneratorService).generateExcel2003(localDateArgumentCaptor.capture(),
                stringArgumentCaptor.capture(), anyBoolean());

        Mockito.verify(jasperReportGeneratorService).generatePDF(localDateArgumentCaptor.capture(),
                stringArgumentCaptor.capture(), anyBoolean());

        Mockito.verify(mailService, Mockito.times(0)).sendMessage(anyString(), anyString(), anyString(), any(String[].class), anyList());
    }
}
