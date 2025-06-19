package com.btireland.talos.ethernet.engine.bdd.pbtdc;


import com.btireland.talos.core.common.message.test.MessageUtils;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.OrdersDTO;
import com.btireland.talos.ethernet.engine.dto.CloseInterventionDTO;
import com.btireland.talos.ethernet.engine.dto.InterventionDetailsDTO;
import com.btireland.talos.ethernet.engine.dto.NotificationDTO;
import com.btireland.talos.ethernet.engine.util.NotificationFactory;
import com.btireland.talos.ethernet.engine.util.PBTDCOrderDTOFactory;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.tngtech.keycloakmock.api.KeycloakMock;
import com.tngtech.keycloakmock.api.TokenConfig;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource;
import org.apache.commons.io.IOUtils;
import org.awaitility.Awaitility;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;

public class PBTDCStepDefs {

    private static final String BASE_URL = "/ws";
    private static final String REPORT_API_BASE_URL = "/api/v1/dc/report/kci";
    private static final String SOAP_ACTION_HEADER = "SOAPAction";
    private static final String SOAP_ACTION_ACCEPT = "http://wag.btireland.ie/WAG_WS/";
    private static final String XML_CONTENT_TYPE = "text/xml";
    private static WireMockServer wiremock;
    private static KeycloakMock keyCloakMock;
    private static MessageUtils messageUtils;
    private static EmbeddedActiveMQResource mqServer;

    private static final String notificationTopic = "test.supplier.notif.topic.v1";
    private static final String notificationQueue = "test.supplier.notif.topic.v1::test.ethernet-engine.supplier.notif.topic.v1";
    private String pbtdcOrderXml = "";

    private String expectedInternalReportOutput = "";
    private String expectedExternalReportOutput = "";
    private String returnedReportOutput = "";
    private ProcessInstance pbtdcProcess;
    private Long interventionId;

    private InterventionDetailsDTO interventionDetailsDTO;

    public static void setUp(WireMockServer wireMock, KeycloakMock keyCloakMockExt, EmbeddedActiveMQResource server) throws IOException {
        wiremock = wireMock;
        keyCloakMock = keyCloakMockExt;
        mqServer= server;
        messageUtils = new MessageUtils(server, List.of("test.pfib.order.queue", notificationQueue), List.of(notificationTopic), Map.of(OrdersDTO.class, "QuoteOrder", NotificationDTO.class,"Notifications"));
    }

    @Given("^PFIB Order is sent by RSP$")
    public void PFIB_Order_Sent_By_Rsp() throws IOException {
        pbtdcOrderXml = IOUtils.toString(new ClassPathResource("/data/CucumberTest/PBTDCOrder.xml", this.getClass()).getInputStream(), StandardCharsets.UTF_8);
    }

    @And("^Order Manager is called to persist data$")
    public void setupOrderManagerStub() throws Throwable {
        wiremock.stubFor(post(urlPathEqualTo("/api/v1/wag/pbtdc-orders"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(PBTDCOrderDTOFactory.savedPBTDCResponse()))));
    }

