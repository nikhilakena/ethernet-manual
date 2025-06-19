package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.repository.InterventionDetailsRepository;
import com.btireland.talos.ethernet.engine.repository.OrderRepository;
import com.btireland.talos.ethernet.engine.util.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PbtdcOrdersPersistenceService {
    private final OrderRepository<Pbtdc> pbtdcOrderRepository;

    private final InterventionDetailsRepository interventionDetailsRepository;

    private ApplicationConfiguration applicationConfiguration;


    public PbtdcOrdersPersistenceService(OrderRepository<Pbtdc> orderRepository,
                                         ApplicationConfiguration applicationConfiguration,
                                         InterventionDetailsRepository interventionDetailsRepository) {
        this.pbtdcOrderRepository = orderRepository;
        this.interventionDetailsRepository = interventionDetailsRepository;
        this.applicationConfiguration = applicationConfiguration;
    }


    public Pbtdc createOrder(Pbtdc PbtdcObj) {
        return pbtdcOrderRepository.save(PbtdcObj);
    }

    public Pbtdc findById(Long orderId) {
        return pbtdcOrderRepository.findById(orderId).orElse(null);
    }


    public Pbtdc findByOrderId(Long orderId) {
        return pbtdcOrderRepository.findByWagOrderId(orderId) != null ? pbtdcOrderRepository.findByWagOrderId(orderId) : null;
    }

    public Pbtdc update(Pbtdc order) {
        return pbtdcOrderRepository.save(order);
    }

    public void deleteById(Long orderId) {
        pbtdcOrderRepository.deleteById(orderId);
    }

    public List<Pbtdc> findActiveOrdersByRefQuoteItemId(Long refQuoteItemId, String orderStatus){
        return pbtdcOrderRepository.findByRefQuoteItemIdAndOrderStatusNot(refQuoteItemId,orderStatus);
    }
    /**
     * Use RSQL framework to parse a String of filters and transform it into a J
     *
     * @param filters
     * @return
     */
    public Page<Pbtdc> findByFilters(String filters, Pageable pageable) {
        return pbtdcOrderRepository.findAllByRsqlQuery(filters, pageable);
    }

    public List<Pbtdc> findActiveOrdersByOao(String oao, LocalDateTime timeFrame){
        return pbtdcOrderRepository.findActiveOrdersByOao(oao, timeFrame, PageRequest.of(0, applicationConfiguration.getReportLimit()));
    }

    public List<String> findOaosWithActiveOrders(LocalDateTime timeFrame){
        return pbtdcOrderRepository.findOaosWithActiveOrders(timeFrame);
    }

    public List<InterventionDetails> getInterventionDetails(Long wagOrderId, Status status){
        List<InterventionDetails> interventionDetailsList=interventionDetailsRepository.findByOrderIdAndStatus(wagOrderId, status);
        return interventionDetailsList;

    }
    public InterventionDetails findByInterventionId(Long id){

        InterventionDetails interventionDetails=interventionDetailsRepository.findByInterventionId(id);
        return interventionDetails;
    }
    public void updateInterventionDetails(InterventionDetails interventionDetails){
        interventionDetailsRepository.save(interventionDetails);
    }

}
