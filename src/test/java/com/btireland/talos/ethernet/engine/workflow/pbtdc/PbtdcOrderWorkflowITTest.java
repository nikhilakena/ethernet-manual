package com.btireland.talos.ethernet.engine.workflow.pbtdc;


import com.btireland.talos.core.common.message.test.MessageUtils;
import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.OrdersDTO;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants.PARKED_NOTIFICATION_ID;
import static com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants.PROC_DEF_KEY;
import static com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants.SupplierProvisioningProcess.ACT_ID_SEND_ORDER;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;

@SpringBootTest
@ActiveProfiles("test")
@IntegrationTest
@Import({DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
@Slf4j
class PbtdcOrderWorkflowITTest {

    private static final String TEST_DATA_DIR = "/data/PbtdcOrderWorkflowITTest/";
    private static final WireMockServer wireMock = new WireMockServer(9980);

    @Autowired
    public EmbeddedActiveMQResource mqServer;

    @Autowired
    private ProcessEngine processEngine;

    @Value("${application.queue.cerberus.data-sync}")
    private String cerberusDataSyncQueue;

    @BeforeAll
    private static void setup(){
        wireMock.start();
    }

    @AfterAll
    private static void tearDown(){
        wireMock.stop();
    }

    @BeforeEach
    private void setUp() {
        BpmnAwareTests.init(processEngine);
        wireMock.resetAll();
    }

    @AfterEach
    private void teardown() {
        BpmnAwareTests.reset();
    }

    @Test
    @DataSet(cleanBefore = true,
            value = TEST_DATA_DIR + "PbtdcOrder-ds.yml", skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY",
            "ACT_GE_SCHEMA_LOG",
            "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
            , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history",
    })
    @DisplayName("Http Requests are not retried for 4xx exceptions")
    void checkNoRetries_whenClientExceptionIsThrown() throws IOException {

        //Return 4xx status code
        wireMock.stubFor(get(urlMatching("/api/v1/wag/agent/([0-9]*)"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.BAD_REQUEST.value())));


        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendOrderStep("123", 1L);

        BpmnAwareTests.assertThat(processInstance).isStarted()
                      .isWaitingAt(ACT_ID_SEND_ORDER);

        // Give time to Camunda async job to run
        Awaitility.await().atMost(30, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> {
                    return BpmnAwareTests.job(processInstance).getRetries() == 0;
                }
        );

        //Verify there are no retries when http status code is 4xx
        wireMock.verify(1, getRequestedFor(urlEqualTo("/api/v1/wag/agent/123")));


    }

    @Test
    @DataSet(cleanBefore = true,
            value = TEST_DATA_DIR + "PbtdcOrder-ds.yml", skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY",
            "ACT_GE_SCHEMA_LOG",
            "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
            , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history",
    })
    @DisplayName("Http Requests are retried three times for any 5xx HTTP response")
    void checkRetriesHappen_whenServerExceptionIsThrown() {

        //Return 5xx status code
        wireMock.stubFor(get(urlMatching("/api/v1/wag/agent/([0-9]*)"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.SERVICE_UNAVAILABLE.value())));


        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendOrderStep("123", 1L);

        BpmnAwareTests.assertThat(processInstance).isStarted()
                      .isWaitingAt(ACT_ID_SEND_ORDER).isNotEnded();

        // Give time to Camunda async job to run
        Awaitility.await().atMost(30, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> {
                    return BpmnAwareTests.job(processInstance).getRetries() == 0;
                }
        );

        //Verify http request is retried 3 times(default camunda behaviour) when http status code is 5xx
        wireMock.verify(3, getRequestedFor(urlEqualTo("/api/v1/wag/agent/123")));


    }

    @Test
    @DisplayName("Intervention is created when no agent details are found")
    @DataSet(cleanBefore = true,
            value = TEST_DATA_DIR + "PbtdcOrder-ds.yml", skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY",
            "ACT_GE_SCHEMA_LOG",
            "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
            , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history",
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "PbtdcOrder-intervention-result-ds.yml",
            ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP","CREATE_TIMESTAMP"})
    void checkInterventionIsCreated_whenExceptionThrownWhileFetchingAgentDetails() throws IOException {
        MessageUtils messageUtils = new MessageUtils(mqServer,
                List.of(), List.of(cerberusDataSyncQueue),
                Map.of(OrdersDTO.class, OrdersDTO.class.getSimpleName()));

        // stub 404 response
        wireMock.stubFor(get(urlMatching("/api/v1/wag/agent/([0-9]*)"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.BAD_REQUEST.value())));

        wireMock.stubFor(post(urlPathEqualTo("/api/v1/pbtdc-order"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("")));
        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendOrderStep("123", 1L);

        BpmnAwareTests.assertThat(processInstance).isStarted()
                      .isWaitingAt(ACT_ID_SEND_ORDER);

        // Give time to Camunda async job to run
        Awaitility.await().atMost(30, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> {
                    return BpmnAwareTests.job(processInstance).getRetries() == 0;
                }
        );
        com.btireland.talos.ethernet.engine.dto.OrdersDTO orderDTO =
                messageUtils.getFirstMessageFromQueue(cerberusDataSyncQueue, com.btireland.talos.ethernet.engine.dto.OrdersDTO.class);
        Assertions.assertThat(orderDTO.getInterventionDetails()).isNotNull();


    }


    private ProcessInstance startSupplierProvisioningProcessAtSendOrderStep(String orderId, Long notificationId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(PARKED_NOTIFICATION_ID, notificationId);
        ProcessInstance processInstance = runtimeService()
                .createProcessInstanceByKey(PROC_DEF_KEY)
                .startBeforeActivity(ACT_ID_SEND_ORDER)
                .businessKey(orderId)
                .setVariables(variables)
                .execute();

        return processInstance;
    }
}
