package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.domain.Qbtdc;
import com.btireland.talos.ethernet.engine.facade.QBTDCOrderMapper;
import com.btireland.talos.ethernet.engine.util.ContextFactory;
import com.btireland.talos.ethernet.engine.util.QbtdcFactory;
import com.btireland.talos.ethernet.engine.util.QuoteItemEmailDTOFactory;
import com.btireland.talos.ethernet.engine.util.TemplateEngineFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.assertj.core.api.Assertions;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class QbtdcMailServiceTest {

    @Mock
    TemplateEngine templateEngine;

    @Mock
    MailService mailService;

    @Mock
    QBTDCOrderMapper qbtdcOrderMapper;

    @Mock
    ApplicationConfiguration applicationConfiguration;

    @InjectMocks
    QbtdcMailService qbtdcMailService;

    @Captor
    ArgumentCaptor<Context> emailBodyArgumentCaptor;

    @Test
    @DisplayName("Build Email Text For Complete")
    void buildEmailTextForComplete() throws MessagingException {
        String from = "wholesalepricing@bt.com";
        String subject = "Quote Update - sky - Wholesale Ethernet Connect - BT-QBTDC-1";
        String emailText = TemplateEngineFactory.getQbtdcCompleteEmailText();
        Map<String, String> handoverMap = new HashMap<>();
        handoverMap.put("EQUINIX_DB1", "Equinix DB1 - Unit 4027 Citywest");
        ApplicationConfiguration.OfflinePricingMailConfiguration offlinePricingMailConfiguration = new ApplicationConfiguration.OfflinePricingMailConfiguration();
        offlinePricingMailConfiguration.setMailSource(from);

        Qbtdc qbtdc = QbtdcFactory.qbtdcOrderAutoCompleteEmail();

        Mockito.when(qbtdcOrderMapper.toQuoteItemEmailDTO(qbtdc.getQuoteItems().get(0))).thenReturn(QuoteItemEmailDTOFactory.completeQuoteItemEmailDTO());
        Mockito.when(applicationConfiguration.getOfflinePricingMail()).thenReturn(offlinePricingMailConfiguration);
        Mockito.when(applicationConfiguration.getHandoverMap()).thenReturn(handoverMap);
        Mockito.when(templateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn(emailText);

        List<String> recipients = List.of("joe.bloggs@bt.com");
        qbtdcMailService.generateAndSendEmail(qbtdc, recipients, null);

        Mockito.verify(templateEngine, Mockito.times(1)).process(Mockito.any(String.class), emailBodyArgumentCaptor.capture());
        Mockito.verify(mailService, Mockito.times(1)).sendMessage(subject, emailText, from, new String[]{"joe.bloggs@bt.com"}, null);

        Assertions.assertThat(emailBodyArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(ContextFactory.getCompleteEmailContext());
    }

    @Test
    @DisplayName("Build Email Text For Complete - Null properties in mail body")
    void buildEmailTextForCompleteNullPropsInMailBody() throws MessagingException {
        String from = "wholesalepricing@bt.com";
        String subject = "Quote Update - sky - Wholesale Ethernet Connect - BT-QBTDC-1";
        String emailText = TemplateEngineFactory.getQbtdcCompleteEmailTextNullMailBodyProps();
        Map<String, String> handoverMap = new HashMap<>();
        handoverMap.put("EQUINIX_DB1", "Equinix DB1 - Unit 4027 Citywest");
        ApplicationConfiguration.OfflinePricingMailConfiguration offlinePricingMailConfiguration = new ApplicationConfiguration.OfflinePricingMailConfiguration();
        offlinePricingMailConfiguration.setMailSource(from);

        Qbtdc qbtdc = QbtdcFactory.qbtdcOrderAutoCompleteEmailNullMailBodyProps();

        Mockito.when(qbtdcOrderMapper.toQuoteItemEmailDTO(qbtdc.getQuoteItems().get(0))).thenReturn(QuoteItemEmailDTOFactory.completeQuoteItemEmailDTONullMailBodyProps());
        Mockito.when(applicationConfiguration.getOfflinePricingMail()).thenReturn(offlinePricingMailConfiguration);
        Mockito.when(applicationConfiguration.getHandoverMap()).thenReturn(handoverMap);
        Mockito.when(templateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn(emailText);

        List<String> recipients = List.of("joe.bloggs@bt.com");
        qbtdcMailService.generateAndSendEmail(qbtdc, recipients, null);

        Mockito.verify(templateEngine, Mockito.times(1)).process(Mockito.any(String.class), emailBodyArgumentCaptor.capture());
        Mockito.verify(mailService, Mockito.times(1)).sendMessage(subject, emailText, from, new String[]{"joe.bloggs@bt.com"}, null);

        Assertions.assertThat(emailBodyArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(ContextFactory.getCompleteEmailContextNullMailProps());
    }

    @Test
    @DisplayName("Build Email Text For Delay")
    void buildEmailTextForDelay() throws MessagingException {
        String from = "wholesalepricing@bt.com";
        String subject = "Delayed Quote - sky - Wholesale Ethernet Connect - BT-QBTDC-1";
        String emailText = TemplateEngineFactory.getQbtdcDelayEmailText();
        Map<String, String> handoverMap = new HashMap<>();
        handoverMap.put("EQUINIX_DB1", "Equinix DB1 - Unit 4027 Citywest");
        ApplicationConfiguration.OfflinePricingMailConfiguration offlinePricingMailConfiguration = new ApplicationConfiguration.OfflinePricingMailConfiguration();
        offlinePricingMailConfiguration.setMailSource(from);

        List<String> recipients = List.of("joe.bloggs@bt.com");
        Qbtdc qbtdc = QbtdcFactory.qbtdcOrderAutoDelayEmail();

        Mockito.when(qbtdcOrderMapper.toQuoteItemEmailDTO(qbtdc.getQuoteItems().get(0))).thenReturn(QuoteItemEmailDTOFactory.delayQuoteItemEmailDTO());
        Mockito.when(applicationConfiguration.getOfflinePricingMail()).thenReturn(offlinePricingMailConfiguration);
        Mockito.when(applicationConfiguration.getHandoverMap()).thenReturn(handoverMap);
        Mockito.when(templateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn(emailText);

        qbtdcMailService.generateAndSendEmail(qbtdc,recipients, null);

        Mockito.verify(templateEngine, Mockito.times(1)).process(Mockito.any(String.class), emailBodyArgumentCaptor.capture());
        Mockito.verify(mailService, Mockito.times(1)).sendMessage(subject, emailText, from, new String[]{"joe.bloggs@bt.com"}, null);

        Assertions.assertThat(emailBodyArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(ContextFactory.getDelayEmailContext());
    }
}
