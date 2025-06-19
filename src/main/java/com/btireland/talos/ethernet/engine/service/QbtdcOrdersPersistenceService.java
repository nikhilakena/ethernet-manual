package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.domain.Qbtdc;
import com.btireland.talos.ethernet.engine.domain.QbtdcQuote;
import com.btireland.talos.ethernet.engine.repository.OrderRepository;
import com.btireland.talos.ethernet.engine.repository.QbtdcQuoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class QbtdcOrdersPersistenceService {
    private final OrderRepository<Qbtdc> qbtdcOrderRepository;

    private final QbtdcQuoteRepository qbtdcQuoteRepository;

    public QbtdcOrdersPersistenceService(OrderRepository<Qbtdc> orderRepository,
                                         QbtdcQuoteRepository qbtdcQuoteRepository) {
        this.qbtdcOrderRepository = orderRepository;
        this.qbtdcQuoteRepository = qbtdcQuoteRepository;
    }


    public Qbtdc findById(Long orderId) {
        return qbtdcOrderRepository.findById(orderId).orElse(null);
    }


    public Qbtdc findByOrderId(Long orderId) {
        return qbtdcOrderRepository.findByWagOrderId(orderId) != null ? qbtdcOrderRepository.findByWagOrderId(orderId) : null;
    }

    public Qbtdc update(Qbtdc order) {
        return qbtdcOrderRepository.save(order);
    }

    public List<QbtdcQuote> findAllInactiveQuotes(String quoteItemStatus, String orderStatus, LocalDateTime maxDate) {
        return qbtdcQuoteRepository.findByStatusAndQbtdcOrderStatusAndChangedAtBefore(quoteItemStatus,orderStatus,maxDate);
    }

    public List<QbtdcQuote> save(List<QbtdcQuote> quoteItemList){
        return qbtdcQuoteRepository.saveAll(quoteItemList);
    }

    public Qbtdc findByOrderNumberAndOao(String orderNumber, String oao){
        return qbtdcOrderRepository.findByOrderNumberAndOao(orderNumber, oao);
    }
}
