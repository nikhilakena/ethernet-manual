package com.btireland.talos.quote.facade.process.mapper.notcom;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import com.btireland.talos.ethernet.engine.util.DateUtils;
import com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.NotificationType;
import com.btireland.talos.quote.facade.base.enumeration.internal.PriceType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteItemNameType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteMode;
import com.btireland.talos.quote.facade.base.enumeration.internal.Role;
import com.btireland.talos.quote.facade.base.enumeration.internal.ServiceClassType;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.notcom.Access;
import com.btireland.talos.quote.facade.dto.notcom.Address;
import com.btireland.talos.quote.facade.dto.notcom.Location;
import com.btireland.talos.quote.facade.dto.notcom.LogicalLink;
import com.btireland.talos.quote.facade.dto.notcom.NotificationRequest;
import com.btireland.talos.quote.facade.dto.notcom.QuoteItem;
import com.btireland.talos.quote.facade.dto.notcom.QuoteOrder;
import com.btireland.talos.quote.facade.dto.notcom.RejectionDetails;
import com.btireland.talos.quote.facade.dto.notcom.Site;
import com.btireland.talos.quote.facade.dto.quotemanager.response.CreateQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteProductConfigResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteProductPlaceResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteProductResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.RejectionDetailsResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteAEndResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteBEndResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteGroupResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteLogicalResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetRejectionDetailsResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.QuoteGroupOfflinePricingResponse;
import com.btireland.talos.quote.facade.process.helper.QuoteHelper;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotcomRequestMapper {

    private static final Logger logger = LoggerFactory.getLogger(NotcomRequestMapper.class);


    /**
     * Create notcom orders orders dto sent in the request.
     *
     * @param createQuoteResponse the response from Quote Manager {@link CreateQuoteResponse}
     * @param wagOrderId          the wag order id {@link Long}
     * @param quoteOrderMapEntity the quote order map  {@link QuoteOrderMapEntity}
     * @return the NotificationRequest dto
     */
    public static NotificationRequest createNotcomOrders(CreateQuoteResponse createQuoteResponse, Long wagOrderId,
                                                         QuoteOrderMapEntity quoteOrderMapEntity) throws TalosNotFoundException {
        String oao = QuoteHelper.fetchOao(createQuoteResponse.getQuotes().get(0).getRelatedParties());
        String obo = QuoteHelper.fetchObo(createQuoteResponse.getQuotes().get(0).getRelatedParties());


        return NotificationRequest.NotificationRequestBuilder.notificationRequestBuilder()
                .orderId(wagOrderId)
                .talosOrderId(createQuoteResponse.getQuoteGroupId())
                .oao(obo != null ? obo : oao)
                .obo(obo)
                .dataContract(QuoteManagerConstants.QBTDC_DATA_CONTRACT_NAME)
                .originatorCode(QuoteManagerConstants.QBTDC_ORIGINATOR_CODE)
                .resellerTransactionId(createQuoteResponse.getResellerTransactionId())
                .resellerOrderRequestDateTime(DateUtils.btDateTimeToString(createQuoteResponse.getQuoteDate()))
                .operatorName(quoteOrderMapEntity.getOperatorName())
                .operatorCode(quoteOrderMapEntity.getOperatorCode())
                .orderNumber(quoteOrderMapEntity.getOrderNumber())
                .serviceType(QuoteManagerConstants.QBTDC_TYPE)
                .createdAt(createQuoteResponse.getQuoteDate().toString())
                .mode(quoteOrderMapEntity.getSyncFlag() ? QuoteMode.SYNC.getValue() : QuoteMode.ASYNC.getValue())
                .projectKey(createQuoteResponse.getProjectIdentifier())
                .rejectionDetails(populateOrderRejectionDetails(createQuoteResponse.getRejectionDetails(),
                                                                createQuoteResponse.getNotificationType()))
                .qbtdc(createQuoteOrder(createQuoteResponse, quoteOrderMapEntity, wagOrderId))
                .build();

    }


    /**
     * Create qbtdc dto qbtdc dto.
     *
     * @param createQuoteResponse the response from Quote Manager {@link CreateQuoteResponse}
     * @param quoteOrderMapEntity the quote order map entity {@link QuoteOrderMapEntity}
     * @param orderId             the wag order id {@link Long}
     * @return the qbtdc dto
     */
    private static QuoteOrder createQuoteOrder(CreateQuoteResponse createQuoteResponse,
                                             QuoteOrderMapEntity quoteOrderMapEntity, Long orderId) throws TalosNotFoundException {
        List<QuoteItem> quoteItemList = new ArrayList<>();
        String recurringfrequency = null;

        if (createQuoteResponse.getQuotes() != null) {
            recurringfrequency = QuoteHelper.fetchRecurringFrequency(createQuoteResponse.getQuotes().get(0));
            for (QuoteResponse quoteResponse : createQuoteResponse.getQuotes()) {
                QuoteItem quoteItem = mapQuote(quoteResponse, quoteOrderMapEntity, orderId, createQuoteResponse);
                quoteItemList.add(quoteItem);
            }
        }
        return new QuoteOrder(recurringfrequency, quoteItemList);
    }

    /**
     * Create quote item dto for notcom request.
     *
     * @param quoteResponse       the quote response from Quote Manager {@link QuoteResponse}
     * @param quoteOrderMapEntity the quote order map entity {@link QuoteOrderMapEntity}
     * @param orderId             the order id {@link Long}
     * @param createQuoteResponse
     * @return the quote item dto
     */
    private static QuoteItem mapQuote(QuoteResponse quoteResponse,
                                         QuoteOrderMapEntity quoteOrderMapEntity, Long orderId,
                                         CreateQuoteResponse createQuoteResponse) throws TalosNotFoundException {
        return QuoteItem.QuoteItemBuilder.quoteItemBuilder()
                .group(quoteResponse.getGroup())
                .logicalLink(createLogicalLink(quoteResponse))
                .customerAccess(createCustomerAccess(quoteResponse))
                .wholesalerAccess(createWholesalerAccess(quoteResponse))
                .rejectionDetails(populateQuoteRejectionDetails(quoteResponse.getRejectionDetails()))
                //To be updated
                .offlineQuoted(null)
                .quoteItemId(QuoteHelper.fetchWagQuoteItemId(quoteOrderMapEntity, quoteResponse.getQuoteId()))
                .orderId(orderId)
                .term(quoteResponse.getQuoteItems().get(0).getQuoteItemTerm())
                .product(createQuoteResponse.getServiceClass().name())
                .connectionType(createQuoteResponse.getConnectionType() != null ? createQuoteResponse.getConnectionType().getValue() : null)
                .nonRecurringPrice(QuoteHelper.fetchPrice(quoteResponse.getQuotePrice(), PriceType.NON_RECURRING))
                .recurringPrice(QuoteHelper.fetchPrice(quoteResponse.getQuotePrice(), PriceType.RECURRING))
                .etherflowRecurringPrice(QuoteHelper.fetchPrice(quoteResponse.getQuotePrice(),
                                                                PriceType.ETHERFLOW_RECURRING))
                .etherwayRecurringPrice(QuoteHelper.fetchPrice(quoteResponse.getQuotePrice(),
                                                               PriceType.ETHERWAY_RECURRING))
                .status(quoteResponse.getQuoteState().getValue())
                //To be updated
                .notes(null)
                .build();

    }


    /**
     * Create logical link dto for notcom request.
     *
     * @param quoteResponse the quote response from quote manager {@link QuoteResponse}
     * @return the logical link dto
     */
    private static LogicalLink createLogicalLink(QuoteResponse quoteResponse) throws TalosNotFoundException {
        QuoteProductConfigResponse logicalQuoteProductConfig = QuoteHelper.fetchQuoteItemResponse(quoteResponse,
                                                                                                  QuoteItemNameType.LOGICAL).getQuoteProduct().getQuoteProductConfig();
        String ipRange = logicalQuoteProductConfig.getIpRange() != null ?
                String.valueOf(logicalQuoteProductConfig.getIpRange()) : null;
        return new LogicalLink(Enum.valueOf(ActionFlag.class, logicalQuoteProductConfig.getAction().getValue()),
                               QuoteHelper.formatBandwidth(logicalQuoteProductConfig.getBandwidth()),
                               logicalQuoteProductConfig.getProfile().name(),ipRange);

    }

    /**
     * Create customer access access dto for notcom request.
     *
     * @param quoteResponse the quote response from Quote Manager
     * @return the access dto
     */
    private static Access createCustomerAccess(QuoteResponse quoteResponse) throws TalosNotFoundException {
        QuoteProductResponse aEndQuoteProduct = QuoteHelper.fetchQuoteItemResponse(quoteResponse,
                                                                                   QuoteItemNameType.A_END).getQuoteProduct();
        QuoteProductConfigResponse aEndQuoteProductConfig = aEndQuoteProduct.getQuoteProductConfig();
        QuoteProductPlaceResponse aEndQuoteProductPlace = aEndQuoteProduct.getQuoteProductPlace();

        String sla = aEndQuoteProductConfig.getSla() != null ? aEndQuoteProductConfig.getSla().name() : null;
        String targetAccessSupplier = aEndQuoteProductConfig.getTargetAccessSupplier() != null ? aEndQuoteProductConfig
                .getTargetAccessSupplier().getValue() : null;
        String supplier = aEndQuoteProductConfig.getAccessSupplier() != null ? aEndQuoteProductConfig.getAccessSupplier()
                        .getValue() : null;
        return new Access(Enum.valueOf(ActionFlag.class, aEndQuoteProductConfig.getAction().getValue()),
                          QuoteHelper.formatBandwidth(aEndQuoteProductConfig.getBandwidth()),sla,
                          createAEndSite(aEndQuoteProductPlace, aEndQuoteProductConfig),targetAccessSupplier,supplier);

    }

    /**
     * Create wholesaler access access dto for notcom request.
     *
     * @param quoteResponse the quote response from Quote Manager
     * @return the access dto
     */
    private static Access createWholesalerAccess(QuoteResponse quoteResponse) throws TalosNotFoundException {
        QuoteProductResponse bEndQuoteProduct = QuoteHelper.fetchQuoteItemResponse(quoteResponse,
                                                                                   QuoteItemNameType.B_END).getQuoteProduct();
        QuoteProductConfigResponse bEndQuoteProductConfig = bEndQuoteProduct.getQuoteProductConfig();
        QuoteProductPlaceResponse bEndQuoteProductPlace = bEndQuoteProduct.getQuoteProductPlace();

        return new Access(Enum.valueOf(ActionFlag.class, bEndQuoteProductConfig.getAction().getValue())
                ,createBEndSite(bEndQuoteProductPlace));

    }

    /**
     * Create a end site site dto for notcom request.
     *
     * @param aEndQuoteProductPlace  the a end quote product place
     * @param aEndQuoteProductConfig the a end quote product config
     * @return the site dto
     */
    private static Site createAEndSite(QuoteProductPlaceResponse aEndQuoteProductPlace,
                                          QuoteProductConfigResponse aEndQuoteProductConfig) {
        String networkStatus = aEndQuoteProductConfig.getNetworkStatus() != null ?
                aEndQuoteProductConfig.getNetworkStatus().getValue() : null;
        Location location = new Location(aEndQuoteProductPlace.getEircode(),
                                         new Address(aEndQuoteProductPlace.getFullAddress()),
                                         networkStatus,aEndQuoteProductPlace.getMultiEircode());

        return new Site(location);
    }

    /**
     * Create b end site site dto.
     *
     * @param bEndQuoteProductPlace the b end quote product place
     * @return the site dto
     */
    private static Site createBEndSite(QuoteProductPlaceResponse bEndQuoteProductPlace) {
        Location location = new Location(bEndQuoteProductPlace.getHandoverNode());
        return new Site(location);
    }


    /**
     * Populate order rejection details.
     *
     * @param rejectionDetails the rejection details
     * @param notificationType the notification type
     * @return the rejection details dto
     */
    private static RejectionDetails populateOrderRejectionDetails(RejectionDetailsResponse rejectionDetails,
                                                                  NotificationType notificationType) {
        RejectionDetails rejectionDetailsDTO = null;
        if (notificationType == NotificationType.REJECT && rejectionDetails != null) {
            rejectionDetailsDTO = new RejectionDetails(rejectionDetails.getRejectCode(),
                                                          rejectionDetails.getRejectReason());
        }
        return rejectionDetailsDTO;
    }

    /**
     * Populate quote rejection details rejection details dto.
     *
     * @param rejectionDetails the rejection details
     * @return the rejection details dto
     */
    private static RejectionDetails populateQuoteRejectionDetails(RejectionDetailsResponse rejectionDetails) {
        RejectionDetails rejectionDetailsDTO = null;
        if (rejectionDetails != null) {
            rejectionDetailsDTO = new RejectionDetails(rejectionDetails.getRejectCode(),
                                                          rejectionDetails.getRejectReason());
        }
        return rejectionDetailsDTO;
    }

    public static NotificationRequest createNotcomOrderForDelay(QuoteGroupOfflinePricingResponse quoteResponse,
                                                      QuoteOrderMapEntity quoteOrderMap) {

        return NotificationRequest.NotificationRequestBuilder.notificationRequestBuilder()
                .createdAt(quoteResponse.getQuoteDate().toString())
                .oao(quoteOrderMap.getSupplier())
                .dataContract(QuoteManagerConstants.QBTDC_DATA_CONTRACT_NAME)
                .originatorCode(QuoteManagerConstants.QBTDC_ORIGINATOR_CODE)
                .createdAt(quoteResponse.getQuoteDate().toString())
                .resellerTransactionId(quoteResponse.getResellerTransactionId())
                .resellerOrderRequestDateTime(DateUtils.btDateTimeToString(quoteResponse.getQuoteDate()))
                .operatorName(quoteOrderMap.getOperatorName())
                .operatorCode(quoteOrderMap.getOperatorCode())
                .orderNumber(quoteOrderMap.getOrderNumber())
                .serviceType(QuoteManagerConstants.QBTDC_TYPE)
                //.projectKey(createQuoteResponse.getProjectIdentifier())
                .delayReason(quoteResponse.getDelayReason())
                .build();

    }

    /**
     * Create notcom orders orders dto sent in the request.
     *
     * @param quoteGroupResponse the response from Quote Manager {@link CreateQuoteResponse}
     * @param quoteOrderMapEntity the quote order map  {@link QuoteOrderMapEntity}
     * @return the notification request {@link NotificationRequest}
     * @throws TalosNotFoundException when calling method createQbtdcDTOForAsyncCompletion
     */
    public static NotificationRequest createNotcomOrderForAsyncCompletion(GetQuoteGroupResponse quoteGroupResponse,
        QuoteOrderMapEntity quoteOrderMapEntity) throws TalosNotFoundException {
        String oao = QuoteHelper.fetchRelatedPartyByType(quoteGroupResponse.getRelatedPartyResponses(), Role.BUYER);
        String obo = QuoteHelper.fetchRelatedPartyByType(quoteGroupResponse.getRelatedPartyResponses(), Role.REQUESTOR);

        return NotificationRequest.NotificationRequestBuilder.notificationRequestBuilder()
            .talosOrderId(quoteGroupResponse.getQuoteGroupId())
            .oao(obo != null ? obo : oao)
            .obo(obo)
            .dataContract(QuoteManagerConstants.QBTDC_DATA_CONTRACT_NAME)
            .originatorCode(QuoteManagerConstants.QBTDC_ORIGINATOR_CODE)
            .resellerTransactionId(quoteGroupResponse.getResellerTransactionId())
            .resellerOrderRequestDateTime(DateUtils.btDateTimeToString(quoteGroupResponse.getQuoteDate()))
            .operatorName(quoteOrderMapEntity.getOperatorName())
            .operatorCode(quoteOrderMapEntity.getOperatorCode())
            .orderNumber(quoteOrderMapEntity.getOrderNumber())
            .serviceType(QuoteManagerConstants.QBTDC_TYPE)
            .createdAt(quoteGroupResponse.getQuoteDate().toString())
            .mode(Boolean.TRUE.equals(quoteOrderMapEntity.getSyncFlag()) ? QuoteMode.SYNC.getValue() : QuoteMode.ASYNC.getValue())
            .projectKey(quoteGroupResponse.getProjectIdentifier())
            .qbtdc(createQbtdcDTOForAsyncCompletion(quoteGroupResponse, quoteOrderMapEntity))
            .build();

    }

    /**
     * Create qbtdc dto from QuoteGroupResponse.
     *
     * @param quoteGroupResponse the response from Quote Manager {@link CreateQuoteResponse}
     * @param quoteOrderMapEntity the quote order map entity {@link QuoteOrderMapEntity}
     * @return the qbtdc dto {@link QuoteOrder}
     * @throws TalosNotFoundException when calling method mapQuoteForAsync
     */
    private static QuoteOrder createQbtdcDTOForAsyncCompletion(GetQuoteGroupResponse quoteGroupResponse,
        QuoteOrderMapEntity quoteOrderMapEntity) throws TalosNotFoundException {
        List<QuoteItem> quoteItemDTOList = new ArrayList<>();

        String recurringFrequency = QuoteHelper.fetchRecurringFrequencyForAsync(quoteGroupResponse.getQuotes().get(0));
        for (GetQuoteResponse quoteResponse : quoteGroupResponse.getQuotes()) {
            QuoteItem quoteItemDTO = mapQuoteForAsync(quoteResponse, quoteOrderMapEntity, quoteGroupResponse);
            quoteItemDTOList.add(quoteItemDTO);
        }
        return new QuoteOrder(recurringFrequency, quoteItemDTOList);
    }

    /**
     * Create quote item dto for notcom request.
     *
     * @param quoteResponse       the quote response from Quote Manager {@link GetQuoteResponse}
     * @param quoteOrderMapEntity the quote order map entity {@link QuoteOrderMapEntity}
     * @param quoteGroupResponse the quote group response from Quote Manager {@link GetQuoteGroupResponse}
     * @return the quote item dto {@link QuoteItem}
     * @throws TalosNotFoundException when fetching wag quote item id
     */
    private static QuoteItem mapQuoteForAsync(GetQuoteResponse quoteResponse,
        QuoteOrderMapEntity quoteOrderMapEntity, GetQuoteGroupResponse quoteGroupResponse)
        throws TalosNotFoundException {
        return QuoteItem.QuoteItemBuilder.quoteItemBuilder()
            .logicalLink(createLogicalLinkForAsync(quoteResponse.getLogicalResponse()))
            .customerAccess(createCustomerAccessForAsync(quoteResponse.getaEndResponse(), quoteGroupResponse.getServiceClass(), quoteGroupResponse.getConnectionType()))
            .wholesalerAccess(createWholesalerAccessForAsync(quoteResponse.getbEndResponse()))
            .rejectionDetails(populateQuoteRejectionDetailsForAsync(quoteResponse.getRejectionDetailsResponse()))
            .offlineQuoted(getOfflineQuoted1(quoteOrderMapEntity.getQuoteItemMapList(),
                quoteResponse.getQuoteId()) ? QuoteManagerConstants.YES : QuoteManagerConstants.NO)
            .quoteItemId(QuoteHelper.fetchWagQuoteItemId(quoteOrderMapEntity, quoteResponse.getQuoteId()))
            .term(quoteResponse.getQuoteItemTerm())
            .product(quoteGroupResponse.getServiceClass().name())
            .connectionType(quoteResponse.getConnectionType() != null ? quoteResponse.getConnectionType().getValue() : null)
            .nonRecurringPrice(QuoteHelper.fetchPriceForAsync(quoteResponse.getQuotePriceResponses(), PriceType.NON_RECURRING))
            .recurringPrice(QuoteHelper.fetchPriceForAsync(quoteResponse.getQuotePriceResponses(), PriceType.RECURRING))
            .etherflowRecurringPrice(QuoteHelper.fetchPriceForAsync(quoteResponse.getQuotePriceResponses(),
                PriceType.ETHERFLOW_RECURRING))
            .etherwayRecurringPrice(QuoteHelper.fetchPriceForAsync(quoteResponse.getQuotePriceResponses(),
                PriceType.ETHERWAY_RECURRING))
            .status(quoteResponse.getQuoteState().getValue())
            .notes(quoteResponse.getNotes())
            .group(quoteResponse.getGroup())
            .build();
    }

    /**
     * Create logical link dto for notcom request For Async FLow.
     *
     * @param quoteLogicalResponse the quote logical response from quote manager {@link GetQuoteLogicalResponse}
     * @return the logical link dto {@link LogicalLink}
     */
    private static LogicalLink createLogicalLinkForAsync(GetQuoteLogicalResponse quoteLogicalResponse) {
        String ipRange = quoteLogicalResponse.getIpRange() != null ?
                String.valueOf(quoteLogicalResponse.getIpRange()) : null;
        return new LogicalLink(Enum.valueOf(ActionFlag.class, quoteLogicalResponse.getAction().getValue()),
                               QuoteHelper.formatBandwidth(quoteLogicalResponse.getBandwidth()),
                               quoteLogicalResponse.getProfile().name(),ipRange);

    }

    /**
     * Create customer access access dto for notcom request from GetQuoteAEndResponse.
     *
     * @param quoteAEndResponse the quote AEnd response from Quote Manager {@link GetQuoteAEndResponse}
     * @param serviceClassType the service class for the quote {@link ServiceClassType}
     * @return the access dto {@link Access}
     */
    private static Access createCustomerAccessForAsync(GetQuoteAEndResponse quoteAEndResponse,
                                                       ServiceClassType serviceClassType, ConnectionType connectionType) {
        String sla = quoteAEndResponse.getSla() != null ? quoteAEndResponse.getSla().name() : null;
        String targetAccessSupplier = quoteAEndResponse.getTargetAccessSupplier() != null ?
                quoteAEndResponse.getTargetAccessSupplier().getValue() : null;
        String supplier = quoteAEndResponse.getAccessSupplier() != null ?
                quoteAEndResponse.getAccessSupplier().getValue() : null;
        return new Access(Enum.valueOf(ActionFlag.class, quoteAEndResponse.getAction().getValue()),
                          QuoteHelper.formatBandwidth(quoteAEndResponse.getBandwidth()),sla,
                          createAEndSiteForAsync(quoteAEndResponse),targetAccessSupplier,supplier,serviceClassType.name()
                          );

    }

    /**
     * Create a end site site dto for notcom request From GetQuoteAEndResponse.
     *
     * @param quoteAEndResponse  the a end quote response from quote manager {@link GetQuoteAEndResponse}
     * @return the site dto {@link Site}
     */
    private static Site createAEndSiteForAsync(GetQuoteAEndResponse quoteAEndResponse) {

        Address addressDTO = quoteAEndResponse.getFullAddress() != null
                ? new Address(quoteAEndResponse.getFullAddress()) : null;
        String networkStatus = quoteAEndResponse.getNetworkStatus() != null ?
                quoteAEndResponse.getNetworkStatus().getValue() : null;
        Location location = new Location(quoteAEndResponse.getEircode(),addressDTO,networkStatus,
                                         quoteAEndResponse.getMultiEircode());
        return new Site(location);
    }

    /**
     * Create wholesaler access dto for notcom request from QuoteBEndResponse.
     *
     * @param quoteBEndResponse the quote response from Quote Manager {@link GetQuoteBEndResponse}
     * @return the access dto {@link Access}
     */
    private static Access createWholesalerAccessForAsync(GetQuoteBEndResponse quoteBEndResponse) {
        return new Access(Enum.valueOf(ActionFlag.class, quoteBEndResponse.getAction().getValue()),
                          createBEndSiteForAsync(quoteBEndResponse));

    }

    /**
     * Create b end site site dto from GetQuoteBEndResponse.
     *
     * @param quoteBEndResponse the b end quote product place {@link GetQuoteBEndResponse)
     * @return the site dto {@link Site}
     */
    private static Site createBEndSiteForAsync(GetQuoteBEndResponse quoteBEndResponse) {
        Location location = new Location(quoteBEndResponse.getHandOverNode());
        return new Site(location);
    }

    /**
     * Populate quote rejection details rejection details dto from GetRejectionDetailsResponse
     *
     * @param rejectionDetails the rejection details {@link GetRejectionDetailsResponse}
     * @return the rejection details dto {@link RejectionDetails}
     */
    private static RejectionDetails populateQuoteRejectionDetailsForAsync(
        GetRejectionDetailsResponse rejectionDetails) {
        RejectionDetails rejectionDetailsDTO = null;
        if (rejectionDetails != null) {
            rejectionDetailsDTO = new RejectionDetails(rejectionDetails.getRejectCode(),
                rejectionDetails.getRejectReason());
        }
        return rejectionDetailsDTO;
    }

    /**
     * Method to get the value of offlineQuoted for a quote from the QuoteItemMap
     * @param quoteItemMapEntities List of QuoteItemMapEntity {@link QuoteItemMapEntity}}
     * @param quoteId Id of the quote {@link Long}
     * @return boolean value for offlineQuoted {@link boolean}
     */
    private static boolean getOfflineQuoted(List<QuoteItemMapEntity> quoteItemMapEntities, Long quoteId) {
        return quoteItemMapEntities.stream()
            .filter(quoteItemMapEntity -> quoteItemMapEntity.getQuoteId().equals(quoteId)).findFirst().get()
            .isOfflineQuoted();
    }
    

        private static boolean getOfflineQuoted1(List<QuoteItemMapEntity> quoteItemMapEntities, Long quoteId) {
        logger.info("getOfflineQuoted() called with quoteId: {}", quoteId);

        if (quoteItemMapEntities == null) {
            logger.warn("quoteItemMapEntities is null");
            return false;
        }

        logger.info("quoteItemMapEntities size: {}", quoteItemMapEntities.size());

        for (QuoteItemMapEntity entity : quoteItemMapEntities) {
            logger.debug("Checking QuoteItemMapEntity - quoteId: {}, isOfflineQuoted: {}",
                    entity.getQuoteId(), entity.isOfflineQuoted());
        }

        try {
            QuoteItemMapEntity matchedEntity = quoteItemMapEntities.stream()
                    .filter(entity -> quoteId != null && quoteId.equals(entity.getQuoteId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        logger.warn("No matching entity found for quoteId: {}", quoteId);
                        return new IllegalArgumentException("No matching entity found for quoteId: " + quoteId);
                    });

            logger.info("Matched entity found: quoteId = {}, isOfflineQuoted = {}",
                    matchedEntity.getQuoteId(), matchedEntity.isOfflineQuoted());

            return matchedEntity.isOfflineQuoted();
        } catch (Exception e) {
            logger.error("Exception while evaluating offlineQuoted for quoteId {}: {}", quoteId, e.getMessage(), e);
            return false;
        }
    }

}
