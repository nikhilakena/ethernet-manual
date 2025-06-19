package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
@Import({DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
class PBTDCOrderValidationServiceITTest {

    public static final String TEST_DATA_DIR = "/data/PBTDCOrderValidationServiceITTest/";

    @Autowired
    OrderValidationService pbtdcValidationService;

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
    @DisplayName("Validate and Persist Wag Quote")
    @DataSet(value = TEST_DATA_DIR + "testPbtdcOrder-ds.yml", cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
    })
    @ExpectedDataSet(value = TEST_DATA_DIR + "testPersistedQuote-result-ds.yml", ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void validateAndPersistQuote() throws Exception {
        wireMock.stubFor(get(urlPathEqualTo("/api/v1/wag/quote/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(QBTDCOrderDTOFactory.asJson(QBTDCOrderDTOFactory.defaultQuoteDetailsDTO()))));

        pbtdcValidationService.validateAndPersistQuote(123L);
    }

}
