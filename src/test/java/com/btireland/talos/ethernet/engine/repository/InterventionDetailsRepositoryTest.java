package com.btireland.talos.ethernet.engine.repository;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseConfiguration;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.util.Color;
import com.btireland.talos.ethernet.engine.util.Status;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@DataJpaTest
@DisplayName("InterventionDetailsRepository")
@IntegrationTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatabaseConfiguration.class, DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
public class InterventionDetailsRepositoryTest {

    @Autowired
    private OrderRepository ordersRepository;

    @Autowired
    private InterventionDetailsRepository interventionDetailsRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("Check that we can find an intervention by its id")
    @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    void findByInterventionId(){
        Orders expected = Orders.builder().wagOrderId(1L)
                .orderNumber("BT-PBTDC-1236")
                .oao("sky")
                .dataContract("WAG")
                .originatorCode("EXT")
                .resellerTransactionId("100")
                .resellerOrderRequestDateTime(LocalDateTime.parse("2020-09-08T10:43:35"))
                .createdAt(LocalDateTime.parse("2020-09-08T10:43:35"))
                .serviceType("PBTDC")
                .build();
        ordersRepository.save(expected);
        InterventionDetails interventionDetails= InterventionDetails.builder().id(1L).createdAt(LocalDateTime.parse("2020-09-08T10:43:35")).updatedAt(LocalDateTime.parse("2020-09-08T10:43:35"))
                .orders(expected).build();
        InterventionDetails interventionSetup = interventionDetailsRepository.save(interventionDetails);

        InterventionDetails actual = interventionDetailsRepository.findByInterventionId(interventionSetup.getId());
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getOrders().getWagOrderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Check that we can find an intervention by its id and status")
    @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    void testFindByOrderIdAndStatus(){
        Orders expected = Orders.builder().wagOrderId(1L)
                .orderNumber("BT-PBTDC-1236")
                .oao("sky")
                .dataContract("WAG")
                .originatorCode("EXT")
                .resellerTransactionId("100")
                .resellerOrderRequestDateTime(LocalDateTime.parse("2020-09-08T10:43:35"))
                .createdAt(LocalDateTime.parse("2020-09-08T10:43:35"))
                .serviceType("PBTDC")
                .build();
        ordersRepository.save(expected);
        InterventionDetails interventionDetails= InterventionDetails.builder().color(Color.RED).status(Status.OPEN).createdAt(LocalDateTime.parse("2020-09-08T10:43:35")).updatedAt(LocalDateTime.parse("2020-09-08T10:43:35"))
                .orders(expected).build();
        InterventionDetails interventionDetails2= InterventionDetails.builder().color(Color.CLEAR).status(Status.CLOSED).createdAt(LocalDateTime.parse("2020-09-08T10:43:35")).updatedAt(LocalDateTime.parse("2020-09-08T10:43:35"))
                .orders(expected).build();
        interventionDetailsRepository.save(interventionDetails);
        Assertions.assertThat(interventionDetailsRepository.findByOrderIdAndStatus(1L, Status.OPEN)).contains(interventionDetails);

    }

    @Test
    @DisplayName("Check that we can find open intervention count")
    @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    void testCounyByStatus(){
        Orders order = Orders.builder().wagOrderId(1L)
                .orderNumber("BT-PBTDC-1236")
                .oao("sky")
                .dataContract("WAG")
                .originatorCode("EXT")
                .resellerTransactionId("100")
                .resellerOrderRequestDateTime(LocalDateTime.parse("2020-09-08T10:43:35"))
                .createdAt(LocalDateTime.parse("2020-09-08T10:43:35"))
                .serviceType("PBTDC")
                .build();
        Orders order1 = Orders.builder().wagOrderId(1L)
                .orderNumber("BT-PBTDC-1237")
                .oao("sky")
                .dataContract("WAG")
                .originatorCode("EXT")
                .resellerTransactionId("100")
                .resellerOrderRequestDateTime(LocalDateTime.parse("2020-09-08T10:43:35"))
                .createdAt(LocalDateTime.parse("2020-09-08T10:43:35"))
                .serviceType("PBTDC")
                .build();
        ordersRepository.save(order);
        ordersRepository.save(order1);
        InterventionDetails interventionDetails= InterventionDetails.builder().color(Color.RED).status(Status.OPEN).createdAt(LocalDateTime.parse("2020-09-08T10:43:35")).updatedAt(LocalDateTime.parse("2020-09-08T10:43:35"))
                .orders(order).build();
        InterventionDetails interventionDetails1= InterventionDetails.builder().color(Color.RED).status(Status.OPEN).createdAt(LocalDateTime.parse("2020-09-08T10:43:35")).updatedAt(LocalDateTime.parse("2020-09-08T10:43:35"))
                .orders(order1).build();
        InterventionDetails interventionDetails2= InterventionDetails.builder().color(Color.CLEAR).status(Status.CLOSED).createdAt(LocalDateTime.parse("2020-09-08T10:43:35")).updatedAt(LocalDateTime.parse("2020-09-08T10:43:35"))
                .orders(order).build();
        interventionDetailsRepository.save(interventionDetails);
        interventionDetailsRepository.save(interventionDetails1);
        interventionDetailsRepository.save(interventionDetails2);
        Assertions.assertThat(interventionDetailsRepository.countByStatus(Status.OPEN)).isEqualTo(2L);

    }
}