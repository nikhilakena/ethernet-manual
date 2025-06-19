package com.btireland.talos.quote.facade.process.processor;

import com.btireland.talos.core.common.rest.exception.TalosException;
import com.btireland.talos.core.common.rest.exception.checked.TalosInternalErrorException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.client.asset.notcom.NotificationClient;
import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.quote.facade.base.constant.NotcomConstants;
import com.btireland.talos.quote.facade.base.enumeration.internal.ErrorCode;
import com.btireland.talos.quote.facade.base.enumeration.internal.NotificationType;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.notcom.NotificationRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.response.CreateQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteGroupResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.QuoteGroupOfflinePricingResponse;
import com.btireland.talos.quote.facade.process.mapper.notcom.NotcomRequestMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotComNotificationsProcessor {

    private final NotificationClient notificationClient;

    private final ObjectMapper objectMapper;

    private final Logger LOGGER = LoggerFactory.getLogger(NotComNotificationsProcessor.class);

    public NotComNotificationsProcessor(NotificationClient notificationClient, ObjectMapper objectMapper) {
        this.notificationClient = notificationClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Send notification Completion/Rejection/Delayed to notcom.
     *
     * @param createQuoteResponse the create quote response from Quote Manager
     * @param wagOrderId          the wag order id
     * @param quoteOrderMapEntity the quote order map entity
     * @return the notifications sent from notcom
     * @throws TalosInternalErrorException the talos internal error exception
     * @throws JsonProcessingException     the json processing exception
     */
    public Notifications createNotification(CreateQuoteResponse createQuoteResponse, Long wagOrderId,
        QuoteOrderMapEntity quoteOrderMapEntity) throws JsonProcessingException,
        TalosInternalErrorException {
        Notifications notComRequest = null;
        try {
            notComRequest = generateNotComRequestForRSPNotification(createQuoteResponse, wagOrderId,
                quoteOrderMapEntity);
            return notificationClient.createNotification(notComRequest);
        } catch (TalosException | TalosNotFoundException ex) {
            throw new TalosInternalErrorException(LOGGER, ErrorCode.INTERNAL_ERROR.name(),
                "Exception received while sending RSP Notification of type: " + createQuoteResponse
                    .getNotificationType() + ", for order reference " + quoteOrderMapEntity.getOrderNumber(), ex);
        }
    }

    /**
     * Generate not com request for rsp notification notifications.
     *
     * @param createQuoteResponse the create quote response from Quote Manager
     * @param wagOrderId          the wag order id
     * @param quoteOrderMapEntity the quote order map entity
     * @return the notifications
     * @throws JsonProcessingException the json processing exception
     */
    private Notifications generateNotComRequestForRSPNotification(CreateQuoteResponse createQuoteResponse,
        Long wagOrderId,
        QuoteOrderMapEntity quoteOrderMapEntity) throws JsonProcessingException, TalosNotFoundException {
        NotificationRequest notcomOrder = NotcomRequestMapper.createNotcomOrders(createQuoteResponse, wagOrderId,
                                                                                 quoteOrderMapEntity);
        return Notifications.builder().source(NotcomConstants.TALOS)
            .type(createQuoteResponse.getNotificationType().getValue())
            .reference(generateTrackingRef(wagOrderId, notcomOrder.getServiceType()))
            .content(objectMapper.writeValueAsBytes(notcomOrder)).build();
    }


    /**
     * Generate tracking ref string for notcom request.
     *
     * @param orderId     the order id
     * @param serviceType the service type
     * @return the string
     */
    private String generateTrackingRef(Long orderId, String serviceType) {
        return NotcomConstants.BT + serviceType + NotcomConstants.HYPHEN + orderId.toString();
    }

    /**
     * Create delay notification and invoke notcom API.
     *
     * @param quoteResponse the quote response {@link QuoteGroupOfflinePricingResponse}
     * @param quoteOrderMap the quote order map {@link QuoteOrderMapEntity}
     * @return the notifications
     * @throws TalosInternalErrorException the talos internal error exception
     * @throws JsonProcessingException     the json processing exception
     */
    public Notifications createDelayNotification(QuoteGroupOfflinePricingResponse quoteResponse,
                                                 QuoteOrderMapEntity quoteOrderMap)
        throws JsonProcessingException, TalosInternalErrorException {
        Notifications notComRequest = generateNotComRequestForDelayNotification(quoteResponse, quoteOrderMap);
        try {
            return notificationClient.createNotification(notComRequest);
        } catch (TalosException ex) {
            throw new TalosInternalErrorException(LOGGER, ErrorCode.INTERNAL_ERROR.name(),
                "Exception received while sending RSP Notification of type: " + notComRequest
                    .getType() + ", for order reference " + notComRequest.getReference(), ex);
        }
    }

    /**
     * Generate not com request for delay notification .
     *
     * @param quoteResponse       the quote response {@link QuoteGroupOfflinePricingResponse}
     * @param quoteOrderMapEntity the quote order map entity {@link QuoteOrderMapEntity}
     * @return the notifications
     * @throws JsonProcessingException the json processing exception
     */
    private Notifications generateNotComRequestForDelayNotification(QuoteGroupOfflinePricingResponse quoteResponse,
        QuoteOrderMapEntity quoteOrderMapEntity) throws JsonProcessingException {
        NotificationRequest notcomOrder = NotcomRequestMapper.createNotcomOrderForDelay(quoteResponse,
            quoteOrderMapEntity);
        return Notifications.builder().source(NotcomConstants.TALOS).type(NotificationType.DELAY.getValue())
            .reference(generateTrackingRef(quoteOrderMapEntity.getQuoteGroupId(), notcomOrder.getServiceType()))
            .content(objectMapper.writeValueAsBytes(notcomOrder)).build();
    }

    /**
     * Create complete notification for Async and invoke notcom API.
     *
     * @param quoteGroupResponse the quote group response {@link GetQuoteGroupResponse}
     * @param quoteOrderMap the quote order map {@link QuoteOrderMapEntity}
     * @return the notifications
     * @throws TalosInternalErrorException the talos internal error exception
     * @throws JsonProcessingException     the json processing exception
     */
    public void createCompleteNotificationForAsync(GetQuoteGroupResponse quoteGroupResponse,
        QuoteOrderMapEntity quoteOrderMap)
        throws JsonProcessingException, TalosNotFoundException, TalosInternalErrorException {
        Notifications notComRequest = generateNotComRequestForCompleteNotificationForAsync(quoteGroupResponse,
            quoteOrderMap);
        try {
            notificationClient.createNotification(notComRequest);
        } catch (TalosException ex) {
            throw new TalosInternalErrorException(LOGGER, ErrorCode.INTERNAL_ERROR.name(),
                "Exception received while sending RSP Notification of type: " + notComRequest
                    .getType() + ", for order reference " + notComRequest.getReference(), ex);
        }
    }

    /**
     * Generate not com request for Async complete notification .
     *
     * @param quoteGroupResponse       the quote response {@link GetQuoteGroupResponse}
     * @param quoteOrderMapEntity the quote order map entity {@link QuoteOrderMapEntity}
     * @return the notifications
     * @throws JsonProcessingException the json processing exception
     */
    private Notifications generateNotComRequestForCompleteNotificationForAsync(GetQuoteGroupResponse quoteGroupResponse,
        QuoteOrderMapEntity quoteOrderMapEntity) throws JsonProcessingException, TalosNotFoundException {
        NotificationRequest notcomOrder = NotcomRequestMapper.createNotcomOrderForAsyncCompletion(quoteGroupResponse,
            quoteOrderMapEntity);
        return Notifications.builder().source(NotcomConstants.TALOS).type(NotificationType.COMPLETE.getValue())
            .reference(generateTrackingRef(
                quoteOrderMapEntity.getQuoteGroupId(), notcomOrder.getServiceType()))
            .content(objectMapper.writeValueAsBytes(notcomOrder)).build();
    }

}
