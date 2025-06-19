package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.domain.Qbtdc;
import com.btireland.talos.ethernet.engine.domain.QbtdcQuote;
import com.btireland.talos.ethernet.engine.dto.QuoteItemEmailDTO;
import com.btireland.talos.ethernet.engine.enums.CosType;
import com.btireland.talos.ethernet.engine.enums.DeliveryType;
import com.btireland.talos.ethernet.engine.enums.FrequencyType;
import com.btireland.talos.ethernet.engine.enums.Sla;
import com.btireland.talos.ethernet.engine.enums.TalosOrderStatus;
import com.btireland.talos.ethernet.engine.facade.QBTDCOrderMapper;
import com.btireland.talos.ethernet.engine.util.BTNotificationTypes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class QbtdcMailService {

    private final MailService mailService;

    private final TemplateEngine templateEngine;

    private final QBTDCOrderMapper qbtdcOrderMapper;

    private final ApplicationConfiguration applicationConfiguration;

    private static final DateTimeFormatter SLASH_DAY_FIRST_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public QbtdcMailService(MailService mailService, TemplateEngine templateEngine, QBTDCOrderMapper qbtdcOrderMapper, ApplicationConfiguration applicationConfiguration) {
        this.mailService = mailService;
        this.templateEngine = templateEngine;
        this.qbtdcOrderMapper = qbtdcOrderMapper;
        this.applicationConfiguration = applicationConfiguration;
    }

    public void generateAndSendEmail(Qbtdc order,List<String> recipients, String narrative) throws MessagingException {
            String from = applicationConfiguration.getOfflinePricingMail().getMailSource();
            String subject = buildMailSubject(order);
            Context context = buildContext(order,narrative);
            String emailText = templateEngine.process("qbtdc-mail-template", context);

            try {
                mailService.sendMessage(subject, emailText, from, recipients.toArray(String[]::new), null);
                log.info("Successfully sent email for order : " + order.getWagOrderId());
            } catch (MessagingException ex) {
                log.error("Error sending email for order: " + order.getWagOrderId(), ex);
                throw new MessagingException(ex.getMessage());
            }
    }

    private String buildMailSubject(Qbtdc qbtdcOrder) {
        StringBuilder subjectLine = new StringBuilder();
        if (TalosOrderStatus.TALOS_COMPLETE.getValue()
                .equals(qbtdcOrder.getOrderStatus()) || BTNotificationTypes.C.name()
                .equals(qbtdcOrder.getLastNotificationType())) {
            subjectLine.append("Quote Update");
        } else if (BTNotificationTypes.D.name()
                .equals(qbtdcOrder.getLastNotificationType())) {
            subjectLine.append("Delayed Quote");
        }
        subjectLine.append(" - ")
                .append(qbtdcOrder.getOao())
                .append(" - ");
        subjectLine.append(getProductDesc(qbtdcOrder.getServiceClass()))
                .append(" - ");
        subjectLine.append(qbtdcOrder.getOrderNumber());
        return subjectLine.toString();
    }

    private String getProductDesc(String product) {
        if (product.equalsIgnoreCase("WIC"))
            return "Wholesale Internet Connect";
        else if (product.equalsIgnoreCase("WEC"))
            return "Wholesale Ethernet Connect";
        else
            return null;
    }

    private Context buildContext(Qbtdc order,String narrative) {
        Context context = new Context();
        context.setVariable("customer", order.getOao());
        context.setVariable("btReference", order.getOrderNumber());
        context.setVariable("quoteInitiatedDate", order.getCreatedAt().format(SLASH_DAY_FIRST_FORMAT));
        context.setVariable("connectionType", order.getConnectionType());
        if(StringUtils.isNotBlank(narrative)){
            context.setVariable("narrative", narrative);
        }
        if (TalosOrderStatus.TALOS_COMPLETE.getValue()
                .equals(order.getOrderStatus()) ||"C".equals(order.getLastNotificationType())) {
            context.setVariable("quoteDate", order.getChangedAt().format(SLASH_DAY_FIRST_FORMAT));
            context.setVariable("status", "Complete");
        } else {
            context.setVariable("quoteDate", null);
            context.setVariable("status", "Delayed");
        }

        List<QuoteItemEmailDTO> quoteItems = new ArrayList<>();
        for (QbtdcQuote quoteItem : order.getQuoteItems()) {
            QuoteItemEmailDTO quoteItemEmailDTO = qbtdcOrderMapper.toQuoteItemEmailDTO(quoteItem);

            quoteItemEmailDTO.setRecurringFrequency(order.getRecurringFrequency());
            quoteItemEmailDTO.setServiceClass(order.getServiceClass());
            quoteItemEmailDTO.setConnectionType(order.getConnectionType());
            quoteItemEmailDTO.setGroup(quoteItem.getGroup());
            if("D".equals(order.getLastNotificationType())) {
                quoteItemEmailDTO.setStatus("Delayed");
                quoteItemEmailDTO.setReason(order.getDelayReason());
            } else {
                if("Y".equals(order.getSyncFlag()) && quoteItem.getRejectionDetails()!=null){
                    quoteItemEmailDTO.setReason(quoteItem.getRejectionDetails().getRejectReason());
                }
                else
                {
                    quoteItemEmailDTO.setReason(quoteItem.getNotes());
                 }
            }

            if (quoteItemEmailDTO.getTerm().equals("1")) {
                quoteItemEmailDTO.setTerm(quoteItemEmailDTO.getTerm() + " Year");
            } else {
                quoteItemEmailDTO.setTerm(quoteItemEmailDTO.getTerm() + " Years");
            }

            if (quoteItemEmailDTO.getBandwidth() == null) {
                quoteItemEmailDTO.setBandwidth("Existing");
                quoteItemEmailDTO.setSla(null);
            } else {
                quoteItemEmailDTO.setBandwidth(quoteItemEmailDTO.getBandwidth() + " Gb");
                quoteItemEmailDTO.setSla(Sla.getDisplayNameByCode(quoteItemEmailDTO.getSla()) + " SLA");
            }

            quoteItemEmailDTO.setAEndTargetAccessSupplier(DeliveryType.getDisplayNameByCode(quoteItemEmailDTO.getAEndTargetAccessSupplier()));


            quoteItemEmailDTO.setHandoff(applicationConfiguration.getHandoverMap().get(quoteItemEmailDTO.getHandoff()));

            if (quoteItemEmailDTO.getLogicalLinkBandwidth() != null) {
                quoteItemEmailDTO.setLogicalLinkBandwidth(quoteItemEmailDTO.getLogicalLinkBandwidth() + " Mb");
            }

            if (quoteItemEmailDTO.getLogicalLinkProfile() != null) {
                quoteItemEmailDTO.setLogicalLinkProfile(CosType.getDisplayNameByCode(quoteItem.getLogicalLink().getProfile()));
            }

            if (quoteItemEmailDTO.getRecurringFrequency() != null) {
                quoteItemEmailDTO.setRecurringFrequency(FrequencyType.getDisplayNameByCode(quoteItemEmailDTO.getRecurringFrequency()));
            }

            quoteItems.add(quoteItemEmailDTO);
        }

        context.setVariable("quoteItems", quoteItems);

        return context;
    }
}
