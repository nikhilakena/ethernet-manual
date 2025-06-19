package com.btireland.talos.ethernet.engine.metrics;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
public class OrderMonitoringServiceTest {

    @Autowired
    private OrderMonitoringService orderMonitoringService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    public void testMetrics() throws Exception {
        //GIVEN

        //WHEN
        List<MultiGauge.Row<?>> gaugeRows = orderMonitoringService.checkActiveOrdersForMultiGauge();

        //THEN
        assertThat(gaugeRows).hasSize(6);
        assertThat(meterRegistry.get("order_metrics")
                        .gauges()
                        .stream()
                        .map(gauge -> gauge.getId().getTag("order_metrics_list")))
                        .containsExactlyInAnyOrder("qbtdc_active_orders", "pbtdc_active_orders",
                                                          "qbtdc_inactive_orders", "pbtdc_inactive_orders",
                                                          "orders_intervention_count", "parked_notification_count");

    }


}
