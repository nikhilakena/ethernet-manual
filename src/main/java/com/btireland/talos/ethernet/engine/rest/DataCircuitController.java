package com.btireland.talos.ethernet.engine.rest;

import com.btireland.talos.ethernet.engine.client.asset.ordermanager.SearchQuoteRequestDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.SearchQuoteResponseDTO;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderJsonResponse;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderRequestDTO;
import com.btireland.talos.ethernet.engine.dto.QBTDCOrderRequestDTO;
import com.btireland.talos.ethernet.engine.dto.QBTDCOrderResponseDTO;
import com.btireland.talos.ethernet.engine.exception.AuthenticationException;
import com.btireland.talos.ethernet.engine.exception.BadRequestException;
import com.btireland.talos.ethernet.engine.exception.PersistanceException;
import com.btireland.talos.ethernet.engine.exception.RequestValidationException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceNotFoundException;
import com.btireland.talos.ethernet.engine.facade.PBTDCOrderFacade;
import com.btireland.talos.ethernet.engine.service.QuoteService;
import com.btireland.talos.quote.facade.service.api.QuoteAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Data circuit Controller", description = "Controller for ordering data circuit orders")
@RequestMapping("/api/v1/dc")
public class DataCircuitController {

    private PBTDCOrderFacade pbtcOrderFacade;
    private QuoteAPI quoteAPI;
    private QuoteService quoteService;

    public DataCircuitController(PBTDCOrderFacade pbtcOrderFacade, QuoteAPI quoteAPI, QuoteService quoteService) {
        this.pbtcOrderFacade = pbtcOrderFacade;
        this.quoteAPI = quoteAPI;
        this.quoteService = quoteService;
    }

    @Operation(summary = "generate PBTDC order", description = "Generates PBTDC order")
    @PostMapping(path = "/generate-PBTDC-order")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PBTDCOrderJsonResponse generatePBTDCOrder(@RequestBody PBTDCOrderRequestDTO request) throws BadRequestException,
            RequestValidationException, AuthenticationException, PersistanceException,
            OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException {
        return pbtcOrderFacade.createPBTDCOrder(request);
    }

    @Operation(summary = "generate QBTDC order", description = "Generates QBTDC order")
    @PostMapping(path = "/generate-QBTDC-order")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public QBTDCOrderResponseDTO generateQBTDCOrder(@RequestBody QBTDCOrderRequestDTO qbtdcOrderRequestDTO) throws PersistanceException {
        return quoteAPI.createQBTDCQuote(qbtdcOrderRequestDTO);
    }

    @Operation(summary = "Fetch all quotes based on given filter", description = "Fetches all the quotes based on given filter")
    @PostMapping(path = "/search-quote")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SearchQuoteResponseDTO searchQuote(@RequestBody SearchQuoteRequestDTO searchQuoteRequestDTO) {
        return quoteService.searchQuote(searchQuoteRequestDTO);
    }

}
