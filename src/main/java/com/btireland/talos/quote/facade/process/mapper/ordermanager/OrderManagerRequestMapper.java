package com.btireland.talos.quote.facade.process.mapper.ordermanager;

import static com.btireland.talos.quote.facade.base.constant.WagConstants.TALOS_IN_SYSTEM;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.util.DateUtils;
import com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants;
import com.btireland.talos.quote.facade.base.constant.WagConstants;
import com.btireland.talos.quote.facade.base.enumeration.internal.NotificationType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteItemNameType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteMode;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.ordermanager.Qbtdcs;
import com.btireland.talos.quote.facade.dto.ordermanager.Quote;
import com.btireland.talos.quote.facade.dto.ordermanager.Order;
import com.btireland.talos.quote.facade.dto.ordermanager.QuoteOrder;
import com.btireland.talos.quote.facade.dto.quotemanager.response.CreateQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteItemResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteProductConfigResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteResponse;
import com.btireland.talos.quote.facade.exception.BadQuoteResponseException;
import com.btireland.talos.quote.facade.process.helper.QuoteHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Quote request mapper.
 */
public class OrderManagerRequestMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderManagerRequestMapper.class);

    /**
     * Map Quote Manager Response and incoming qbtdc request to Order Manager request.
     *
     * @param createQuoteResponse the create quote response {@link CreateQuoteResponse}
     * @param quoteOrderMap       the qbtdc order map {@link QuoteOrderMapEntity}
     * @return the qbtdc order dto for order manager
     */
    public static QuoteOrder createOrderManagerRequest(CreateQuoteResponse createQuoteResponse,
                                                       QuoteOrderMapEntity quoteOrderMap) throws TalosNotFoundException {


        QuoteOrder quoteOrder = new QuoteOrder(buildOrders(createQuoteResponse, quoteOrderMap),
                buildQbtdcs(createQuoteResponse, quoteOrderMap),
                buildQuoteItemRequest(createQuoteResponse));
        LOGGER.error("quote order.... {}", quoteOrder);
        return quoteOrder;
    }

    /**
     * Build quote item request list for Order Manager DTO.
     *
     * @param createQuoteResponse the create quote response
     * @return the list of quotes
     */
    private static List<Quote> buildQuoteItemRequest(CreateQuoteResponse createQuoteResponse) throws TalosNotFoundException {
        List<Quote> quoteItemDTOList = new ArrayList<>();

        if (createQuoteResponse.getQuotes() != null) {
            for (QuoteResponse quoteResponse : createQuoteResponse.getQuotes()) {
                QuoteItemResponse logical = QuoteHelper.fetchQuoteItemResponse(quoteResponse,
                        QuoteItemNameType.LOGICAL);
                QuoteProductConfigResponse logicalQuoteProductConfig =
                        logical.getQuoteProduct().getQuoteProductConfig();
                QuoteItemResponse aEnd = QuoteHelper.fetchQuoteItemResponse(quoteResponse,
                        QuoteItemNameType.A_END);
                QuoteProductConfigResponse aEndquoteProductConfig = aEnd.getQuoteProduct().getQuoteProductConfig();

                QuoteItemResponse bEnd = QuoteHelper.fetchQuoteItemResponse(quoteResponse,
                        QuoteItemNameType.B_END);
                String product = createQuoteResponse.getServiceClass().name();

                Quote quote = Quote.QuoteBuilder.quoteBuilder()
                        .group(quoteResponse.getGroup())
                        .product(product)
                        .connectionType(createQuoteResponse.getConnectionType() != null ?
                                createQuoteResponse.getConnectionType().name() : null)
                        .term(logical.getQuoteItemTerm())
                        .ipRange(logicalQuoteProductConfig.getIpRange() != null ?
                                logicalQuoteProductConfig.getIpRange().intValue() : null)
                        .logicalActionFlag(logicalQuoteProductConfig.getAction().getValue())
                        .logicalBandwidth(QuoteHelper.formatBandwidth(logicalQuoteProductConfig.getBandwidth()))
                        .logicalProfile(logicalQuoteProductConfig.getProfile().name())
                        .aendEircode(aEnd.getQuoteProduct().getQuoteProductPlace().getEircode())
                        .aendActionFlag(aEndquoteProductConfig.getAction().getValue())
                        .aendReqAccessSupplier(aEndquoteProductConfig.getAccessSupplier() != null ?
                                aEndquoteProductConfig.getAccessSupplier().getValue() : null)
                        .aendBandwidth(QuoteHelper.formatBandwidth(aEndquoteProductConfig.getBandwidth()))
                        .aendSla(aEndquoteProductConfig.getSla() != null ? aEndquoteProductConfig.getSla().name() :
                                null)
                        .aendCeSwitch(aEndquoteProductConfig.getCeSwitch() != null ?
                                aEndquoteProductConfig.getCeSwitch() : null)
                        .bendHandOverNode(bEnd.getQuoteProduct().getQuoteProductPlace().getHandoverNode())
                        .bendActionFlag(bEnd.getQuoteProduct().getQuoteProductConfig().getAction().getValue())
                        .build();
                quoteItemDTOList.add(quote);
            }
        } else {
            throw new BadQuoteResponseException("No quotes in Quote Manager Response for quote group id" + createQuoteResponse.getQuoteGroupId());
        }
        return quoteItemDTOList;
    }

    /**
     * Build qbtdcs for OrderManager request.
     *
     * @param createQuoteResponse the response from quote manager {@link CreateQuoteResponse}
     * @param quoteOrderMapEntity the quote order map {@link QuoteOrderMapEntity}
     * @return the qbtdcs dto
     */
    private static Qbtdcs buildQbtdcs(CreateQuoteResponse createQuoteResponse, QuoteOrderMapEntity quoteOrderMapEntity) {
        String mode = Boolean.TRUE.equals(quoteOrderMapEntity.getSyncFlag()) ? QuoteMode.SYNC.getValue() :
                QuoteMode.ASYNC.getValue();
        return new Qbtdcs(mode, DateUtils.btDateTimeToStringWithPattern(createQuoteResponse.getQuoteDate(),
                WagConstants.DD_MM_YYYY),
                QuoteHelper.fetchRecurringFrequency(createQuoteResponse.getQuotes().get(0)));

    }

    /**
     * Build orders dto for Order Manager request
     *
     * @param createQuoteResponse the response from quote manager {@link CreateQuoteResponse}
     * @param quoteOrderMap       the quote order map {@link QuoteOrderMapEntity}
     * @return the orders dto
     */
    private static Order buildOrders(CreateQuoteResponse createQuoteResponse, QuoteOrderMapEntity quoteOrderMap) throws TalosNotFoundException {
        String oao = QuoteHelper.fetchOao(createQuoteResponse.getQuotes().get(0).getRelatedParties());
        String obo = QuoteHelper.fetchObo(createQuoteResponse.getQuotes().get(0).getRelatedParties());
        return Order.OrderBuilder.orderBuilder()
                .oao(obo != null ? obo : oao)
                .obo(obo)
                .projectKey(createQuoteResponse.getProjectIdentifier())
                .operatorName(quoteOrderMap.getOperatorName())
                .operatorCode(quoteOrderMap.getOperatorCode())
                .dataContractName(QuoteManagerConstants.QBTDC_DATA_CONTRACT_NAME)
                .originatorCode(QuoteManagerConstants.QBTDC_ORIGINATOR_CODE)
                .transactionId(createQuoteResponse.getResellerTransactionId())
                .orderRequestDateTime(DateUtils.btDateTimeToString(createQuoteResponse.getQuoteDate()))
                .orderNumber(quoteOrderMap.getOrderNumber())
                .orderServiceType(QuoteManagerConstants.QBTDC_TYPE)
                .orderStatus(fetchOrderStatus(createQuoteResponse.getNotificationType()))
                .workflowStatus(TALOS_IN_SYSTEM)
                .build();
    }

    /**
     * Fetch wag order status string.
     *
     * @param notificationType the notification type from Quote Manager
     * @return the string order status
     */
    private static String fetchOrderStatus(NotificationType notificationType) {
        if (notificationType == NotificationType.COMPLETE) {
            return WagConstants.TALOS_COMPLETE;
        } else if (notificationType == NotificationType.REJECT) {
            return WagConstants.TALOS_REJECT;
        }
        return null;
    }

    public static OrdersDTO createStatusUpdateRequest(QuoteOrderMapEntity quoteOrderMap) {
        OrdersDTO ordersDTO = new com.btireland.talos.ethernet.engine.dto.OrdersDTO();
        ordersDTO.setOrderNumber(quoteOrderMap.getOrderNumber());
        ordersDTO.setOao(quoteOrderMap.getSupplier());
        ordersDTO.setLastNotificationType(WagConstants.COMPLETE);
        return ordersDTO;
    }

}
