package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.domain.Qbtdc;
import com.btireland.talos.ethernet.engine.util.QbtdcFactory;
import com.btireland.talos.ethernet.engine.util.TemplateEngineFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
public class QbtdcMailServiceITest {

    @Autowired
    private QbtdcMailService qbtdcMailService;

    @MockBean
    private MailService mailService;

    @Captor
    ArgumentCaptor<String> emailSubjectArgumentCaptor;

    @Captor
    ArgumentCaptor<String> emailBodyArgumentCaptor;

    @Captor
    ArgumentCaptor<String> emailFromArgumentCaptor;

    @Captor
    ArgumentCaptor<String[]> emailToArgumentCaptor;

    @Test
    @DisplayName("Build Email Text For Complete")
    void buildEmailTextForComplete() throws MessagingException {
        String from = "wholesalepricing@bt.com";
        String subject = "Quote Update - sky - Wholesale Ethernet Connect - BT-QBTDC-1";
        String[] to = new String[]{"joe.bloggs@bt.com"};
        String emailText = TemplateEngineFactory.getQbtdcCompleteEmailText();

        Qbtdc qbtdc = QbtdcFactory.qbtdcOrderAutoCompleteEmail();

        qbtdcMailService.generateAndSendEmail(qbtdc, Arrays.asList(to),null);

        Mockito.verify(mailService, Mockito.times(1)).sendMessage(emailSubjectArgumentCaptor.capture(), emailBodyArgumentCaptor.capture(), emailFromArgumentCaptor.capture(), emailToArgumentCaptor.capture(), any());

        Assertions.assertThat(emailSubjectArgumentCaptor.getValue()).isEqualTo(subject);
        Assertions.assertThat(emailBodyArgumentCaptor.getValue()).isEqualToIgnoringWhitespace(emailText);
        Assertions.assertThat(emailFromArgumentCaptor.getValue()).isEqualTo(from);
        Assertions.assertThat(emailToArgumentCaptor.getValue()).isEqualTo(to);
    }

    @Test
    @DisplayName("Build Email Text For Complete - SINGLE LINE")
    void buildEmailTextForSingleLineComplete() throws MessagingException {
        String from = "wholesalepricing@bt.com";
        String subject = "Quote Update - sky - Wholesale Ethernet Connect - BT-QBTDC-1";
        String[] to = new String[]{"joe.bloggs@bt.com"};
        String emailText = TemplateEngineFactory.getQbtdcCompleteEmailTextForSingleLine();

        Qbtdc qbtdc = QbtdcFactory.qbtdcOrderAutoCompleteForSingleLineEmail();

        qbtdcMailService.generateAndSendEmail(qbtdc, Arrays.asList(to),null);

        Mockito.verify(mailService, Mockito.times(1)).sendMessage(emailSubjectArgumentCaptor.capture(), emailBodyArgumentCaptor.capture(), emailFromArgumentCaptor.capture(), emailToArgumentCaptor.capture(), any());

        Assertions.assertThat(emailSubjectArgumentCaptor.getValue()).isEqualTo(subject);
        Assertions.assertThat(emailBodyArgumentCaptor.getValue()).isEqualToIgnoringWhitespace(emailText);
        Assertions.assertThat(emailFromArgumentCaptor.getValue()).isEqualTo(from);
        Assertions.assertThat(emailToArgumentCaptor.getValue()).isEqualTo(to);
    }

    @Test
    @DisplayName("Build Email Text For Complete - null email props")
    void buildEmailTextForCompleteNullEmailProps() throws MessagingException {
        String from = "wholesalepricing@bt.com";
        String subject = "Quote Update - sky - Wholesale Ethernet Connect - BT-QBTDC-1";
        String[] to = new String[]{"joe.bloggs@bt.com"};
        String emailText = TemplateEngineFactory.getQbtdcCompleteEmailTextNullMailBodyProps();

        Qbtdc qbtdc = QbtdcFactory.qbtdcOrderAutoCompleteEmailNullMailBodyProps();

        qbtdcMailService.generateAndSendEmail(qbtdc, Arrays.asList(to),null);

        Mockito.verify(mailService, Mockito.times(1)).sendMessage(emailSubjectArgumentCaptor.capture(), emailBodyArgumentCaptor.capture(), emailFromArgumentCaptor.capture(), emailToArgumentCaptor.capture(), any());

        Assertions.assertThat(emailSubjectArgumentCaptor.getValue()).isEqualTo(subject);
        Assertions.assertThat(emailBodyArgumentCaptor.getValue()).isEqualToIgnoringWhitespace(emailText);
        Assertions.assertThat(emailFromArgumentCaptor.getValue()).isEqualTo(from);
        Assertions.assertThat(emailToArgumentCaptor.getValue()).isEqualTo(to);
    }

    @Test
    @DisplayName("Build Email Text For Delay")
    void buildEmailTextForDelay() throws MessagingException {
        String from = "wholesalepricing@bt.com";
        String subject = "Delayed Quote - sky - Wholesale Ethernet Connect - BT-QBTDC-1";
        String[] to = new String[]{"joe.bloggs@bt.com"};
        String emailText = TemplateEngineFactory.getQbtdcDelayEmailText();

        Qbtdc qbtdc = QbtdcFactory.qbtdcOrderAutoDelayEmail();

        qbtdcMailService.generateAndSendEmail(qbtdc,Arrays.asList(to),null);

        Mockito.verify(mailService, Mockito.times(1)).sendMessage(emailSubjectArgumentCaptor.capture(), emailBodyArgumentCaptor.capture(), emailFromArgumentCaptor.capture(), emailToArgumentCaptor.capture(), any());

        Assertions.assertThat(emailSubjectArgumentCaptor.getValue()).isEqualTo(subject);
        Assertions.assertThat(emailBodyArgumentCaptor.getValue()).isEqualToIgnoringWhitespace(emailText);
        Assertions.assertThat(emailFromArgumentCaptor.getValue()).isEqualTo(from);
        Assertions.assertThat(emailToArgumentCaptor.getValue()).isEqualTo(to);
    }

    @Test
    @DisplayName("Build Email Text For Complete for email All API")
    void buildEmailTextForCompleteForEmailAll() throws MessagingException {
        String from = "wholesalepricing@bt.com";
        String subject = "Quote Update - sky - Wholesale Ethernet Connect - BT-QBTDC-1";
        String[] to = new String[]{"joe.bloggs@bt.com"};
        String emailText = TemplateEngineFactory.getQbtdcCompleteEmailTextForEmailAll();

        Qbtdc qbtdc = QbtdcFactory.qbtdcOrderForEmailAll();

        qbtdcMailService.generateAndSendEmail(qbtdc, Arrays.asList(to),"narrative");

        Mockito.verify(mailService, Mockito.times(1)).sendMessage(emailSubjectArgumentCaptor.capture(), emailBodyArgumentCaptor.capture(), emailFromArgumentCaptor.capture(), emailToArgumentCaptor.capture(), any());

        Assertions.assertThat(emailSubjectArgumentCaptor.getValue()).isEqualTo(subject);
        Assertions.assertThat(emailBodyArgumentCaptor.getValue()).isEqualToIgnoringWhitespace(emailText);
        Assertions.assertThat(emailFromArgumentCaptor.getValue()).isEqualTo(from);
        Assertions.assertThat(emailToArgumentCaptor.getValue()).isEqualTo(to);
    }
}
