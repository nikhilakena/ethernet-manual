package com.btireland.talos.quote.facade.process.helper;

import static com.btireland.talos.quote.facade.base.enumeration.internal.ErrorCode.ACTION_NOT_FOUND;
import static com.btireland.talos.quote.facade.base.enumeration.internal.ErrorCode.BUYER_NOT_FOUND;
import static com.btireland.talos.quote.facade.base.enumeration.internal.ErrorCode.QUOTE_ITEM_MAP_NOT_FOUND;
import static com.btireland.talos.quote.facade.base.enumeration.internal.ErrorCode.QUOTE_ITEM_NOT_FOUND;
import static com.btireland.talos.quote.facade.base.enumeration.internal.ErrorCode.QUOTE_MAP_NOT_FOUND;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants;
import com.btireland.talos.quote.facade.base.enumeration.internal.ActionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.BusinessConsoleStatusType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ErrorCode;
import com.btireland.talos.quote.facade.base.enumeration.internal.NetworkType;
import com.btireland.talos.quote.facade.base.enumeration.internal.PriceType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteItemNameType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.btireland.talos.quote.facade.base.enumeration.internal.Role;
import com.btireland.talos.quote.facade.domain.dao.QuoteItemMapRepository;
import com.btireland.talos.quote.facade.domain.dao.QuoteOrderMapRepository;
import com.btireland.talos.quote.facade.domain.entity.QuoteEmailEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteItemResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuotePriceResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.RelatedPartyResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuotePriceResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetRelatedPartyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.util.CollectionUtils;


