package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.util.PBTDCOrderDTOFactory;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
@Import({DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
class PbtdcNotificationProcessorServiceITTest {

    public static final String TEST_DATA_DIR = "/data/PbtdcNotificationProcessorServiceTest/";

    @Autowired
    PbtdcNotificationProcessorService pbtdcNotificationProcessorService;

    public static WireMockServer wireMock = new WireMockServer(9980);

    @BeforeEach
    public void setUp() {
        wireMock.start();
    }

    @AfterEach
    public void stopWiremock(){
        wireMock.stop();
    }

    @Test
    @DisplayName("Process undeliverable notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessUndeliverableNotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessUndeliverableNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processUndeliverableNotification() throws Exception {
        pbtdcNotificationProcessorService.processUndeliverable(123l, 1l);

    }

    @Test
    @DisplayName("Process DS notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessDSNotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessDSNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processDSNotification() throws Exception {
        pbtdcNotificationProcessorService.processDelayStart(123l, 1l);

    }

    @Test
    @DisplayName("Process DE notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessDENotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessDENotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processDENotification() throws Exception {
        pbtdcNotificationProcessorService.processDelayEnd(123l, 1l);

    }


    @Test
    @DisplayName("Process Appointment Request notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessAppointmentRequestNotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessAppointmentRequestNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processAppointmentRequestNotification() throws Exception {
        pbtdcNotificationProcessorService.processAppointmentRequest(123l, 1l);

    }

    @Test
    @DisplayName("Process Appointment Request notification without site and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessAppointmentRequestNotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessAppointmentRequestNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processAppointmentRequestNotificationWithoutSite() throws Exception {
        pbtdcNotificationProcessorService.processAppointmentRequest(123l, 1l);

    }

    @Test
    @DisplayName("Process status notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessStatusNotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessStatusNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processStatusNotification() throws Exception {
        pbtdcNotificationProcessorService.processStatusNotification(123l, 1l);

    }

    @Test
    @DisplayName("Process status notification with status Plan & Build Analysis and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_PlanAndBuildAnalysis-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_PlanAndBuildAnalysis-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processStatusNotificationForPlanAndBuildAnalysis() throws Exception {
        pbtdcNotificationProcessorService.processStatusNotification(123l, 1l);

    }

    @Test
    @DisplayName("Process status notification with status Integrity Check and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_IntegrityCheck-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_IntegrityCheck-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processStatusNotificationForIntegrityCheck() throws Exception {
        pbtdcNotificationProcessorService.processStatusNotification(123l, 1l);

    }

    @Test
    @DisplayName("Process status notification with status Plan & Build Implement and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_PlanAndBuildImplement-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_PlanAndBuildImplement-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processStatusNotificationForPlanAndBuildImplement() throws Exception {
        pbtdcNotificationProcessorService.processStatusNotification(123l, 1l);

    }

    @Test
    @DisplayName("Process status notification with status LEC Order Creation and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_LECOrderCreation-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_LECOrderCreation-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processStatusNotificationForLECOrderCreation() throws Exception {
        pbtdcNotificationProcessorService.processStatusNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process status notification with status LEC Order Creation-B End and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_LECOrderCreationBEnd-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_LECOrderCreationBEnd-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processStatusNotificationForLECOrderCreationBEnd() throws Exception {
        pbtdcNotificationProcessorService.processStatusNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process status notification with status OLO Order Submission and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_OLOOrderSubmission-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessStatusNotification_OLOOrderSubmission-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processStatusNotificationForOLOOrderSubmission() throws Exception {
        pbtdcNotificationProcessorService.processStatusNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process status notification without circuit details and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessStatusNotificationWithoutCircuitDetails-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessStatusNotificationWithoutCircuitDetails-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processStatusNotificationWithoutCircuitDetails() throws Exception {
        pbtdcNotificationProcessorService.processStatusNotification(123l, 1l);

    }

    @Test
    @DisplayName("Process confirmation notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessConfirmationNotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessConfirmationNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processConfirmationNotification() throws Exception {
        pbtdcNotificationProcessorService.processConfirmationNotification(123l, 1l);

    }

    @Test
    @DisplayName("Process notes notification with delivery on track changed and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessNotesNotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessNotesNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processNotesNotificationWithDeliveryOnTrackChanged() throws Exception {
        Boolean expected = true;
        Map<String, Boolean> resultMap = pbtdcNotificationProcessorService.processNotesNotification(123l, 1l);
        Boolean deliveryOnTrackChanged = resultMap.get(PbtdcOrderProcessConstants.VAR_NAME_DELIVERY_ON_TRACK_CHANGED);
        Assertions.assertThat(deliveryOnTrackChanged).isEqualTo(expected);
    }

    @Test
    @DisplayName("Process notes notification with delivery on track not changed and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessNotesNotificationWithDeliveryOnTrackNotChanged-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessNotesNotificationWithDeliveryOnTrackNotChanged-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processNotesNotificationWithDeliveryOnTrackNotChanged() throws Exception {
        Boolean expected = false;
        Map<String, Boolean> resultMap  = pbtdcNotificationProcessorService.processNotesNotification(123l, 1l);
        Boolean deliveryOnTrackChanged = resultMap.get(PbtdcOrderProcessConstants.VAR_NAME_DELIVERY_ON_TRACK_CHANGED);
        Assertions.assertThat(deliveryOnTrackChanged).isEqualTo(expected);

    }

    @Test
    @DisplayName("Process Accept notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessAcceptNotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessAcceptNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processAcceptNotification() throws Exception {
        pbtdcNotificationProcessorService.processAcceptNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process CI notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessCINotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessCINotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP","CREATED"})
    void processCINotification() throws Exception {
        pbtdcNotificationProcessorService.processCircuitId(123l, 1l);

    }

    @Test
    @DisplayName("Process CI notification for WIC product and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessCINotificationForWIC-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessCINotificationForWIC-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP","CREATED"})
    void processCINotificationForWICProduct() throws Exception {
        pbtdcNotificationProcessorService.processCircuitId(123l, 1l);

    }

    @Test
    @DisplayName("Process Complete notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessCompleteNotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessCompleteNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processCompleteNotification() throws Exception {
        pbtdcNotificationProcessorService.processCompleteNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process Complete notification With L Action flag and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessCompleteNotificationWithLActionFlag-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessCompleteNotificationWithLActionFlag-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processCompleteNotificationWithLActionFlag() throws Exception {
        pbtdcNotificationProcessorService.processCompleteNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process Complete notification without PortSettings and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessCompleteNotificationWithoutPortSettings-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessCompleteNotificationWithoutPortSettings-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processCompleteNotificationWithoutPortSettings() throws Exception {
        pbtdcNotificationProcessorService.processCompleteNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process Complete notification with Site details and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessCompleteNotificationWithSiteContact-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = "/data/PbtdcNotificationProcessorServiceTest/testProcessCompleteNotificationWithSiteContact-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processCompleteNotificationWithSiteContact() throws Exception {
        pbtdcNotificationProcessorService.processCompleteNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process Complete notification without Site contact number and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessCompleteNotificationWithoutSiteContactNumber-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = "/data/PbtdcNotificationProcessorServiceTest/testProcessCompleteNotificationWithoutSiteContactNumber-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processCompleteNotificationWithoutSiteContactNumber() throws Exception {
        pbtdcNotificationProcessorService.processCompleteNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process Complete notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessCompleteNotificationWithoutLogicalAdditionalServices-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessCompleteNotificationWithoutLogicalAdditionalServices-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processCompleteNotificationWithoutLogicalAdditonalServices() throws Exception {
        pbtdcNotificationProcessorService.processCompleteNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process Complete notification without B-End details and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessCompleteNotificationWithoutBEnd-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessCompleteNotificationWithoutBEnd-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processCompleteNotificationWithoutBEnd() throws Exception {
        pbtdcNotificationProcessorService.processCompleteNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process OMD notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessOMDNotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessOMDConfirmationNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processOrderManagerDetailsNotification() throws Exception {
        pbtdcNotificationProcessorService.processOrderManagerDetailsNotification(123l, 1l);
    }


    @Test
    @DisplayName("Process OMD notification without any existing order manager details and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessOMDNotificationWithoutOMD-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessOMDConfirmationNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processOrderManagerDetailsNotification_WithoutExistingOMD() throws Exception {
        pbtdcNotificationProcessorService.processOrderManagerDetailsNotification(123l, 1l);
    }

    @Test
    @DisplayName("Process GIU notification and update Orders object")
    @DataSet(value = TEST_DATA_DIR + "testProcessGIUNotification-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testProcessGIUNotification-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void processGlanIdUpdationNotification() throws Exception {
        wireMock.stubFor(put(urlPathEqualTo("/api/v1/wag/pbtdc-orders/123/glanid"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(PBTDCOrderDTOFactory.asJson(PBTDCOrderDTOFactory.defaultPBTDCOrderDTO()))));

        pbtdcNotificationProcessorService.processGlanIdUpdationNotification(123l, 1l);
    }
}
