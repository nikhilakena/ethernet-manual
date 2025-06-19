package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.dto.EmailAttachmentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MailService {

    public JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMessage(String subject, String text, String sender, String[] recipients, List<EmailAttachmentDTO> attachmentList) throws MailException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setSubject(subject);
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);
        helper.setFrom(sender);
        helper.setTo(recipients);
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setSentDate(new Date());

        if(attachmentList != null) {
            for(EmailAttachmentDTO attachment: attachmentList){
                helper.addAttachment(attachment.getFileName(), attachment.getFileContent(), attachment.getContentType());
            }
        }
        log.debug("Sending email to : '" + Arrays.stream(recipients).collect(Collectors.joining()) + "' with subject : '" + message.getSubject() + "'");
        mailSender.send(message);
    }
}
