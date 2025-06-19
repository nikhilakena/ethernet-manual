package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCOrderDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QuoteDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QuoteDetailsDTO;
import com.btireland.talos.ethernet.engine.domain.*;
import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import com.btireland.talos.ethernet.engine.exception.UserUnauthorizedException;
import com.btireland.talos.ethernet.engine.exception.ValidationException;
import com.btireland.talos.ethernet.engine.facade.QBTDCOrderMapper;
import com.btireland.talos.ethernet.engine.util.LocationType;
import com.btireland.talos.ethernet.engine.util.RejectCode;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class OrderValidationService {

    private OrderManagerService orderManagerService;

    private PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService;

    private QBTDCOrderMapper qbtdcOrderMapper;

    private static final String TALOS_COMPLETE = "talos complete";

    /**
     * Fetch quote item from wag and persist it and Check that a quote is valid for a given order
     * @param orderId order id
     */
    public void validateAndPersistQuote(Long orderId) throws ValidationException, UserUnauthorizedException {
        //Retrieve orders from local DB
        Pbtdc order = pbtdcOrdersPersistenceService.findByOrderId(orderId);

        Optional<PBTDCOrderDTO> pbtdcOrderDTO = orderManagerService.findPBTDCOrderById(orderId);
        if (!pbtdcOrderDTO.isEmpty() && !pbtdcOrderDTO.get().getPbtdcs().getConnectionType().equals(ConnectionType.ETHERWAY_STANDARD.name()) &&
                pbtdcOrderDTO.get().getPbtdcs().getBtGroupRef() == null) {
            populateRejectDetails(order, RejectCode.CODE_759_BT_GROUP_REF.getCode(), RejectCode.CODE_759_BT_GROUP_REF.getRejectReason());
            pbtdcOrdersPersistenceService.update(order);
            String error = "BT Group Ref is invalid for connection_type " +  pbtdcOrderDTO.get().getPbtdcs().getConnectionType();
            log.error(error);
            throw new ValidationException(error);
        }

        Optional<QuoteDetailsDTO> quoteDetailsDTO = orderManagerService.getQuoteItem(order.getRefQuoteItemId());

        if (quoteDetailsDTO.isEmpty()) {
            populateRejectDetails(order, RejectCode.CODE_759.getCode(), RejectCode.CODE_759.getRejectReason());
            pbtdcOrdersPersistenceService.update(order);
            String error = "Quote " + order.getRefQuoteItemId() + " is invalid";
            log.error(error);
            throw new ValidationException(error);
        }

        if (quoteDetailsDTO.isEmpty()) {
            populateRejectDetails(order, RejectCode.CODE_759.getCode(), RejectCode.CODE_759.getRejectReason());
            pbtdcOrdersPersistenceService.update(order);
            String error = "Quote " + order.getRefQuoteItemId() + " is invalid";
            log.error(error);
            throw new ValidationException(error);
        }

        QuoteDTO quote = quoteDetailsDTO.get().getQuote();
        //Check if pbtdc and qbtdc belong to the same customer
        if(!validateQuote(quote,order)) {
            populateRejectDetails(order,RejectCode.CODE_759.getCode(),RejectCode.CODE_759.getRejectReason());
            pbtdcOrdersPersistenceService.update(order);
            String error = String.format("QBTDC does not belong to the customers order,QBTDC oao: %s PBTDC oao: %s",
                    quote.getOao(), order.getOao());
            log.error(error);
            throw new UserUnauthorizedException(error);
        }

        updateOrderWithQuoteInfo(quoteDetailsDTO.get(), order);
        pbtdcOrdersPersistenceService.update(order);
    }

    private void populateRejectDetails(Pbtdc order,int rejectCode,String rejectReason) {
        order.setRejectionDetails(RejectionDetails.builder()
                .orders(order)
                .rejectCode(String.valueOf(rejectCode))
                .rejectReason(rejectReason)
                .build());
    }

    private boolean validateQuote(QuoteDTO quoteDTO, Pbtdc order) {
        return order.getOao().equalsIgnoreCase(quoteDTO.getOao());
    }

    private void updateOrderWithQuoteInfo(QuoteDetailsDTO quoteDetailsDTO, Pbtdc order) {
        QuoteDTO quoteDTO = quoteDetailsDTO.getQuote();
        order.setServiceClass(quoteDTO.getProduct());
        if(quoteDTO.getLogicalActionFlag()!=null) {
            LogicalLink logicalLink = order.getLogicalLink() != null ? order.getLogicalLink() : LogicalLink.builder().build();
            logicalLink.setAction(ActionFlag.fromString(quoteDTO.getLogicalActionFlag()));
            logicalLink.setProfile(quoteDTO.getLogicalProfile());
            logicalLink.setBandWidth(quoteDTO.getLogicalBandwidth());
            order.setLogicalLink(logicalLink);
        }

        Access customerAccess = order.getCustomerAccess();
        customerAccess.setBandWidth(quoteDTO.getAendBandwidth());
        customerAccess.setSla(quoteDTO.getAendSla());
        if (customerAccess.getSite() == null) {
            customerAccess.setSite(Site.builder().build());
        }
        customerAccess.getSite().setLocation(Location.builder().id(quoteDTO.getAendEircode())
                .type(LocationType.EIRCODE.name())
                .address(Address.builder().fullAddress(quoteDetailsDTO.getQuote().getAendAddress()).build())
                .build());

        if(quoteDTO.getBendActionFlag()!=null) {
            Access wholesalerAccess = order.getWholesalerAccess() != null ? order.getWholesalerAccess() : Access.builder().build();
            wholesalerAccess.setAction(ActionFlag.fromString(quoteDTO.getBendActionFlag()));
            if (quoteDTO.getBendHandOverNode() != null) {
                Site site = Site.builder()
                        .location(Location.builder().type(LocationType.HANDOVER.name()).id(quoteDTO.getBendHandOverNode()).build())
                        .build();
                wholesalerAccess.setSite(site);
            }
            order.setWholesalerAccess(wholesalerAccess);
        }
        order.setQuote(qbtdcOrderMapper.toQuote(quoteDTO, quoteDetailsDTO.getQbtdcs(), quoteDetailsDTO.getOrders()));

    }

    public void checkDuplicateOrder(Long orderId) throws ValidationException {
        //Retrieve orders from local DB
        Pbtdc order = pbtdcOrdersPersistenceService.findByOrderId(orderId);
        List<Pbtdc> orderList = pbtdcOrdersPersistenceService.findActiveOrdersByRefQuoteItemId(order.getRefQuoteItemId(),TALOS_COMPLETE);
        if(orderList.size()>1){
            populateRejectDetails(order,RejectCode.CODE_760.getCode(),RejectCode.CODE_760.getRejectReason());
            pbtdcOrdersPersistenceService.update(order);
            throw new ValidationException("Active Order already in progress for the Quote Item Reference ID"+order.getRefQuoteItemId());
        }
    }
}
