package com.btireland.talos.ethernet.engine.repository;


import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseConfiguration;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.domain.Orders;
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

import java.util.List;

@DataJpaTest
@DisplayName("ParkedNotificationsRepositoryTest")
@IntegrationTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatabaseConfiguration.class, DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
class ParkedNotificationsRepositoryTest {

    public static final String TEST_DATA_DIR = "/data/ParkedNotificationsRepositoryTest/";

    @Autowired
    private ParkedNotificationsRepository repository;


    @Test
    @DisplayName("Check that we return the list of orders which have paused on error flag is null or not 1 in orders and processed status is U")
    @DataSet(value = TEST_DATA_DIR + "ActiveInactiveOrders-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    void testFindAllActiveOrders() {

        List<Orders> actual = repository.findAllActiveOrders();
        Assertions.assertThat(actual.size()).isEqualTo(1);
        Assertions.assertThat(actual.get(0).getWagOrderId()).isEqualTo(102L);
    }

    @Test
    @DisplayName("Check that we return the count of parked notification which have paused on error flag is null or not 1 in orders and processed status is U")
    @DataSet(value = TEST_DATA_DIR + "findAllActiveOrders-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    void testCountParkedNotificationQueue() {

        long actual = repository.countByActiveParkedNotification();
        Assertions.assertThat(actual).isEqualTo(1L);
    }
}
