package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.rest.exception.ResourceNotFoundException;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrdersPersistenceService {

    private OrderRepository<Orders> orderRepository;

    private static final String NO_ORDER_REFERENCE_FOUND="No order found with Reference";

    public OrdersPersistenceService(OrderRepository<Orders> orderRepository) {
        this.orderRepository = orderRepository;
    }


    public Orders findById(Long orderId){
        return orderRepository.findById(orderId).orElse(null);
    }

    public Orders findByOrderId(Long orderId){
        return orderRepository.findByWagOrderId(orderId) !=null ? orderRepository.findByWagOrderId(orderId) : null;
    }

    public Orders update(Orders order){
        return orderRepository.save(order);
    }

    public void deleteById(Long orderId){
        orderRepository.deleteById(orderId);
    }

    @Transactional(noRollbackFor= ResourceNotFoundException.class)
    public Orders findBySupplierOrderNumberNoRollbackonNotfound(String reference) {
        Orders order = orderRepository.findByInternalTrackingOrderReference(reference);
        if (order == null) {
            throw new ResourceNotFoundException(NO_ORDER_REFERENCE_FOUND+ reference);
        }

        return order;
    }
}
