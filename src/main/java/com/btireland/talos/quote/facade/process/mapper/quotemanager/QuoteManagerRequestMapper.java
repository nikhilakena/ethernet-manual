package com.btireland.talos.quote.facade.process.mapper.quotemanager;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.soap.orders.OrderQbtdc;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrder;
import com.btireland.talos.quote.facade.base.enumeration.internal.ProfileType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteItemNameType;
import com.btireland.talos.quote.facade.base.enumeration.internal.RecurringChargePeriodType;
import com.btireland.talos.quote.facade.base.enumeration.internal.Role;
import com.btireland.talos.quote.facade.base.enumeration.internal.ServiceClassType;
import com.btireland.talos.quote.facade.base.enumeration.internal.SlaType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.NetworkType;
import com.btireland.talos.quote.facade.dto.quotemanager.request.CreateQuoteRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.QuoteItemRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.QuotePriceRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.QuoteProductConfigRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.QuoteProductPlaceRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.QuoteProductRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.QuoteRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.RelatedPartyRequest;
import com.btireland.talos.quote.facade.process.helper.QuoteHelper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants.BT_DATE_TIME_FORMAT;

@Component
public class QuoteManagerRequestMapper {

    /**
     * Method to create CreateQuoteItemRequest from incoming QBTDCOrder xml object
     * @param qbtdcOrderRequest QBTDCOrder xml object
     * @param quoteGroupId Unique Identifier for the Quote Group
     * @param oao oao of the quote request
     * @return CreateQuoteRequest object
     */
    public static CreateQuoteRequest createQuoteManagerRequest(QBTDCOrder qbtdcOrderRequest, Long quoteGroupId, String oao) throws TalosNotFoundException {
        List<QuoteRequest> quoteRequests = new ArrayList<>();
        for (OrderQbtdc.ORDERDATA.QUOTEITEMREQUEST xmlQuoteItemRequest :
                qbtdcOrderRequest.getORDER().getORDERDATA().getQUOTEITEMREQUEST()) {
            QuoteRequest quoteRequest = createQuoteRequest(qbtdcOrderRequest, xmlQuoteItemRequest, oao);
            quoteRequests.add(quoteRequest);
        }

        return new CreateQuoteRequest(quoteGroupId,
                LocalDateTime.parse(qbtdcOrderRequest.getORDER().getHEADER().getDATETIMESTAMP(), BT_DATE_TIME_FORMAT),
                quoteRequests);
    }

    /**
     * Method to create NotificationRequest from the QUOTEITEMREQUEST xml object
     * @param qbtdcOrderRequest QBTDCOrder xml object
     * @param xmlQuoteItemRequest QUOTEITEMREQUEST xml object
     * @param oao oao of the request
     * @return NotificationRequest
     */
    public static QuoteRequest createQuoteRequest(QBTDCOrder qbtdcOrderRequest, OrderQbtdc.ORDERDATA.QUOTEITEMREQUEST xmlQuoteItemRequest,
                                                  String oao) throws TalosNotFoundException {
        OrderQbtdc.ORDERDATA.ORDERDETAILS orderDetails = qbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS();
        List<RelatedPartyRequest> relatedPartyRequests = createRelatedPartyRequests(orderDetails, oao);
        QuotePriceRequest quotePriceRequest = new QuotePriceRequest(
            RecurringChargePeriodType.valueOf(qbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getRECURRINGFREQUENCY()));
        List<QuoteItemRequest> quoteItemRequests = createQuoteItemRequests(xmlQuoteItemRequest);

        return new QuoteRequest(orderDetails.getPROJECTKEY(),
                BooleanUtils.toBooleanObject(orderDetails.getSYNCREQUIRED()),
                String.valueOf(qbtdcOrderRequest.getORDER().getHEADER().getTRANSACTIONID()),
                ServiceClassType.valueOf(xmlQuoteItemRequest.getPRODUCT()),
                xmlQuoteItemRequest.getCONNECTIONTYPE() != null ? ConnectionType.valueOf(xmlQuoteItemRequest.getCONNECTIONTYPE()) : null,
                xmlQuoteItemRequest.getGROUP(),
                relatedPartyRequests, quotePriceRequest, quoteItemRequests);
    }

