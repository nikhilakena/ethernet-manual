package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.rest.exception.InternalErrorException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.domain.Qbtdc;
import com.btireland.talos.ethernet.engine.dto.QBTDCEmailRequest;
import com.btireland.talos.ethernet.engine.enums.TalosOrderStatus;
import com.btireland.talos.ethernet.engine.util.BTNotificationTypes;
import com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class QBTDCManualMailService {

    Logger LOGGER = LoggerFactory.getLogger(QBTDCManualMailService.class);

    private final QbtdcMailService qbtdcMailService;

    private final QbtdcOrdersPersistenceService ordersPersistenceService;

    public QBTDCManualMailService(QbtdcOrdersPersistenceService ordersPersistenceService,
                                  QbtdcMailService qbtdcMailService) {
        this.ordersPersistenceService = ordersPersistenceService;
        this.qbtdcMailService = qbtdcMailService;
    }

    public void sendQBTDCEmail(QBTDCEmailRequest qbtdcEmailRequest)
        throws InternalServerErrorException, TalosNotFoundException {
        LOGGER.info("Quote Email request for Order Number %s is being processed by Ethernet Engine",
            qbtdcEmailRequest.getOrderNumber());
        Qbtdc qbtdcOrder = ordersPersistenceService.findByOrderNumberAndOao(qbtdcEmailRequest.getOrderNumber(),
            qbtdcEmailRequest.getSupplier());
        if (qbtdcOrder != null && isCompletedOrDelayed(qbtdcOrder)) {
            try {
                qbtdcMailService.generateAndSendEmail(qbtdcOrder,qbtdcEmailRequest.getEmailRecipients(),
                                                      qbtdcEmailRequest.getNarrative());
                LOGGER.info("Successfully sent email for the order: " + qbtdcOrder.getWagOrderId());
            } catch (MessagingException ex) {
                LOGGER.error("Error sending email for the order: " + qbtdcOrder.getWagOrderId());
                throw new InternalErrorException(ex.getMessage(), ex);
            }
        } else {
            LOGGER.error("No QBTDC Completed or Delayed Order found with number {}",
                qbtdcEmailRequest.getOrderNumber()+" oao "+qbtdcEmailRequest.getSupplier());
            throw new TalosNotFoundException(LOGGER, QuoteManagerConstants.QUOTE_ORDER_NOT_FOUND,
                String.format("Order not found for order number %s and supplier %s",
                    qbtdcEmailRequest.getOrderNumber(), qbtdcEmailRequest.getSupplier()));
        }
    }



    private boolean isCompletedOrDelayed(Qbtdc qbtdcOrder) {
        return TalosOrderStatus.TALOS_COMPLETE.getValue()
                .equals(qbtdcOrder.getOrderStatus()) || TalosOrderStatus.DELAYED.getValue()
                .equals(qbtdcOrder.getOrderStatus()) || (BTNotificationTypes.C.name()
                .equals(qbtdcOrder.getLastNotificationType()) || BTNotificationTypes.D.name()
                .equals(qbtdcOrder.getLastNotificationType()));

    }

}
