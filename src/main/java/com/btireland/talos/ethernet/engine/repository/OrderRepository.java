package com.btireland.talos.ethernet.engine.repository;


import com.btireland.talos.ethernet.engine.domain.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository<T extends Orders> extends JpaRsqlRepository<T, Long> {
    /**
     * Standard query method. The implementation will be generated by Spring.
     * Have a look here for further info https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories
     *
     * @param orderId order_id of the wag.order
     * @return The {@link Orders} matching orderId or null if not found
     */
    T findByWagOrderId(Long orderId);

    T findByInternalTrackingOrderReference(String reference);

    // fetch a list of  active orders for an OAO (an active order is any order except those which are completed and updated more than 7 days ago)
    @Query("from Pbtdc o where o.oao = :oao and not (((o.lastNotificationType = 'C' or o.lastNotificationType = 'R' or o.lastNotificationType = 'U') and (o.changedAt <= :timeFrame)))")
    List<T> findActiveOrdersByOao(String oao, LocalDateTime timeFrame, Pageable pageable);

    // fetch a list of OAOs with active orders
    @Query("select distinct (o.oao) from Pbtdc o where (o.oao = oao) and not (((o.lastNotificationType = 'C' or o.lastNotificationType = 'R' or o.lastNotificationType = 'U') and (o.changedAt <= :timeFrame)))")
    List<String> findOaosWithActiveOrders(LocalDateTime timeFrame);

    List<T> findByRefQuoteItemIdAndOrderStatusNot(Long quoteItemId,String orderStatus);

    Page<T> findByWagOrderIdIn(List<Long> orderIdList, Pageable pageable);
    
    //Get the count of active pbtdc orders
    @Query("select count(o.id) from Pbtdc o where (o.changedAt <= :timeFrame) and not (((o.lastNotificationType = 'C' or o.lastNotificationType = 'R' or o.lastNotificationType = 'U')))")
    long findActivePbtdcOrders(LocalDateTime timeFrame);

    //Get the count of active qbtdc orders
    @Query("select count(o.id) from Qbtdc o where (o.changedAt <= :timeFrame) and not (((o.lastNotificationType = 'C' or o.lastNotificationType = 'R' or o.lastNotificationType = 'U')))")
    long findActiveQbtdcOrders(LocalDateTime timeFrame);

    //Get the count of inactive pbtdc orders
    @Query("select count(o.id) from Pbtdc o where (o.changedAt <= :timeFrame) and (((o.lastNotificationType = 'C' or o.lastNotificationType = 'R' or o.lastNotificationType = 'U')))")
    long findInActivePbtdcOrders(LocalDateTime timeFrame);

    //Get the count of active qbtdc orders
    @Query("select count(o.id) from Qbtdc o where (o.changedAt <= :timeFrame) and (((o.lastNotificationType = 'C' or o.lastNotificationType = 'R' or o.lastNotificationType = 'U')))")
    long findInActiveQbtdcOrders(LocalDateTime timeFrame);

    /**
     * Find list of orders by order number.
     *
     * @param orderNumber the order number
     * @return the list of orders
     */
    List<T> findByOrderNumber(String orderNumber);

    T findByOrderNumberAndOao(String orderNumber, String oao);
}
