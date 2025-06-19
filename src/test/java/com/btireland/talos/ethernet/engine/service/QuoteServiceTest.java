package com.btireland.talos.ethernet.engine.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.OrderManagerClient;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.domain.QbtdcQuote;
import com.btireland.talos.ethernet.engine.facade.QBTDCOrderMapper;
import com.btireland.talos.ethernet.engine.util.QbtdcFactory;
import com.btireland.talos.ethernet.engine.util.QuoteStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@UnitTest
@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    @Mock
    private QbtdcOrdersPersistenceService qbtdcOrdersPersistenceService;

    @Mock
    private OrderManagerService orderManagerService;

    @Mock
    private QBTDCOrderMapper mapper;

    @InjectMocks
    private QuoteService quoteService;

    @Mock
    private OrderManagerClient orderManagerClient;

    @BeforeEach
    public void setUp() {
        quoteService = new QuoteService(qbtdcOrdersPersistenceService, orderManagerService, mapper, orderManagerClient);
    }

    @Test
    @DisplayName("Order manager is invoked to expire quotes")
    void expireQuotes() {
        final List<QbtdcQuote> quoteItems = QbtdcFactory.qbtdcForPricingEngine().getQuoteItems();
        when(qbtdcOrdersPersistenceService.findAllInactiveQuotes(anyString(),anyString(),any(LocalDateTime.class))).thenReturn(quoteItems);
        quoteService.expireUnusedQuotes();

        assertTrue( quoteItems.stream().allMatch(qbtdcQuote -> QuoteStatus.EXPIRED.getValue().equals(qbtdcQuote.getStatus())));
        Mockito.verify(orderManagerService, Mockito.times(1)).expireQuotes(anyInt());
    }

}
