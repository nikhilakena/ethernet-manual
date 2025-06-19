package com.btireland.talos.ethernet.engine.mq;

import com.btireland.talos.core.common.message.test.MessageUtils;
import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.dto.NotificationDTO;
import com.btireland.talos.ethernet.engine.util.NotificationFactory;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
@Import({DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
public class SupplierNotificationConsumerTest {
    @Autowired
    private EmbeddedActiveMQResource mqServer;

    private MessageUtils messageUtils;

    private static final String TEST_DATA_DIR = "/data/SupplierNotificationConsumerTest/";

    @BeforeEach
    public void beforeEach(){
        messageUtils = new MessageUtils(this.mqServer,
                List.of("test.supplier.notif.topic.v1::test.ethernet-engine.supplier.notif.topic.v1"),
                List.of(), Map.of(NotificationDTO.class, "Notifications"));
    }

    @Value("${application.queue.supplier.notif.topic}")
    String notificationQueue;

        /**
     * Testing a consumer involves usually a complete end 2 end tests.
     * When you test a consumer, the only way to know that the message has been processed correctly is by checking the side effects
     * like update in a database or a call to a REST service (you can check it with Wiremock)
     * @throws Exception
     */
    @Test
    @DisplayName("Notification sent to MQ topic is consumed")
    @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    public void sendMessage() throws Exception{

        NotificationDTO notification = NotificationFactory.undeliverableNotificationRequest();
        messageUtils.sendMessage(notificationQueue, notification);

        // TODO check that the message has been received properly by checking the order has been created in DB
        // and workflow has started
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(messageHasBeenConsumed());

    }

    @Test
    @DisplayName("Configured Notification sent to MQ topic is persisted in ParkedNotifications")
    @DataSet(value = TEST_DATA_DIR + "testUndeliverableNotification-ds.yml",cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    @ExpectedDataSet(value = TEST_DATA_DIR + "testUndeliverableNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP","SUPPLIER_NOTIFICATION"})
    public void persistMessage() throws Exception{

        NotificationDTO notification = NotificationFactory.undeliverableNotificationRequest();
        messageUtils.sendMessage(notificationQueue, notification);

        // TODO check that the message has been received properly by checking the order has been created in DB
        // and workflow has started
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(messageHasBeenConsumed());

    }

    @Test
    @DisplayName("Categorization Notification sent to MQ topic is not persisted in ParkedNotifications")
    @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    @ExpectedDataSet(value = TEST_DATA_DIR + "testCategorizationNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP", "supplier_notification"})
    public void ignoreMessage() throws Exception{

        NotificationDTO notification = NotificationFactory.categorizationNotificationRequest();
        messageUtils.sendMessage(notificationQueue, notification);

        // TODO check that the message has been received properly by checking the order has been created in DB
        // and workflow has started
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(messageHasBeenConsumed());

    }

    private Callable<Boolean> messageHasBeenConsumed() {
        return () -> mqServer.getMessageCount(notificationQueue) == 0;
    }
}