package com.btireland.talos.ethernet.engine.client.asset.ordermanager;


import com.btireland.talos.core.common.rest.exception.NotFoundException;
import com.btireland.talos.ethernet.engine.dto.PBTDCGlanIdDTO;
import com.btireland.talos.core.common.rest.exception.BadRequestException;
import com.btireland.talos.quote.facade.dto.ordermanager.QuoteOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "ordermanager", url = "${application.talos.ordermanager.url}")
public interface OrderManagerClient {

    @PostMapping(value = "/api/v1/wag/pbtdc-orders", consumes = "application/json")
    PBTDCOrderDTO createPBTDCOrder(PBTDCOrderDTO pbtdcOrderRequest) throws BadRequestException;

    @GetMapping(value = "/api/v1/wag/quote/{id}", consumes = "application/json")
    QuoteDetailsDTO fetchQuote(@PathVariable("id") Long quoteId) throws NotFoundException;

    @GetMapping(value = "/api/v1/wag/agent/{orderId}", consumes = "application/json")
    AgentDTO getAgentByWagOrderId(@PathVariable("orderId") Long orderId) throws NotFoundException;

    @PutMapping(value = "/api/v1/wag/quote/{id}", consumes = "application/json")
    QuoteDTO updateQuote(@PathVariable("id") Long id,QuoteDTO quoteDTO) throws BadRequestException,NotFoundException;

    @PutMapping(value = "/api/v1/wag/pbtdc-orders/{id}/glanid", consumes = "application/json")
    PBTDCOrderDTO updatePBTDCOrderGlanId(@PathVariable("id") Long id, List<PBTDCGlanIdDTO> pbtdcGlanIdDTOList) throws BadRequestException,NotFoundException;

    @PostMapping(value = "/api/v1/wag/qbtdc-orders", consumes = "application/json")
    QuoteOrder createQBTDCOrder(QuoteOrder qbtdcOrderRequest) throws BadRequestException;

    @PutMapping(value = "/api/v1/wag/quote", consumes = "application/json")
    List<QuoteDTO> updateAllQuotes(List<QuoteDTO> quoteDTO) throws BadRequestException;

    @PutMapping(value = "/api/v1/wag/quote/expire/{period}", consumes = "application/json")
    void expireQuotes(@PathVariable("period") int period);

    @GetMapping(value = "/api/v1/wag/pbtdc-orders/{id}", consumes = "application/json")
    PBTDCOrderDTO findPBTDCOrderById(@PathVariable("id") Long quoteId) throws BadRequestException,NotFoundException;

    @PostMapping(value = "/api/v1/wag/searchQuotes", consumes = "application/json")
    SearchQuoteResponseDTO searchQuote(SearchQuoteRequestDTO searchQuoteRequestDTO) throws BadRequestException;
}