    /**
     * Method to create relatedParties List from the OrderDetails xml object
     * @param orderDetails OrderDetails xml object
     * @param oao oao of the request
     * @return List of RelatedPartyRequest
     */
    public static List<RelatedPartyRequest> createRelatedPartyRequests(OrderQbtdc.ORDERDATA.ORDERDETAILS orderDetails
            , String oao) {
        List<RelatedPartyRequest> relatedPartyRequests = new ArrayList<>();
        relatedPartyRequests.add(new RelatedPartyRequest(oao, Role.BUYER));
        if(orderDetails.getOBO() != null) {
            relatedPartyRequests.add(new RelatedPartyRequest(orderDetails.getOBO(), Role.REQUESTOR));
        }
        return relatedPartyRequests;
    }

    /**
     * Method to create List of QuoteItemRequest from the QUOTEITEMREQUEST xml Object
     * @param xmlQuoteItemRequest QUOTEITEMREQUEST xml object
     * @return List of QuoteItemRequest
     */
    public static List<QuoteItemRequest> createQuoteItemRequests(OrderQbtdc.ORDERDATA.QUOTEITEMREQUEST xmlQuoteItemRequest) throws TalosNotFoundException {
        List<QuoteItemRequest> quoteItemRequests = new ArrayList<>();
        QuoteProductConfigRequest quoteProductConfigRequestForAEnd = createQuoteProductConfigRequestForAEnd(xmlQuoteItemRequest);
        QuoteProductConfigRequest quoteProductConfigRequestForBEnd = createQuoteProductConfigRequestForBEnd(xmlQuoteItemRequest);
        QuoteProductConfigRequest quoteProductConfigRequestForLogical = createQuoteProductConfigRequestForLogical(xmlQuoteItemRequest);
        QuoteProductPlaceRequest quoteProductPlaceRequestForAEnd = createQuoteProductPlaceRequestForAEnd(xmlQuoteItemRequest);
        QuoteProductPlaceRequest quoteProductPlaceRequestForBEnd = createQuoteProductPlaceRequestForBEnd(xmlQuoteItemRequest);
        QuoteProductRequest quoteProductRequestForAEnd = new QuoteProductRequest(quoteProductPlaceRequestForAEnd, quoteProductConfigRequestForAEnd);
        QuoteProductRequest quoteProductRequestForBEnd = new QuoteProductRequest(quoteProductPlaceRequestForBEnd, quoteProductConfigRequestForBEnd);
        QuoteProductRequest quoteProductRequestForLogical = new QuoteProductRequest(quoteProductConfigRequestForLogical);

        quoteItemRequests.add(new QuoteItemRequest(QuoteItemNameType.A_END, Integer.valueOf(xmlQuoteItemRequest.getTERM()), quoteProductRequestForAEnd));
        quoteItemRequests.add(new QuoteItemRequest(QuoteItemNameType.B_END, Integer.valueOf(xmlQuoteItemRequest.getTERM()), quoteProductRequestForBEnd));
        quoteItemRequests.add(new QuoteItemRequest(QuoteItemNameType.LOGICAL, Integer.valueOf(xmlQuoteItemRequest.getTERM()), quoteProductRequestForLogical));

        return quoteItemRequests;
    }

