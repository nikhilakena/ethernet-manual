package com.btireland.talos.ethernet.engine.bdd.pbtdc;

import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.dsl.RiderDSL;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.tngtech.keycloakmock.api.KeycloakMock;
import com.tngtech.keycloakmock.api.ServerConfig;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class CucumberHooks {
    private static EmbeddedActiveMQResource mqServer = new EmbeddedActiveMQResource("broker.xml");

    private static GreenMail mailServer = new GreenMail(ServerSetupTest.SMTP);

    @Autowired
    @Qualifier("databaseRiderDatasource")
    private DataSource ds;

    @LocalServerPort
    private int port;

    @Autowired
    private ProcessEngine processEngine;

    private static final WireMockServer wireMock = new WireMockServer(9980);

    private static final KeycloakMock keycloakMock = new KeycloakMock(
            ServerConfig.aServerConfig()
                    .withPort(8000)
                    .withDefaultRealm("talos")
                    .build());

    @BeforeAll
    public static void setUp() throws IOException {
        mqServer.start();
        wireMock.start();
        WireMock.configureFor("localhost", wireMock.getOptions().portNumber());
        keycloakMock.start();
        mailServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMock.stop();
        keycloakMock.stop();
        mailServer.stop();
        mqServer.stop();
    }

    @Before
    public void setupPerTest() throws SQLException, IOException {
        PBTDCStepDefs.setUp(wireMock, keycloakMock, mqServer);
        RiderDSL.withConnection(ds.getConnection())
                .withDataSetConfig(new DataSetConfig().executeScripsBefore("/data/CucumberTest/reset_sequences.sql").cleanBefore(true).skipCleaningFor("ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
                        "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
                        "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
                        "flyway_schema_history"))
                .createDataSet();
        wireMock.resetAll();
        BpmnAwareTests.init(processEngine);
        RestAssured.port = port;
    }
    @After
    public void resetBpmnAssert() {
        BpmnAwareTests.reset();
    }
}
