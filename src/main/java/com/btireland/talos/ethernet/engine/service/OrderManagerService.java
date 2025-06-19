package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.rest.exception.BadRequestException;
import com.btireland.talos.core.common.rest.exception.NotFoundException;
import com.btireland.talos.core.common.rest.exception.ResourceNotFoundException;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.*;
import com.btireland.talos.ethernet.engine.dto.PBTDCGlanIdDTO;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderManagerService {

    private OrderManagerClient orderManagerClient;

    public OrderManagerService(OrderManagerClient orderManagerClient) {
        this.orderManagerClient = orderManagerClient;
    }

    /**
     * @param pbtdcOrderRequest
     * @return
     * @throws ResourceNotFoundException if the asset does not exist
     * @throws BadRequestException       if order already exists or the request has invalid data
     */
    public PBTDCOrderDTO createPBTDCOrder(PBTDCOrderDTO pbtdcOrderRequest) throws OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException {
        try {
            return orderManagerClient.createPBTDCOrder(pbtdcOrderRequest);
        } catch (BadRequestException bre) {
            throw new OrderManagerServiceBadRequestException(bre.getMessage(), bre);
        } catch (NotFoundException nfe) {
            throw new OrderManagerServiceNotFoundException("Resource CreatePBTDCOrder in Order Manager service could not be " +
                    "reached",
                    nfe);
        }
    }

    public Optional<PBTDCOrderDTO> findPBTDCOrderById(Long orderId) {
        try {
            return Optional.of(orderManagerClient.findPBTDCOrderById(orderId));
        } catch (NotFoundException e) {
            log.warn("Could not find PBTDC Order by Id : " + orderId);
            return Optional.empty();
        }
    }

    public Optional<AgentDTO> getAgentByWagOrderId(Long wagOrderId) {
        try {
            return Optional.of(orderManagerClient.getAgentByWagOrderId(wagOrderId));
        } catch (NotFoundException e) {
            log.warn("Could not find agent for WagOrderId : " + wagOrderId);
            return Optional.empty();
        }
    }

    public Optional<QuoteDetailsDTO> getQuoteItem(Long quoteItemId) {
        try {
            return Optional.of(orderManagerClient.fetchQuote(quoteItemId));
        } catch (NotFoundException e) {
            log.warn("Could not find quote item : " + quoteItemId);
            return Optional.empty();
        }
    }

    public QuoteDTO updateQuote(QuoteDTO quoteDTO) throws OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException {
        QuoteDTO updatedQuoteDTO = null;
        try {
            updatedQuoteDTO = orderManagerClient.updateQuote(quoteDTO.getId(), quoteDTO);
        } catch (BadRequestException bre) {
            String errorMessage = "Failed in updating quote with quote item id " + quoteDTO.getId();
            log.error(errorMessage);
            throw new OrderManagerServiceBadRequestException(errorMessage, bre);
        } catch (NotFoundException nfe) {
            String errorMessage = "No Quote found with quote item id " + quoteDTO.getId();
            log.error(errorMessage);
            throw new OrderManagerServiceNotFoundException(errorMessage, nfe);
        }
        return updatedQuoteDTO;
    }


    public void expireQuotes(int days) {
        orderManagerClient.expireQuotes(days);
    }

    public PBTDCOrderDTO updatePBTDCOrderGlanId(Long orderId, List<PBTDCGlanIdDTO> pbtdcGlanIdDTOList) throws OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException {
        PBTDCOrderDTO pbtdcOrderDTO;
        try {
            pbtdcOrderDTO = orderManagerClient.updatePBTDCOrderGlanId(orderId, pbtdcGlanIdDTOList);
        } catch (BadRequestException bre) {
            String errorMessage = "Failed to update glanId for Order Id " + orderId;
            log.error(errorMessage);
            throw new OrderManagerServiceBadRequestException(errorMessage, bre);
        } catch (NotFoundException nfe) {
            String errorMessage = "No Order found with Order Id " + orderId;
            log.error(errorMessage);
            throw new OrderManagerServiceNotFoundException(errorMessage, nfe);
        }
        return pbtdcOrderDTO;
    }

}
