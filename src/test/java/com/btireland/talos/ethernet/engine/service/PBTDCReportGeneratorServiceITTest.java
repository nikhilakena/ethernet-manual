package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseConfiguration;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.repository.OrderRepository;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
@Import({DatabaseConfiguration.class, DatabaseRiderConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DBRider(dataSourceBeanName = "databaseRiderDatasource")

public class PBTDCReportGeneratorServiceITTest {
    public static final String TEST_DATA_DIR = "/data/PBTDCReportGeneratorServiceITTest/";

    @Autowired
    PBTDCReportGeneratorService reportGeneratorService;

    @Autowired
    PBTDCReportPersistenceService reportPersistenceService;

    @Autowired
    PbtdcOrdersPersistenceService ordersPersistenceService;

    @Autowired
    JasperReportGeneratorService jasperReportGeneratorService;

    @Autowired
    OrderRepository orderRepository;

    /* check report when a QBTDC and PBTDC order is created. */

    @Test
    @DataSet(value = TEST_DATA_DIR + "PBTDCReportGeneratorServiceITTest-ds.yml",
            cleanBefore = true,
            skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    @DisplayName("Validate report content with a newly created order")
    void testWhenOrderCreated() throws Exception {
        String referenceIds = "NOG Order Number : BT-PBTDC-123\n" +
                "Siebel Account Number : 1234567890\n";

        String expectedJson = new JSONArray()
                .put(new JSONObject()
                        .put("Report Date", "02-01-2020")
                        .put("Order Manager Name", "First Last")
                        .put("Order Manager Email", "test@test.com")
                        .put("Site Name", "")
                        .put("Date Received By Delivery","")
                        .put("Reference IDs", referenceIds)
                        .put("Etherway Type", ConnectionType.ETHERWAY_STANDARD.getPrompt())
                        .put("Product","")
                        .put("Delivery Type","New Delivery")
                        .put("A-End Site Details","")
                        .put("B-End Site Details","")
                        .put("A-End Site Contact","")
                        .put("B-End Site Contact","")
                        .put("Access Speed","")
                        .put("Port Speed","")
                        .put("A-End Presentation","")
                        .put("B-End Presentation","")
                        .put("Notes","")
                        .put("Indicative Delivery Date","")
                        .put("Delivery Date","")
                        .put("Delivery On Track","")
                        .put("Order Entry and Validation Status","")
                        .put("Planning Status","")
                        .put("Access Installation","")
                        .put("Testing CPE Installation","")
                        .put("Service Complete and Operational","")
                ).toString();

        reportGeneratorService.generate( Optional.of(LocalDate.of(2020, 1, 2)));

        // Now retrieve the JSON report

        Optional<byte[]> report = jasperReportGeneratorService.generateJSON(
                LocalDate.of(2020, 01, 02), "sky", true);

        JSONAssert.assertEquals(expectedJson, new String(report.get()), false);
    }

    /* order created, A notification received */

    /* Order created, A and Notes notifications received */

    /* Order created, A and CF notifications received */

    /* Order created, A, CF and C received */

}
