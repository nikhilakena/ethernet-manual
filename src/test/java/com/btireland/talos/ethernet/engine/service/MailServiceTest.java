package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.dto.EmailAttachmentDTO;
import com.btireland.talos.ethernet.engine.util.EmailAttachmentDTOFactory;
import com.btireland.talos.ethernet.engine.util.MimeMultipartUtil;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSendException;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
public class MailServiceTest {

    private static final GreenMail mailServer = new GreenMail(ServerSetupTest.SMTP);;

    @Autowired
    private MailService mailService;

    @BeforeAll
    private static void beforeAll(){
        mailServer.start();
    }
    @AfterAll
    private static void afterAll() {
        mailServer.stop();
    }

    @Test
    public void testSendMessage() throws Exception {
        List<EmailAttachmentDTO> attachmentDTOList = EmailAttachmentDTOFactory.defaultEmailAttachmentDTOList();

        mailService.sendMessage("my subject", "my body text", "user.sender@bt.com", new String[]{"user.recipient@bt.com"},attachmentDTOList);

        MimeMessage[] mails = mailServer.getReceivedMessages();
        Assertions.assertThat(mails).hasSize(1);
        MimeMessage mail = mails[0];
        MimeMultipart mp = (MimeMultipart) mail.getContent();
        Assertions.assertThat(mail.getFrom()[0].toString()).isEqualTo("user.sender@bt.com");
        Assertions.assertThat(mail.getRecipients(Message.RecipientType.TO)[0].toString()).isEqualTo("user.recipient@bt.com");
        Assertions.assertThat(mail.getSubject()).isEqualTo("my subject");
        Assertions.assertThat(MimeMultipartUtil.getTextFromMimeMultipart(mp)).isEqualToIgnoringNewLines("my body text");
    }

    @Test
    public void testSendMessageThrowsException() throws Exception {
        mailServer.stop();
        Assertions.assertThatThrownBy(() -> mailService.sendMessage("my subject", "my body text", "user.sender@bt.com", new String[]{"user.recipient@bt.com"},null))
                .isInstanceOf(MailSendException.class);
    }


}
