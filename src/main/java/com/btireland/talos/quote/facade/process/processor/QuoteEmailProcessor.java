package com.btireland.talos.quote.facade.process.processor;

import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.BT_REFERENCE;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.CONNECTION_TYPE;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.CUSTOMER;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.DELAYED_QUOTE;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.HYPHEN;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.NARRATIVE;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.ORDER_STATUS_COMPLETE;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.ORDER_STATUS_DELYED;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.QBTDC_MAIL_TEMPLATE;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.QOUTE_ITEMS;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.QUOTE_DATE;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.QUOTE_INITIATED_DATE;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.QUOTE_UPDATE;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.STATUS;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.WHOLESALE_ETHERNET_CONNECT;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.WHOLESALE_INTERNET_CONNECT;
import static com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants.BT_DATE_WITHOUT_TIME_FORMAT;

import com.btireland.talos.core.common.rest.exception.checked.TalosInternalErrorException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.dto.QBTDCEmailRequest;
import com.btireland.talos.ethernet.engine.dto.QuoteItemEmailDTO;
import com.btireland.talos.ethernet.engine.service.MailService;
import com.btireland.talos.ethernet.engine.service.QBTDCManualMailService;
import com.btireland.talos.quote.facade.base.enumeration.internal.ErrorCode;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.btireland.talos.quote.facade.base.enumeration.internal.ServiceClassType;
import com.btireland.talos.quote.facade.connector.rest.quotemanager.QuoteManagerClient;
import com.btireland.talos.quote.facade.domain.dao.QuoteOrderMapRepository;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.quotemanager.response.email.QuoteGroupEmailResponse;
import com.btireland.talos.quote.facade.process.mapper.quotemanager.QuoteGroupEmailResponseMapper;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class QuoteEmailProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteEmailProcessor.class);

  private final MailService mailService;

  private final QuoteManagerClient quoteManagerClient;

  private final QuoteOrderMapRepository quoteOrderMapRepository;

  private final ApplicationConfiguration applicationConfiguration;

  private final TemplateEngine templateEngine;

  private final QBTDCManualMailService qbtdcManualMailService;

  public QuoteEmailProcessor(MailService mailService, QuoteManagerClient quoteManagerClient,
      ApplicationConfiguration applicationConfiguration, QuoteOrderMapRepository quoteOrderMapRepository,
      TemplateEngine templateEngine,
      QBTDCManualMailService qbtdcManualMailService) {
    this.mailService = mailService;
    this.quoteManagerClient = quoteManagerClient;
    this.applicationConfiguration = applicationConfiguration;
    this.quoteOrderMapRepository = quoteOrderMapRepository;
    this.templateEngine = templateEngine;
    this.qbtdcManualMailService = qbtdcManualMailService;
  }

  /**
   * Method for generating and sending the qbtdc Order Mail
   *
   * @param qbtdcEmailRequest QbtdcEmailRequest received from NOG
   */
  public void sendQuoteMail(QBTDCEmailRequest qbtdcEmailRequest) throws TalosNotFoundException,
          TalosInternalErrorException {
    Optional<QuoteOrderMapEntity> quoteMap = quoteOrderMapRepository
        .findByOrderNumberAndSupplierAndStatusIn(qbtdcEmailRequest.getOrderNumber(),
            qbtdcEmailRequest.getSupplier(), List.of(QuoteOrderMapStatus.COMPLETE, QuoteOrderMapStatus.DELAY));

    if(quoteMap.isPresent()) {
      QuoteOrderMapEntity quoteOrderMap = quoteMap.get();
      QuoteGroupEmailResponse quoteGroupEmailResponse = quoteManagerClient
          .getQuoteGroupEmail(quoteOrderMap.getQuoteGroupId());

      String from = applicationConfiguration.getOfflinePricingMail().getMailSource();
      String subject = buildMailSubject(quoteOrderMap, qbtdcEmailRequest.getSupplier(),
          quoteGroupEmailResponse.getServiceClass());
      Context context = buildContext(quoteGroupEmailResponse, quoteOrderMap, qbtdcEmailRequest.getNarrative());
      String emailText = templateEngine.process(QBTDC_MAIL_TEMPLATE, context);
      try {
        mailService
            .sendMessage(subject, emailText, from, qbtdcEmailRequest.getEmailRecipients().toArray(String[]::new), null);
        LOGGER.info("Successfully sent email for group id: " + quoteOrderMap.getQuoteGroupId());
      } catch (MessagingException ex) {
        LOGGER.error("Error sending email for group id: " + quoteOrderMap.getQuoteGroupId(), ex);
        throw new TalosInternalErrorException(LOGGER, ErrorCode.MESSAGING_ERROR.name(), String.format("Email sending " +
                "failed " +
                "for %d",
            quoteOrderMap.getQuoteGroupId()));
      }
    } else {
        qbtdcManualMailService.sendQBTDCEmail(qbtdcEmailRequest);
    }
  }

  /**
   * Method for Building the Mail Subject
   *
   * @param quoteOrderMap QuoteOrderMap object containing orderNumber, GroupId
   * @param supplier      Supplier of the Order
   * @param serviceClass  ServiceClass of the Order
   * @return String Mail Subject
   */
  private String buildMailSubject(QuoteOrderMapEntity quoteOrderMap, String supplier, ServiceClassType serviceClass) {
    StringBuilder subjectLine = new StringBuilder();
    if (quoteOrderMap.getStatus() == QuoteOrderMapStatus.COMPLETE) {
      subjectLine.append(QUOTE_UPDATE);
    } else if (quoteOrderMap.getStatus() == QuoteOrderMapStatus.DELAY) {
      subjectLine.append(DELAYED_QUOTE);
    }
    subjectLine.append(HYPHEN)
        .append(supplier)
        .append(HYPHEN);
    subjectLine.append(getProductDesc(serviceClass.name()))
        .append(HYPHEN);
    subjectLine.append(quoteOrderMap.getOrderNumber());
    return subjectLine.toString();
  }

  /**
   * Method for getting Produst Description
   *
   * @param product ServiceClass of the Order
   * @return String Product Description
   */
  private String getProductDesc(String product) {
    if (product.equalsIgnoreCase(ServiceClassType.WIC.name())) {
      return WHOLESALE_INTERNET_CONNECT;
    } else if (product.equalsIgnoreCase(ServiceClassType.WEC.name())) {
      return WHOLESALE_ETHERNET_CONNECT;
    } else {
      return null;
    }
  }

  /**
   * Method to Build the context for the Email templete engine
   *
   * @param quoteGroupEmailResponse QuoteGroupEmailResponse received from Quote Manager
   * @param quoteOrderMap           QuoteOrderMap object containing orderNumber, GroupId
   * @param narrative               Email Text
   * @return Context for the Email Template Engine
   */
  private Context buildContext(QuoteGroupEmailResponse quoteGroupEmailResponse, QuoteOrderMapEntity quoteOrderMap,
      String narrative) throws TalosNotFoundException {
    Context context = new Context();
    context.setVariable(CUSTOMER, quoteOrderMap.getSupplier());
    context.setVariable(BT_REFERENCE, quoteOrderMap.getOrderNumber());
    context.setVariable(CONNECTION_TYPE, quoteGroupEmailResponse.getQuoteEmails().get(0).getConnectionType());
    context
        .setVariable(QUOTE_INITIATED_DATE, quoteGroupEmailResponse.getQuoteDate().format(BT_DATE_WITHOUT_TIME_FORMAT));
    if (StringUtils.isNotBlank(narrative)) {
      context.setVariable(NARRATIVE, narrative);
    }
    if (quoteOrderMap.getStatus() == QuoteOrderMapStatus.COMPLETE) {
      context.setVariable(QUOTE_DATE, quoteGroupEmailResponse.getQuoteCompletionDate().format(BT_DATE_WITHOUT_TIME_FORMAT));
      context.setVariable(STATUS, ORDER_STATUS_COMPLETE);
    } else {
      context.setVariable(QUOTE_DATE, null);
      context.setVariable(STATUS, ORDER_STATUS_DELYED);
    }

    List<QuoteItemEmailDTO> quoteItems = QuoteGroupEmailResponseMapper
        .createQuoteItemEmailList(quoteGroupEmailResponse, quoteOrderMap, applicationConfiguration.getHandoverMap());

    context.setVariable(QOUTE_ITEMS, quoteItems);

    return context;
  }
}