    /**
     * Method to create QuoteProductConfigRequest for AEnd from the QuoteItemRequest xml object
     * @param xmlQuoteItemRequest QUOTEITEMREQUEST xml Object
     * @return QuoteProductConfigRequest
     */
    public static QuoteProductConfigRequest createQuoteProductConfigRequestForAEnd(OrderQbtdc.ORDERDATA.QUOTEITEMREQUEST xmlQuoteItemRequest) throws TalosNotFoundException {
        NetworkType accessSupplier = null;
        SlaType sla = null;
        if(!StringUtils.isEmpty(xmlQuoteItemRequest.getAEND().getACCESS().getACCESSSUPPLIER())) {
            accessSupplier =
                    QuoteHelper.fetchNetworkType(xmlQuoteItemRequest.getAEND().getACCESS().getACCESSSUPPLIER());
        }
        if(!StringUtils.isEmpty(xmlQuoteItemRequest.getAEND().getACCESS().getSLA())) {
            sla = SlaType.valueOf(xmlQuoteItemRequest.getAEND().getACCESS().getSLA());
        }
        Float bandwidth = xmlQuoteItemRequest.getAEND().getACCESS().getBANDWIDTH() != null
            ?Float.valueOf(xmlQuoteItemRequest.getAEND().getACCESS().getBANDWIDTH()) : null;

        return new QuoteProductConfigRequest(bandwidth,
                    QuoteHelper.fetchActionType(xmlQuoteItemRequest.getAEND().getACCESS().getACTIONFLAG()),
                    accessSupplier,sla);
    }

    /**
     * Method for creating QuoteProductConfigRequest for BEnd from the QUOTEITEMREQUEST xml object
     * @param xmlQuoteItemRequest QUOTITEMREQUEST xml object
     * @return QuoteProduuctConfigRequest
     */
    public static QuoteProductConfigRequest createQuoteProductConfigRequestForBEnd(OrderQbtdc.ORDERDATA.QUOTEITEMREQUEST xmlQuoteItemRequest) throws TalosNotFoundException {
        return new QuoteProductConfigRequest(QuoteHelper.fetchActionType(xmlQuoteItemRequest.getBEND().getACCESS().getACTIONFLAG()));
    }

    /**
     * Method for creating QuoteProductConfigRequest for Logical from the QUOTEITEMREQUEST xml object
     * @param xmlQuoteItemRequest QUOTITEMREQUEST xml object
     * @return QuoteProduuctConfigRequest
     */
    public static QuoteProductConfigRequest createQuoteProductConfigRequestForLogical(OrderQbtdc.ORDERDATA.QUOTEITEMREQUEST xmlQuoteItemRequest) throws TalosNotFoundException {
        Float bandwidth = xmlQuoteItemRequest.getLOGICAL().getBANDWIDTH() != null
            ? Float.valueOf(xmlQuoteItemRequest.getLOGICAL().getBANDWIDTH()) : null;
        return new QuoteProductConfigRequest(bandwidth,
                ProfileType.valueOf(xmlQuoteItemRequest.getLOGICAL().getPROFILE()),
                QuoteHelper.fetchActionType(xmlQuoteItemRequest.getLOGICAL().getACTIONFLAG()),
                xmlQuoteItemRequest.getIP() != null ? Long.valueOf(xmlQuoteItemRequest.getIP().getIPRANGE()) : null);
    }

    /**
     * Method for creating QuoteProductPlaceRequest for AEnd from the QUOTEITEMREQUEST xml object
     * @param xmlQuoteItemRequest QUOTITEMREQUEST xml object
     * @return QuoteProduuctPlaceRequest
     */
    public static QuoteProductPlaceRequest createQuoteProductPlaceRequestForAEnd(OrderQbtdc.ORDERDATA.QUOTEITEMREQUEST xmlQuoteItemRequest) {
        QuoteProductPlaceRequest quoteProductPlaceRequest = new QuoteProductPlaceRequest();
        quoteProductPlaceRequest.setEircode(xmlQuoteItemRequest.getAEND().getLOCATION().getEIRCODE());
        return quoteProductPlaceRequest;
    }

    /**
     * Method for creating QuoteProductPlaceRequest for BEnd from the QUOTEITEMREQUEST xml object
     * @param xmlQuoteItemRequest QUOTITEMREQUEST xml object
     * @return QuoteProduuctPlaceRequest
     */
    public static QuoteProductPlaceRequest createQuoteProductPlaceRequestForBEnd(OrderQbtdc.ORDERDATA.QUOTEITEMREQUEST xmlQuoteItemRequest) {
        QuoteProductPlaceRequest quoteProductPlaceRequest = new QuoteProductPlaceRequest();
        quoteProductPlaceRequest.setHandoverNode(xmlQuoteItemRequest.getBEND().getLOCATION().getHANDOVERNODE());
        return quoteProductPlaceRequest;
    }
}