@Service
public class QuoteHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteHelper.class);

    private final QuoteOrderMapRepository quoteOrderMapRepository;

    private final QuoteItemMapRepository quoteItemMapRepository;

    public QuoteHelper(QuoteOrderMapRepository quoteOrderMapRepository, QuoteItemMapRepository quoteItemMapRepository) {
        this.quoteOrderMapRepository = quoteOrderMapRepository;
        this.quoteItemMapRepository = quoteItemMapRepository;
    }

    /**
     * Method to update QuoteOrderMap
     * @param quoteOrderMap QuoteOrderMap to be saved
     * @return QuoteOrderMapEntity Saved QuoteOrderMapEntity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public QuoteOrderMapEntity saveQuoteOrderMap(QuoteOrderMapEntity quoteOrderMap) {
        return quoteOrderMapRepository.save(quoteOrderMap);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public QuoteOrderMapEntity saveQBTDCQuoteOrderMap(QuoteOrderMapEntity quoteOrderMap) {

        if (!CollectionUtils.isEmpty(quoteOrderMap.getQuoteEmails())) {
            for (QuoteEmailEntity email : quoteOrderMap.getQuoteEmails()) {
                email.setQuoteOrderMap(quoteOrderMap);
            }
        }

        return quoteOrderMapRepository.save(quoteOrderMap);
    }

    /**
     * Fetch network type enum.
     *
     * @param accessSupplier the access supplier
     * @return the network type enum
     */
    public static NetworkType fetchNetworkType(String accessSupplier) throws TalosNotFoundException {
        return Arrays.stream(NetworkType.values())
                .filter(networkType -> networkType.getValue()
                        .equals(accessSupplier))
                .findFirst()
                .orElseThrow(() -> new TalosNotFoundException(LOGGER, ErrorCode.NETWORK_TYPE_NOT_FOUND.name(),String.format(
                                                                      "NetworkType not found for " + "accessSupplier %s",
                                                                        accessSupplier)));
    }

    public static ConnectionType fetchConnectionType(String connectionType) throws TalosNotFoundException {
        return Arrays.stream(ConnectionType.values())
                .filter(connection -> connection.getValue()
                        .equals(connectionType))
                .findFirst()
                .orElseThrow(() -> new TalosNotFoundException(LOGGER, ErrorCode.CONNECTION_TYPE_NOT_FOUND.name(),String.format(
                        "Connection type not found for " + "connectionType %s",
                        connectionType)));
    }

    /**
     * Fetch Action Type enum.
     *
     * @param actionFlag the access supplier
     * @return the action type enum
     */
    public static ActionType fetchActionType(String actionFlag) throws TalosNotFoundException {
        return Arrays.stream(ActionType.values())
                .filter(actionType -> actionType.getValue()
                        .equals(actionFlag))
                .findFirst()
                .orElseThrow(() -> new TalosNotFoundException(LOGGER, ACTION_NOT_FOUND.name(), String.format(
                                                                                 "ActionType not found for actionFlag" +
                                                                                         " %s", actionFlag)));
    }

    /**
     * Fetch oao/buyer.
     *
     * @param relatedParties the related parties
     * @return the oao string
     */
    public static String fetchOao(List<RelatedPartyResponse> relatedParties) throws TalosNotFoundException {
        return relatedParties.stream()
                .filter(relatedParty -> relatedParty.getRole() == Role.BUYER)
                .findFirst()
                .map(RelatedPartyResponse::getName)
                .orElseThrow(() -> new TalosNotFoundException(LOGGER,BUYER_NOT_FOUND.name(),
                                                              String.format("BUYER is not found in quote manager " +
                                                                                       "response")));
    }

    /**
     * Fetch obo/requestor.
     *
     * @param relatedParties the related parties {@link List<RelatedPartyResponse>}
     * @return the obo string
     */
    public static String fetchObo(List<RelatedPartyResponse> relatedParties) {
        return relatedParties.stream()
                .filter(relatedParty -> relatedParty.getRole() == Role.REQUESTOR)
                .findFirst()
                .map(RelatedPartyResponse::getName)
                .orElse(null);
    }

    /**
     * Fetch oao/buyer.
     *
     * @param relatedParties the related parties
     * @param role the type of role
     * @return the oao string
     */
    public static String fetchRelatedPartyByType(List<GetRelatedPartyResponse> relatedParties, Role role) throws TalosNotFoundException {
        return relatedParties.stream()
            .filter(relatedParty -> relatedParty.getRole() == role)
            .findFirst()
            .map(GetRelatedPartyResponse::getName)
            .orElse(null);
    }

    /**
     * Fetch quote item response.
     *
     * @param quoteResponse     the quote response
     * @param quoteItemNameType the quote item name type
     * @return the quote item response
     */
    public static QuoteItemResponse fetchQuoteItemResponse(QuoteResponse quoteResponse,
                                                           QuoteItemNameType quoteItemNameType) throws TalosNotFoundException {
        return quoteResponse.getQuoteItems()
                .stream()
                .filter(quoteItem -> quoteItem.getName() == quoteItemNameType)
                .findFirst()
                .orElseThrow(() -> new TalosNotFoundException(LOGGER, QUOTE_ITEM_NOT_FOUND.name(),
                                                              String.format("QuoteItemResponse %s not found " +
                                                                                           "for quote entity id %d",
                                                                               quoteItemNameType.name(),
                                                                               quoteResponse.getQuoteId())));
    }

    /**
     * Method to fetch recurring frequency of the Quote
     * @param quoteResponse QuoteResponse Object
     * @return String RecurringFrequency of the quote
     */
    public static String fetchRecurringFrequency(QuoteResponse quoteResponse) {
        return quoteResponse.getQuotePrice().stream()
            .filter(quotePrice -> quotePrice.getPriceType() == PriceType.RECURRING).findFirst().get().getRecurringChargePeriod().name();
    }

    /**
     * Method to fetch recurring frequency of the Quote
     * @param quoteResponse GetQuoteResponse Object {@link GetQuoteResponse}
     * @return String RecurringFrequency of the quote {@link String}
     */
    public static String fetchRecurringFrequencyForAsync(GetQuoteResponse quoteResponse) {
        return quoteResponse.getQuotePriceResponses().stream()
            .filter(quotePrice -> quotePrice.getPriceType() == PriceType.RECURRING).findFirst().get()
            .getRecurringChargePeriod().name();
    }

    /**
     * Fetch recurring/non-recurring price string.
     *
     * @param quotePriceResponseList the quote price response list {@link List< QuotePriceResponse >}
     * @param priceType              the price type recurring/non-recurring {@link PriceType}
     * @return the recurring/non-recurring price
     */
    public static String fetchPrice(List<QuotePriceResponse> quotePriceResponseList, PriceType priceType){
        return quotePriceResponseList.stream()
                .filter(quotePriceResponse -> priceType == quotePriceResponse.getPriceType())
                .findFirst()
                .map(QuotePriceResponse::getQuotePrice)
                .orElse(null);
    }

    /**
     * Fetch recurring/non-recurring price string From GetQuotePriceResponse List.
     *
     * @param quotePriceResponseList the quote price response list {@link List< GetQuotePriceResponse >}
     * @param priceType              the price type recurring/non-recurring {@link PriceType}
     * @return the recurring/non-recurring price
     */
    public static String fetchPriceForAsync(List<GetQuotePriceResponse> quotePriceResponseList, PriceType priceType){
        return quotePriceResponseList.stream()
            .filter(quotePriceResponse -> priceType == quotePriceResponse.getPriceType())
            .findFirst()
            .map(GetQuotePriceResponse::getQuotePrice)
            .orElse(null);
    }

    /**
     * Fetch wag quote item id long.
     *
     * @param quoteOrderMapEntity the quote order map entity {@link QuoteOrderMapEntity}
     * @param quoteId             the quote manager quote id
     * @return the wag quote item id long
     */
    public static Long fetchWagQuoteItemId(QuoteOrderMapEntity quoteOrderMapEntity,Long quoteId) throws TalosNotFoundException {
        return quoteOrderMapEntity.getQuoteItemMapList().stream()
                .filter(quoteItemMapEntity -> quoteItemMapEntity.getQuoteId().equals(quoteId))
                .findFirst()
                .map(QuoteItemMapEntity::getWagQuoteId)
                .orElseThrow(() -> new TalosNotFoundException(LOGGER,QUOTE_ITEM_MAP_NOT_FOUND.name(),
                                                              String.format("No Wag Quote Id mapping found " +
                                                                                            "for " +
                                                                                       "quote id %d" , quoteId)));

    }


    /**
     * Fetch quote order map quote order map entity for a quotegroupid.
     *
     * @param quoteOrderMapEntityList the quote order map entity list
     * @param quoteGroupId            the quote group id
     * @return the quote order map entity
     */
    public static QuoteOrderMapEntity fetchQuoteOrderMap(List<QuoteOrderMapEntity> quoteOrderMapEntityList,
                                                         Long quoteGroupId) throws TalosNotFoundException {
        return quoteOrderMapEntityList
                .stream()
                .filter(quoteOrderMap -> quoteOrderMap
                        .getQuoteGroupId()
                        .equals(quoteGroupId))
                .findFirst()
                .orElseThrow(() -> new TalosNotFoundException(LOGGER,QUOTE_MAP_NOT_FOUND.name(),
                                                              String.format("No mapping found for Quote Group id %d",
                                                                               quoteGroupId)));

    }


    /**
     * Retrieve quote order map by order number from database.
     *
     * @param orderNumber the order number
     * @return the list
     */
    public List<QuoteOrderMapEntity> fetchQuoteOrderMapByOrderNumber(String orderNumber) {
        return quoteOrderMapRepository.findByOrderNumber(orderNumber);
    }

    /**
     * Retrieve quote order map by order number from database.
     *
     * @param quoteGroupId the quoteGroupId
     * @return the QuoteOrderMapEntity
     */
    public Optional<QuoteOrderMapEntity> fetchQuoteOrderMapByQuoteGroupIdAndStatusNotAsDelayed(Long quoteGroupId) {
        return quoteOrderMapRepository
            .findByQuoteGroupIdAndStatusNotIn(quoteGroupId, Collections.singletonList(QuoteOrderMapStatus.DELAY));
    }

    /**
     * Fetch all quote order map with matching group ids for a specified page.
     *
     * @param quoteGroupIdList the quote group id list
     * @param pageable         the {@link Pageable}
     * @return the {@link Page<QuoteOrderMapEntity>}
     */
    public Page<QuoteOrderMapEntity> fetchQuoteOrderMapByGroupIds(List<Long> quoteGroupIdList, Pageable pageable) {
        return quoteOrderMapRepository.findByQuoteGroupIdIn(quoteGroupIdList, pageable);
    }

    /**
     * Fetch quote item map by quote id .
     *
     * @param quoteId the quote id
     * @return the quote item map entity {@link QuoteItemMapEntity}
     */
    public QuoteItemMapEntity fetchQuoteItemMapByQuoteId(Long quoteId) throws TalosNotFoundException {
        return quoteItemMapRepository
                .findByQuoteId(quoteId)
                .orElseThrow(() -> new TalosNotFoundException(LOGGER, QUOTE_ITEM_MAP_NOT_FOUND.name(),
                                                              String.format("No Quote Item found in quote map" +
                                                                                    " for quote id %d", quoteId)));
    }
    /**
     * Method to get Business console status from Order Map Status
     * @param quoteOrderMap
     * @return String Corresponding status for Business Console
     */
    public static String getBusinessConsoleStatusfromOrderMapStatus(QuoteOrderMapEntity quoteOrderMap) {
        return BusinessConsoleStatusType.valueOf(quoteOrderMap.getStatus().name()).getValue();
    }


    /**
     * Update  quote order map status.
     *
     * @param orderNumber the wag order number
     * @param oao supplier
     * @param status       the status {@link QuoteOrderMapStatus}
     * @return the quote order map entity {@link QuoteOrderMapEntity}
     * @throws TalosNotFoundException the talos not found exception
     */
    public QuoteOrderMapEntity updateOrderStatus(String orderNumber, String oao, QuoteOrderMapStatus status) throws TalosNotFoundException {
        QuoteOrderMapEntity quoteOrderMap = quoteOrderMapRepository.findByOrderNumberAndSupplier(orderNumber, oao)
                .orElseThrow(() -> new TalosNotFoundException(LOGGER, QUOTE_MAP_NOT_FOUND.name(),
                                                              String.format("No mapping found for Order Number %s and" +
                                                                                    " " +
                                                                                    "supplier %s", orderNumber, oao)));

        quoteOrderMap.setStatus(status);
        return quoteOrderMapRepository.save(quoteOrderMap);
    }

    /**
     * Fetch order map by quote group id.
     *
     * @param quoteGroupId the quote group id
     * @return the quote order map entity {@link QuoteOrderMapEntity}
     * @throws TalosNotFoundException the talos not found exception
     */
    public QuoteOrderMapEntity fetchOrderMapById(Long quoteGroupId) throws TalosNotFoundException {
        return quoteOrderMapRepository.findById(quoteGroupId)
                                .orElseThrow(() -> new TalosNotFoundException(LOGGER,QUOTE_MAP_NOT_FOUND.name(),
                                                     String.format("No mapping found for Quote Group id %d",
                                                                   quoteGroupId)));

    }

    /**
     * Converts bandwidth into string by removing decimal if integer else in specified format.
     *
     * @param bandwidth the bandwidth
     * @return the bandwidth string
     */
    public static String formatBandwidth(Float bandwidth) {
        String formattedBandwidth = null;
        if (bandwidth != null) {
            DecimalFormat decimalFormat = new DecimalFormat(QuoteManagerConstants.DECIMAL_PATTERN);
            formattedBandwidth = decimalFormat.format(bandwidth);
        }
        return formattedBandwidth;
    }
}
