package com.btireland.talos.ethernet.engine.repository;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseConfiguration;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.domain.Qbtdc;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
@DisplayName("OrderRepositoryTest")
@IntegrationTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatabaseConfiguration.class, DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")

public class OrderRepositoryTest {
    private static final String TEST_DATA_DIR = "/data/OrderRepositoryTest/";
    private static final int orderPageLimit = 200;
    @Autowired
    private OrderRepository<Pbtdc> repository;

    @Autowired
    private OrderRepository<Qbtdc> qbtdcOrderRepository;

    @Value("${application.report.active-time-period}")
    private int activeTimePeriodInDays;
    /*
     * The test dataset includes a set of 4 orders. Three of them are active for the OAOs Vodafone and Sky;
     * the fourth is non-active (completed more than 2 days ago).
     */
    @Test
    @DisplayName("Test that we filter OAOs with active orders correctly")
    @DataSet(cleanBefore = true,value = TEST_DATA_DIR + "findOaosWithOrders-ds.yml")
    void testGetOaos(){
        List<String> oaos = repository.findOaosWithActiveOrders(LocalDateTime.now().minusDays(activeTimePeriodInDays));

        Assertions.assertThat(oaos.size()).isEqualTo(3);
        Assertions.assertThat(oaos).contains("vodafone");
        Assertions.assertThat(oaos).contains("sky");
        Assertions.assertThat(oaos).contains("acme");
    }

    // the Acme OAO has one completed order which is inactive, and one completed order which is active.
    @Test
    @DisplayName("Test that we filter active orders for an OAO correctly when there are no active orders")
    @DataSet(cleanBefore = true,value = TEST_DATA_DIR + "findOaosWithOrders-ds.yml")
    void testGetOrdersWhenOneActive(){
        List<Pbtdc> orders = repository.findActiveOrdersByOao("acme", LocalDateTime.now().minusDays(activeTimePeriodInDays), PageRequest.of(0, orderPageLimit));
        Assertions.assertThat(orders.size()).isEqualTo(1);
    }

    // fetch total number of active orders.
    @Test
    @DisplayName("Test to fetch all active orders")
    @DataSet(cleanBefore = true,value = TEST_DATA_DIR + "findOaosWithOrders-ds.yml")
    void testGetActiveOrders(){
        long count = repository.findActivePbtdcOrders(LocalDateTime.now().minusDays(activeTimePeriodInDays));
        Assertions.assertThat(count).isEqualTo(3);
    }

    // the Sky OAO has two active orders which are not in the completed state.
    @Test
    @DisplayName("Test that we filter active orders for an OAO correctly when there are active orders")
    @DataSet(cleanBefore = true,value = TEST_DATA_DIR + "findOaosWithOrders-ds.yml")
    void testGetOrdersWhenTwoActive(){
        List<Pbtdc> orders = repository.findActiveOrdersByOao("sky", LocalDateTime.now().minusDays(activeTimePeriodInDays), PageRequest.of(0, orderPageLimit));
        Assertions.assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test that qbtdc order is fetched when there are multiple PE rejects")
    @DataSet(cleanBefore = true, value = "/data/OrderRepositoryTest/findQbtdcWithPERejects-ds.yml")
    void testGetQbtdcOrdersWithMultiplePEReject(){
        Qbtdc order = qbtdcOrderRepository.findByWagOrderId(1L);
        Assertions.assertThat(order.getQuoteItems().stream().map(quote->quote.getRejectionDetails()).collect(Collectors.toList())).hasSize(2);
    }

    @Test
    @DisplayName("Test that qbtdc order is fetched using order number and oao")
    @DataSet(cleanBefore = true, value = "/data/OrderRepositoryTest/findOrderUsingOrderNumberAndOao-ds.yml")
    void getOrder_givenOrderNumberAndOao_orderReturned(){
        //GIVEN data set created by DBRider

        //WHEN
        Qbtdc order = qbtdcOrderRepository.findByOrderNumberAndOao("BT-QBTCDC-12346", "sky");

        //THEN
        Assertions.assertThat(order.getOrderNumber()).isEqualTo("BT-QBTCDC-12346");
        Assertions.assertThat(order.getOao()).isEqualTo("sky");
    }
}

