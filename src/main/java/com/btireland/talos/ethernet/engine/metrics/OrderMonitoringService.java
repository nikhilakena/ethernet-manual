package com.btireland.talos.ethernet.engine.metrics;

import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.repository.InterventionDetailsRepository;
import com.btireland.talos.ethernet.engine.repository.OrderRepository;
import com.btireland.talos.ethernet.engine.repository.ParkedNotificationsRepository;
import com.btireland.talos.ethernet.engine.util.Status;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;
import io.micrometer.core.instrument.MultiGauge.Row;
import io.micrometer.core.instrument.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static io.micrometer.core.instrument.MultiGauge.builder;

@Component
public class OrderMonitoringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMonitoringService.class);
    private final OrderRepository<Orders> orderRepository;
    private final ParkedNotificationsRepository parkedNotificationsRepository;
    private final InterventionDetailsRepository interventionDetailsRepository;

    /**
     * Autowired constructor. All the gauges declared are instantiated here.
     *
     * @param orderRepository               Order repo
     * @param meterRegistry                 Meter registry to register the gauge
     */
    public OrderMonitoringService(final OrderRepository<Orders> orderRepository,
                                  final ParkedNotificationsRepository parkedNotificationsRepository,
                                  final InterventionDetailsRepository interventionDetailsRepository,
                                  final MeterRegistry meterRegistry) {
        this.orderRepository = orderRepository;
        this.parkedNotificationsRepository = parkedNotificationsRepository;
        this.interventionDetailsRepository = interventionDetailsRepository;

        MultiGauge multiGauge = builder("order_metrics")
                .description("List of order metrics")
                .register(meterRegistry);
        multiGauge.register(checkActiveOrdersForMultiGauge(), true);
    }

    /**
     * Method to build the multi gauge.
     *
     * @return Order metrics list
     */
    public List<Row<?>> checkActiveOrdersForMultiGauge() {
        Map<String, Long> map = buildMultiGauge();
        return map.entrySet()
                .stream()
                .map(row -> Row.of(Tags.of("order_metrics_list", row.getKey()), row.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Method to build the gauge map by calling various repo's.
     *
     * @return Map of custom metrics and values
     */
    @Transactional(readOnly = true)
    private Map<String, Long> buildMultiGauge() {
        LocalDateTime timeNow = LocalDateTime.now();
        Map<String, Long> gaugeMap = new ConcurrentHashMap<>();

        gaugeMap.put("qbtdc_active_orders", orderRepository.findActiveQbtdcOrders(timeNow));
        gaugeMap.put("pbtdc_active_orders", orderRepository.findActivePbtdcOrders(timeNow));

        gaugeMap.put("qbtdc_inactive_orders", orderRepository.findInActiveQbtdcOrders(timeNow));
        gaugeMap.put("pbtdc_inactive_orders", orderRepository.findInActivePbtdcOrders(timeNow));

        gaugeMap.put("orders_intervention_count", interventionDetailsRepository.countByStatus(Status.OPEN));
        gaugeMap.put("parked_notification_count", parkedNotificationsRepository.countByActiveParkedNotification());

        return gaugeMap;

    }

}
