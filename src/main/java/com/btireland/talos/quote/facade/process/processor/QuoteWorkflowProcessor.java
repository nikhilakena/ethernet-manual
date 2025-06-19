package com.btireland.talos.quote.facade.process.processor;

import com.btireland.talos.core.common.rest.exception.checked.TalosInternalErrorException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.dto.QBTDCEmailRequest;
import com.btireland.talos.ethernet.engine.mq.CerberusDataSyncMessageProducer;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.btireland.talos.quote.facade.connector.rest.quotemanager.QuoteManagerClient;
import com.btireland.talos.quote.facade.domain.entity.QuoteEmailEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.quotemanager.request.offlinepricing.SearchQuoteRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteGroupResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.SearchQuoteResponse;
import com.btireland.talos.quote.facade.process.helper.QuoteHelper;
import com.btireland.talos.quote.facade.process.mapper.ordermanager.OrderManagerRequestMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteWorkflowProcessor {

    private final QuoteManagerClient quoteManagerClient;
    private final NotComNotificationsProcessor notificationsProcessor;
    private final QuoteHelper quoteHelper;
    private final CerberusDataSyncMessageProducer dataSyncMessageProducer;
    private final QuoteEmailProcessor quoteEmailProcessor;

    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteWorkflowProcessor.class);

    public QuoteWorkflowProcessor(QuoteManagerClient quoteManagerClient,
                                  NotComNotificationsProcessor notificationsProcessor, QuoteHelper quoteHelper,
                                  CerberusDataSyncMessageProducer dataSyncMessageProducer,
                                  QuoteEmailProcessor quoteEmailProcessor) {
        this.quoteManagerClient = quoteManagerClient;
        this.notificationsProcessor = notificationsProcessor;
        this.quoteHelper = quoteHelper;
        this.dataSyncMessageProducer = dataSyncMessageProducer;
        this.quoteEmailProcessor = quoteEmailProcessor;
    }

    /**
     * Sends delayed notification to notcom.
     *
     * @param groupId the quote group id
     * @throws TalosNotFoundException      the talos not found exception
     * @throws JsonProcessingException     the json processing exception
     * @throws TalosInternalErrorException the talos internal error exception
     */
    public void sendDelayedNotification(Long groupId) throws TalosNotFoundException, JsonProcessingException,
        TalosInternalErrorException {
        SearchQuoteRequest searchQuoteRequest = new SearchQuoteRequest(List.of(groupId));
        //Retrieves quote group details from Quote Manager
        SearchQuoteResponse searchQuoteResponse = quoteManagerClient.searchQuotes(searchQuoteRequest);
        QuoteOrderMapEntity quoteOrderMap = quoteHelper.fetchOrderMapById(groupId);
        if (!searchQuoteResponse.getQuoteGroups().isEmpty()) {
            notificationsProcessor.createDelayNotification(searchQuoteResponse.getQuoteGroups().get(0), quoteOrderMap);
        }
        quoteOrderMap.setStatus(QuoteOrderMapStatus.DELAY);
        quoteHelper.saveQuoteOrderMap(quoteOrderMap);
    }

    /**
     * Sends complete notification to notcom.
     *
     * @param groupId the quote group id
     * @throws TalosNotFoundException      the talos not found exception
     * @throws JsonProcessingException     the json processing exception
     * @throws TalosInternalErrorException the talos internal error exception
     */
    public void sendCompleteNotification(Long groupId)
        throws TalosNotFoundException, JsonProcessingException, TalosInternalErrorException {
        //Retrieves quote group details from Quote Manager
        QuoteOrderMapEntity quoteOrderMap = quoteHelper.fetchOrderMapById(groupId);
        GetQuoteGroupResponse quoteGroupResponse = quoteManagerClient.getQuoteGroupDetails(groupId);

        notificationsProcessor.createCompleteNotificationForAsync(quoteGroupResponse, quoteOrderMap);
        updateStatus(quoteOrderMap);
    }

    /**
     * Update quote group status in quote order map
     * and send request to order manager to update order status.
     *
     * @param quoteOrderMap the quote order map
     */
    private void updateStatus(QuoteOrderMapEntity quoteOrderMap) {
        quoteOrderMap.setStatus(QuoteOrderMapStatus.COMPLETE);
        quoteHelper.saveQuoteOrderMap(quoteOrderMap);
        OrdersDTO ordersDTO = OrderManagerRequestMapper.createStatusUpdateRequest(quoteOrderMap);
        //Push process Order data for Order Manager for cerberus and wag order status update
        dataSyncMessageProducer.sendOrderData(ordersDTO);
    }

    /**
     * Send auto email on delay or completion of quote.
     *
     * @param groupId the group id
     * @throws TalosNotFoundException      thrown when quote is not present in Quote Manager
     * @throws TalosInternalErrorException thrown when there is a messaging exception
     */
    @Transactional(readOnly = true)
    public void sendQuoteEmail(Long groupId) throws TalosNotFoundException, TalosInternalErrorException {
        QuoteOrderMapEntity quoteOrderMap = quoteHelper.fetchOrderMapById(groupId);
        if (quoteOrderMap.getQuoteEmails() != null && !quoteOrderMap.getQuoteEmails().isEmpty()) {
            QBTDCEmailRequest qbtdcEmailRequest = buildEmailRequest(quoteOrderMap);
            quoteEmailProcessor.sendQuoteMail(qbtdcEmailRequest);
        } else {
            LOGGER.info("No mail recipients found for quote group id {}", groupId);
        }
    }

    /**
     * Build email request for quote group id.
     *
     * @param quoteOrderMap {@link QuoteOrderMapEntity}
     * @return the qbtdc email request
     * @throws TalosNotFoundException when no quote order map exists for group id
     */
    private QBTDCEmailRequest buildEmailRequest(QuoteOrderMapEntity quoteOrderMap) {
        List<String> emails = quoteOrderMap.getQuoteEmails()
                .stream()
                .map(QuoteEmailEntity::getEmail)
                .collect(Collectors.toList());

        return new QBTDCEmailRequest(quoteOrderMap.getOrderNumber(),  emails, false, quoteOrderMap.getSupplier());
    }

}
