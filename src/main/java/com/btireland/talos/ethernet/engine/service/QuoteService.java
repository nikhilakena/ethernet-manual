package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.client.asset.ordermanager.OrderManagerClient;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.SearchQuoteRequestDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.SearchQuoteResponseDTO;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.domain.QbtdcQuote;
import com.btireland.talos.ethernet.engine.enums.TalosOrderStatus;
import com.btireland.talos.ethernet.engine.facade.QBTDCOrderMapper;
import com.btireland.talos.ethernet.engine.util.QuoteStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class QuoteService {

    private QbtdcOrdersPersistenceService qbtdcOrdersPersistenceService;

    private OrderManagerService orderManagerService;

    private QBTDCOrderMapper mapper;

    private OrderManagerClient orderManagerClient;

    @Value("${application.quote.expiry-days}")
    private int maximumDaysBeforeExpiry;

    public QuoteService(QbtdcOrdersPersistenceService qbtdcOrdersPersistenceService, OrderManagerService orderManagerService, QBTDCOrderMapper mapper, OrderManagerClient orderManagerClient) {
        this.qbtdcOrdersPersistenceService = qbtdcOrdersPersistenceService;
        this.orderManagerService = orderManagerService;
        this.mapper = mapper;
        this.orderManagerClient = orderManagerClient;
    }

    public void expireUnusedQuotes() {
        LocalDateTime maxDate = LocalDateTime.now().minusDays(maximumDaysBeforeExpiry);

        //Fetching all quote items that were quoted for more than 90 days
        List<QbtdcQuote> inactiveQuoteItems = qbtdcOrdersPersistenceService.findAllInactiveQuotes(QuoteStatus.QUOTED.getValue(), TalosOrderStatus.TALOS_COMPLETE.getValue(), maxDate);
        if (!inactiveQuoteItems.isEmpty()) {
            log.info("Expiring quote items with id {}", inactiveQuoteItems.stream().map(quote -> quote.getWagQuoteItemId().toString()).collect(Collectors.joining(",")));
            //Updating status as Expired
            inactiveQuoteItems.forEach(quoteItem -> quoteItem.setStatus(QuoteStatus.EXPIRED.getValue()));
        }

        //Call WAG Api to update the quotes.
        orderManagerService.expireQuotes(maximumDaysBeforeExpiry);
    }

    public SearchQuoteResponseDTO searchQuote(SearchQuoteRequestDTO quoteDTO) {
        return orderManagerClient.searchQuote(quoteDTO);
    }

}