    @And("^Notification is send to Notcom")
    public void setupNotcomNotificationStub() throws Throwable {
        wiremock.stubFor(post(urlPathEqualTo("/api/v1/notification"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(NotificationFactory.asJson(NotificationFactory.WSANotificationResponse()))));
    }

    @And("^Quote Data is fetched from Order Manager$")
    public void setupOrderManagerStubForQuote() throws Throwable {
        wiremock.stubFor(get(urlPathEqualTo("/api/v1/wag/quote/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(QBTDCOrderDTOFactory.asJson(QBTDCOrderDTOFactory.defaultQuoteDetailsDTO()))));
    }

    @And("^Quote Data is updated to Order Manager$")
    public void setupOrderManagerStubForUpdateQuote() throws Throwable {
        wiremock.stubFor(put(urlPathEqualTo("/api/v1/wag/quote/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(QBTDCOrderDTOFactory.asJson(QBTDCOrderDTOFactory.defaultQuoteDTO()))));
    }

    @And("^Quote Data fails to update to Order Manager$")
    public void setupOrderManagerStubForFailUpdateQuote() {
        String response = new JSONObject()
                .put("error", "Bad Request")
                .put("message", "Bad Request")
                .put("path", "/api/v1/wag/quote/1")
                .put("status", 400)
                .put("timestamp", LocalTime.now().toString())
                .toString();

        wiremock.stubFor(put(urlPathEqualTo("/api/v1/wag/quote/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.BAD_REQUEST_400)
                        .withBody(response)
                ));
    }

    @And("^Glan Ids are updated to Order Manager$")
    public void setupOrderManagerStubForUpdatePBTDCGlanId() throws Throwable {
        wiremock.stubFor(put(urlPathEqualTo("/api/v1/wag/pbtdc-orders/1/glanid"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(PBTDCOrderDTOFactory.asJson(PBTDCOrderDTOFactory.defaultPBTDCOrderDTO()))));
    }

    @And("^Glan Ids Fails to update on Order Manager$")
    public void setupOrderManagerStubForUpdatePBTDCGlanIdFailed() throws Throwable {
        String response = new JSONObject()
                .put("error", "Bad Request")
                .put("message", "Bad Request")
                .put("path", "/api/v1/wag/pbtdc-orders/1/glanid")
                .put("status", 400)
                .put("timestamp", LocalTime.now().toString())
                .toString();

        wiremock.stubFor(put(urlPathEqualTo("/api/v1/wag/pbtdc-orders/1/glanid"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(response)
                        .withStatus(HttpStatus.BAD_REQUEST_400)));
    }

    @And("^Order Manager is called to get agent details$")
    public void setupOrderManagerAgentStub() throws Throwable {
        wiremock.stubFor(get(urlMatching("/api/v1/wag/agent/([0-9]*)"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\": \"Test Agent\", \"email\": \"test@test.com\"}")));
    }


    @And("^Seal has an API to send PBTDC order")
    public void setupSealAPIStub() throws Throwable {
        wiremock.stubFor(post(urlPathEqualTo("/api/v1/pbtdc-order"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("")));
    }

    @And("^Get Quote API is called with Quote Item Id \"(.*)\"$")
    public void verifyQuoteAPICalled(String quoteItemId) {
        verify(1, getRequestedFor(urlEqualTo("/api/v1/wag/quote/" + quoteItemId)));
    }

    @And("^PBTDC Order is sent to Seal$")
    public void verifySealAPICalled() {
        verify(1, postRequestedFor(urlEqualTo("/api/v1/pbtdc-order")));

    }

    @And("^A notification of type \"(.*)\" with reference \"(.*)\" has been sent to NotCom$")
    public void workflowHasPassedActivity(String notificationType, String reference) {
        verify(1, postRequestedFor(urlEqualTo("/api/v1/notification"))
                .withRequestBody(equalToJson("{\"reference\":\"" + reference + "\"}", true, true))
                .withRequestBody(equalToJson("{\"type\":\"" + notificationType + "\"}", true, true)));
    }

    @When("^PBTDC Order is received by Ethernet Engine")
    public void pbtdcOrderReceived() throws Throwable {
        String token = keyCloakMock.getAccessToken(
                TokenConfig.aTokenConfig()
                        .withClaim("operatorCode", "83758")
                        .withClaim("operatorName", "SKY")
                        .withClaim("operatorOao", "sky")
                        .build());
        RestAssured.given().auth().oauth2(token)
                .contentType(XML_CONTENT_TYPE)
                .header(SOAP_ACTION_HEADER, SOAP_ACTION_ACCEPT)
                .body(pbtdcOrderXml)
                .when()
                .post(BASE_URL).then().assertThat().statusCode(200);
        pbtdcProcess = BpmnAwareTests.processInstanceQuery().processInstanceBusinessKey("1").singleResult();

    }

    @And("^Workflow has passed activity \"(.*)\"$")
    public void workflowHasPassedActivity(String activityId) {
        Awaitility.await().atMost(20, TimeUnit.SECONDS).untilAsserted(() -> assertThat(pbtdcProcess).hasPassed(activityId));
    }

    @And("^Workflow has been suspended$")
    public void workflowIsSuspended() {
        var f = BpmnAwareTests.jobQuery().suspended().singleResult();
        pbtdcProcess = BpmnAwareTests.processInstanceQuery().processInstanceBusinessKey("1").singleResult();
        Awaitility.await().atMost(20, TimeUnit.SECONDS).untilAsserted(() -> assertThat(pbtdcProcess).isSuspended());
    }

    @When("^Get Intervention api is called")
    public void getInterventionAPICalled() throws Throwable {
        List<InterventionDetailsDTO> interventionDetailsDTOList = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/v1/admin/1/interventions?status=OPEN")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath().getList("",InterventionDetailsDTO.class);

        interventionId = interventionDetailsDTOList.get(0).getId();

    }
    @Then("an intervention has been created with id {int}")
    public void getInterventionCreatedDetails(int id) throws Throwable {
        Awaitility.await()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(30, TimeUnit.SECONDS).until(() ->
        {
            var r = RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/api/v1/admin/1/interventions?status=OPEN")
                    .then()
                    .extract().response();

            if(r.statusCode() == HttpStatus.OK_200){
                this.interventionDetailsDTO = r.body().jsonPath().getList("", InterventionDetailsDTO.class).get(0);
                return true;
            }else{
                return false;
            }
        });
    }

    @And("the created intervention has field {string} set to {string}")
    public void checkInterventionFields(String field, String value) throws Exception {
        Assertions.assertNotEquals(null, this.interventionDetailsDTO);
        String checkfield;
        switch (field){
            case "notes":
                checkfield = this.interventionDetailsDTO.getNotes();
                break;
            case "workflow":
                checkfield = this.interventionDetailsDTO.getWorkflow();
                break;
            default:
                throw new Exception("Unexpected field checked");
        }

        Assertions.assertEquals(value, checkfield);
    }


    @When("^Close Intervention api is called")
    public void closeInterventionAPICalled() throws Throwable {
        CloseInterventionDTO closeInterventionDTO=CloseInterventionDTO.builder().agent("sky").closingNotes("matter solved").build();
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(closeInterventionDTO)
                .when()
                .post("/api/v1/admin/interventions/{id}:close", interventionId)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @When("^\"(.*)\" Notification received from Seal with orderReference \"(.*)\" and notification type \"(.*)\"$")
    public void notificationReceivedFromSeal(String notificationType,String orderReference,String siebelNotificationType) throws Throwable {
        NotificationDTO notification = NotificationFactory.siebelNotificationDTO(siebelNotificationType);
        notification.setReference(orderReference);
        messageUtils.sendMessage(notificationQueue, notification);
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(notificationMessageHasBeenConsumed());

        triggerNotificationProcessing();
    }

    @And("Notification processor is triggered")
    public void triggerNotificationProcessing(){
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/batch/notifications/process")
                .then()
                .assertThat()
                .statusCode(204);
    }

    private Callable<Boolean> notificationMessageHasBeenConsumed() {
        return () -> mqServer.getMessageCount(notificationQueue) == 0;
    }
    @Given("Generate API is called with date \"(.*)\"$")
    public void generateApiCalledWithDate(String date){
        RestAssured
                .given()
                .log().all()
                .when()
                .post(REPORT_API_BASE_URL + "/generate?date=" + date)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.ACCEPTED_202);
    }

    @And("the expected JSON internal report file is \"(.*)\"$")
    public void expectedJsonInternalReportFile(String expectedReportName) throws IOException{
        this.expectedInternalReportOutput = IOUtils.toString(
                new ClassPathResource("/data/CucumberTest/" + expectedReportName,
                        this.getClass()).getInputStream(), StandardCharsets.UTF_8);
    }

    @And("the expected JSON external report file is \"(.*)\"$")
    public void expectedJsonExternalReportFile(String expectedReportName) throws IOException{
        this.expectedExternalReportOutput = IOUtils.toString(
                new ClassPathResource("/data/CucumberTest/" + expectedReportName,
                        this.getClass()).getInputStream(), StandardCharsets.UTF_8);
    }
    @And("the JSON report requested with date \"(.*)\" and oao \"(.*)\"$")
    public void jsonReportRequestedWithDateAndOao(String date, String oao){
        String url = REPORT_API_BASE_URL + "/retrieve/" + oao + "?date=" + date + "&internal=true";
        byte[] returnedReport =
                RestAssured.given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .log().all()
                        .when()
                        .get(url)
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK_200)
                        .and()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .extract().body().asByteArray();

        this.returnedReportOutput = new String(returnedReport);
    }
    @Then("the internal report matches the expected internal report")
    public void reportMatchesExpectedInternalReport(){
        JSONAssert.assertEquals(expectedInternalReportOutput,returnedReportOutput, false);
    }
    @And("the internal report matches the expected external report")
    public void reportMatchesExpectedExternalReport(){
        JSONAssert.assertEquals(expectedExternalReportOutput, returnedReportOutput, false);
    }

}