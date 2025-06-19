package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.util.NotificationFactory;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants.*;
import static com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants.SupplierProvisioningProcess.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
@Import({DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
class ParkedNotificationProcessorITTest {
    private static final String TEST_DATA_DIR = "/data/ParkedNotificationProcessorITTest/";

    @Autowired
    ParkedNotificationProcessor parkedNotificationProcessor;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    private static WireMockServer wireMock = new WireMockServer(9980);

    @BeforeAll
    private static void setup(){
        wireMock.start();
    }

    @AfterAll
    private static void tearDown(){
        wireMock.stop();
    }

    @Test
    @DisplayName("Process undeliverable notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testParkedNotiicationProcessor-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    @ExpectedDataSet(value = TEST_DATA_DIR + "testParkedNotiicationProcessor-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processParkedNotification() throws Exception {
        wireMock.stubFor(post(urlPathEqualTo("/api/v1/pbtdc-order"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("success")));
        wireMock.stubFor(post(urlPathEqualTo("/api/v1/notification"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(NotificationFactory.asJson(NotificationFactory.WSANotificationResponse()))));

        wireMock.stubFor(get(urlMatching("/api/v1/wag/agent/([0-9]*)"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\": \"Test Agent\", \"email\": \"test@test.com\"}")));
        startSupplierProvisioningProcessAtSendOrderStep("123",1L);
        startSupplierProvisioningProcessAtSendOrderStep("124",2L);
        startSupplierProvisioningProcessAtSendOrderStep("125",3L);

        Thread.sleep(5000);
        parkedNotificationProcessor.processNotificationsForActiveOrders();
    }

    ProcessInstance startSupplierProvisioningProcessAtSendOrderStep(String orderId, Long notificationId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(PARKED_NOTIFICATION_ID, notificationId);

        ProcessInstance processInstance = runtimeService
                .createProcessInstanceByKey(PROC_DEF_KEY)
                .startBeforeActivity(ACT_ID_SEND_ORDER)
                .businessKey(orderId)
                .setVariables(variables)
                .execute();

        assertThat(processInstance).isStarted().isWaitingAt(ACT_ID_SEND_ORDER).isNotEnded();
        return processInstance;
    }
}
