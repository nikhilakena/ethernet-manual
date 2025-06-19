package com.btireland.talos.quote.facade.service.api;

import com.btireland.talos.core.aop.annotation.LoggedApiMethod;
import com.btireland.talos.ethernet.engine.dto.QBTDCOrderRequestDTO;
import com.btireland.talos.ethernet.engine.dto.QBTDCOrderResponseDTO;
import com.btireland.talos.ethernet.engine.exception.PersistanceException;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrder;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrderResponse;
import com.btireland.talos.quote.facade.process.processor.QuoteProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class QuoteAPI {

    Logger LOGGER = LoggerFactory.getLogger(QuoteProcessor.class);

    private final QuoteProcessor quoteProcessor;

    public QuoteAPI(QuoteProcessor quoteProcessor) {
        this.quoteProcessor = quoteProcessor;
    }

    /**
     * Method to create quote by calling quote manager API
     * @param request QBTDCOrder xml object
     * @param oao oao of the order
     * @return QBTDCOrderREsponse xml object
     */
    @LoggedApiMethod
    public QBTDCOrderResponse createQuote(QBTDCOrder request, String oao) throws PersistanceException {
        LOGGER.info("Quote request is being processed by Quote Facade");
        return quoteProcessor.createQuote(request, oao);
    }

    @LoggedApiMethod
    public QBTDCOrderResponseDTO createQBTDCQuote(QBTDCOrderRequestDTO request) throws PersistanceException {
        LOGGER.info("Quote request is being processed by Quote Facade for createQBTDCQuote");
        return quoteProcessor.createQBTDCQuote(request.getQuoteOrderMap(), request.getCreateQuoteRequest());
    }
}
