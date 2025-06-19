package com.btireland.talos.ethernet.engine.workflow.pbtdc;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.exception.PBTDCOrderClientException;
import com.btireland.talos.ethernet.engine.exception.UserUnauthorizedException;
import com.btireland.talos.ethernet.engine.exception.ValidationException;
import com.btireland.talos.ethernet.engine.service.OrderValidationService;
import com.btireland.talos.ethernet.engine.service.PBTDCOrderWorkflowService;
import com.btireland.talos.ethernet.engine.service.ParkedNotificationsPersistenceService;
import com.btireland.talos.ethernet.engine.service.PbtdcNotificationProcessorService;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.delegate.*;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.order.OrderStatusUpdateCompletedServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.order.SendRejectNotificationServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.provi.*;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.validation.CheckDuplicateOrderServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.validation.CheckInventoryServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.validation.CheckQuoteServiceTask;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants.*;
import static com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants.OrderValidationProcess.*;
import static com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants.SupplierProvisioningProcess.*;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ProcessEngineExtension.class)
@UnitTest
class PbtdcOrderWorkflowTest {

    @Mock
    private OrderValidationService orderValidationService;

    @Mock
    private PBTDCOrderWorkflowService pbtdcOrderWorkflowService;

    @Mock
    private PbtdcNotificationProcessorService pbtdcNotificationProcessorService;

    @Mock
    private ParkedNotificationsPersistenceService parkedNotificationsPersistenceService;

    private ProcessEngine processEngine;

    @BeforeEach
    public void setUp() {

        BpmnAwareTests.init(processEngine);

        // set up java delegates to use in the process. We mock every spring dependencies.
        // Main process
        Mocks.register("sendRejectNotificationServiceTask", new SendRejectNotificationServiceTask(pbtdcOrderWorkflowService
        ));
        Mocks.register("orderStatusUpdateCompletedServiceTask", new OrderStatusUpdateCompletedServiceTask(pbtdcOrderWorkflowService));

        // order validation
        Mocks.register("checkQuoteServiceTask", new CheckQuoteServiceTask(orderValidationService));
        Mocks.register("checkDuplicateOrderServiceTask", new CheckDuplicateOrderServiceTask(orderValidationService));
        Mocks.register("checkInventoryServiceTask", new CheckInventoryServiceTask());
        Mocks.register("sendUndeliverableNotificationServiceTask", new SendUndeliverableNotificationServiceTask(pbtdcOrderWorkflowService));
        Mocks.register("orderStatusUpdateCompletedServiceTask", new OrderStatusUpdateCompletedServiceTask(pbtdcOrderWorkflowService));
        Mocks.register("processPbtdcUndeliverableNotificationServiceTask", new UndeliverableNotificationProcessorServiceTask(pbtdcNotificationProcessorService));

        //DS Notification
        Mocks.register("processPbtdcCustomerDelayStartNotification", new PbtdcDelayStartNotificationProcessorServiceTask(pbtdcNotificationProcessorService));
        Mocks.register("sendCustomerDelayStartNotificationServiceTask", new SendCustomerDelayStartNotificationServiceTask(pbtdcOrderWorkflowService));

        //DE Notification
        Mocks.register("processPbtdcCustomerDelayEndNotification", new DelayEndNotificationProcessorServiceTask(pbtdcNotificationProcessorService));
        Mocks.register("sendCustomerDelayEndNotificationServiceTask", new SendCustomerDelayEndNotificationServiceTask(pbtdcOrderWorkflowService));

        // Appointment Request
        Mocks.register("processAppointmentRequestNotificationServiceTask", new AppointmentRequestNotificationProcessorServiceTask(pbtdcNotificationProcessorService));
        Mocks.register("sendAppointmentRequestNotificationServiceTask", new SendAppointmentRequestNotificationServiceTask(pbtdcOrderWorkflowService));

        // Status Notification
        Mocks.register("processStatusNotificationServiceTask", new StatusNotificationProcessorServiceTask(pbtdcNotificationProcessorService));
        Mocks.register("sendStatusNotificationServiceTask", new SendStatusNotificationServiceTask(pbtdcOrderWorkflowService));

        // Confirmation Notification
        Mocks.register("processConfirmationNotificationServiceTask", new ConfirmationNotificationProcessorServiceTask(pbtdcNotificationProcessorService));
        Mocks.register("sendConfirmationNotificationServiceTask", new SendConfirmationNotificationServiceTask(pbtdcOrderWorkflowService));

        // Notes Notification
        Mocks.register("processNotesNotificationServiceTask", new NotesNotificationProcessorServiceTask(pbtdcNotificationProcessorService));
        Mocks.register("sendNotesNotificationServiceTask", new SendNotesNotificationServiceTask(pbtdcOrderWorkflowService));

        //Send Order
        Mocks.register("sendOrderServiceTask", new PBTDCOrderSubmissionServiceTask(pbtdcOrderWorkflowService));

        //Accept Notification
        Mocks.register("processAcceptNotificationServiceTask", new AcceptNotificationProcessorServiceTask(pbtdcNotificationProcessorService));
        Mocks.register("sendAcceptNotificationServiceTask", new SendAcceptNotificationServiceTask(pbtdcOrderWorkflowService));

        //CI Notification
        Mocks.register("processPbtdcCircuitIdNotification", new CircuitIDNotificationProcessorServiceTask(pbtdcNotificationProcessorService));
        Mocks.register("sendCircuitIdNotificationServiceTask", new SendCircuitIdNotificationServiceTask(pbtdcOrderWorkflowService));

        //Complete Notification
        Mocks.register("processCompleteNotificationServiceTask", new CompleteNotificationProcessorServiceTask(pbtdcNotificationProcessorService));
        Mocks.register("sendCompleteNotificationServiceTask", new SendCompleteNotificationServiceTask(pbtdcOrderWorkflowService));

        //OMD Notification
        Mocks.register("processOrderManagerDetNotificationServiceTask", new OrderManagerDetailsNotificationProcessorServiceTask(pbtdcNotificationProcessorService));

        //GIU Notification
        Mocks.register("processGlanIdUpdationNotificationServiceTask", new GlanIdUpdationNotificationProcessorServiceTask(pbtdcNotificationProcessorService));

    }


    @AfterEach
    public void teardown() {
        Mocks.reset();
        BpmnAwareTests.reset();
        processEngine.close();
    }


    /**
     * ******************************************************************************************************************************************
     * Order validation tests related
     * ******************************************************************************************************************************************
     */

    ProcessInstance startProcessAtCheckQuoteStep(String orderId) {
        ProcessInstance processInstance = runtimeService()
                .createProcessInstanceByKey(PROC_DEF_KEY)
                .startBeforeActivity(ACT_ID_CHECK_QUOTE)
                .businessKey(orderId)
                .execute();

        assertThat(processInstance).isStarted().isWaitingAt(ACT_ID_CHECK_QUOTE).isNotEnded();
        return processInstance;
    }

    ProcessInstance startProcessAtCheckDuplicateOrderStep(String orderId) {
        ProcessInstance processInstance = runtimeService()
                .createProcessInstanceByKey(PROC_DEF_KEY)
                .startBeforeActivity(ACT_ID_CHECK_ORDER_DUPLICATE)
                .businessKey(orderId)
                .execute();

        assertThat(processInstance).isStarted().isWaitingAt(ACT_ID_CHECK_ORDER_DUPLICATE).isNotEnded();
        return processInstance;
    }


    /**
     * Test the error path in the order validation process, When the quote is invalid.
     * Check that the workflow goes through the reject path
     */
    @Test
    @DisplayName("Quote is invalid")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void orderValidation_quoteInvalid() throws Exception {

        Mockito.doThrow(new ValidationException("quote invalid"))
                .when(orderValidationService).validateAndPersistQuote(123L);

        ProcessInstance processInstance = startProcessAtCheckQuoteStep("123");

        // run the check quote task
        execute(job());

        assertThat(processInstance)
                .hasPassed(ACT_ID_CHECK_QUOTE, BOUNDARY_EVENT_QUOTE_INVALID, END_EVENT_ORDER_INVALID, PROCESS_ORDER_VALIDATION, BOUNDARY_EVENT_ORDER_INVALID)
                .isWaitingAt(ACT_ID_SEND_REJECT_NOTIFICATION);

    }

    /**
     * Test the error path in the order validation process, When the user is not authorized.
     * Check that the workflow goes through the reject path
     */
    @Test
    @DisplayName("Quote is invalid")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void orderValidation_userUnauthorized() throws Exception {

        Mockito.doThrow(new UserUnauthorizedException("user unauthorized"))
                .when(orderValidationService).validateAndPersistQuote(123L);

        ProcessInstance processInstance = startProcessAtCheckQuoteStep("123");

        // run the check quote task
        execute(job());

        assertThat(processInstance)
                .hasPassed(ACT_ID_CHECK_QUOTE, BOUNDARY_EVENT_USER_UNAUTHORIZED, END_EVENT_ORDER_INVALID, PROCESS_ORDER_VALIDATION, BOUNDARY_EVENT_ORDER_INVALID)
                .isWaitingAt(ACT_ID_SEND_REJECT_NOTIFICATION);

    }

    @Test
    @DisplayName("Quote is valid")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void orderValidation_validQuote()  {

        ProcessInstance processInstance = startProcessAtCheckQuoteStep("123");

        // run the check quote task
        execute(job());

        assertThat(processInstance)
                .hasPassed(ACT_ID_CHECK_QUOTE)
                .isWaitingAt(ACT_ID_CHECK_ORDER_DUPLICATE);

    }

    @Test
    @DisplayName("Order is duplicate")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void orderValidation_duplicate()  {

        ProcessInstance processInstance = startProcessAtCheckDuplicateOrderStep("123");

        // run the check quote task
        execute(job());

        assertThat(processInstance)
                .hasPassed(ACT_ID_CHECK_ORDER_DUPLICATE)
                .isWaitingAt(ACT_ID_SEND_POA);

    }

    @Test
    @DisplayName("Received a Undeliverable when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_UndeliverableReceived() {
        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123", 1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        // receives Undeliverable notification
        runtimeService().createMessageCorrelation(MSG_NAME_UNDELIVERABLE).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(PROCESS_SUPPLIER_PROV, BOUNDARY_EVENT_SUPPLIER_ORDER_UNDELIVERABLE)
                .isWaitingAt(ACT_ID_PROCESS_UNDELIVERABLE_NOTIFICATION);
        // run process undeliverable notification
        execute(job());
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_UNDELIVERABLE_NOTIFICATION)
                .isWaitingAt(ACT_ID_SEND_UNDELIVERABLE_NOTIFICATION);
        // run send undeliverable notification
        execute(job());
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_UNDELIVERABLE_NOTIFICATION)
                .isWaitingAt(ACT_ID_ORDER_STATUS_UPDATE_UNDELIVERABLE);

        // run update order status
        execute(job());
        assertThat(processInstance)
                .hasPassed(ACT_ID_ORDER_STATUS_UPDATE_UNDELIVERABLE, END_EVENT_PBTDC_ORDER_UNDELIVERABLE)
                .isEnded();
    }

    @Test
    @DisplayName("Received a Delay Start when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_DelayStartReceived() {
        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123", 1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        // receives DS notification
        runtimeService().createMessageCorrelation(MSG_NAME_DELAY_START).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(START_EVENT_CUSTOMER_DELAY_START)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_CUST_DELAY_START_NOTIFICATION);
        // run process DS notification
        execute(job(ACT_ID_PROCESS_CUST_DELAY_START_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_CUST_DELAY_START_NOTIFICATION)
                .isWaitingAt(ACT_ID_SEND_DELAY_START_NOTIFICATION);
        // run send undeliverable notification
        execute(job(ACT_ID_SEND_DELAY_START_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_DELAY_START_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a Delay End when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_DelayEndReceived() {
        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123", 1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        // receives DE notification
        runtimeService().createMessageCorrelation(MSG_NAME_DELAY_END).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(START_EVENT_CUSTOMER_DELAY_END)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_CUST_DELAY_END_NOTIFICATION);
        // run process DE notification
        execute(job(ACT_ID_PROCESS_CUST_DELAY_END_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_CUST_DELAY_END_NOTIFICATION)
                .isWaitingAt(ACT_ID_SEND_DELAY_END_NOTIFICATION);
        // run send DE notification
        execute(job(ACT_ID_SEND_DELAY_END_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_DELAY_END_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a Appointment Request notification when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_AppointmentRequestReceived() {
        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123", 1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        runtimeService().createMessageCorrelation(MSG_NAME_APPOINTMENT_REQUEST).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(START_EVENT_APPOINTMENT_REQUEST)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_APPOINTMENT_REQUEST_NOTIFICATION);
        // run process appointment request notification
        execute(job(ACT_ID_PROCESS_APPOINTMENT_REQUEST_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_APPOINTMENT_REQUEST_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_SEND_APPOINTMENT_REQUEST_NOTIFICATION);
        // run send appointment request notification
        execute(job(ACT_ID_SEND_APPOINTMENT_REQUEST_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_APPOINTMENT_REQUEST_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a Status notification when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_StatusNotificationReceived() {

        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123",1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        // receives Undeliverable notification
        runtimeService().createMessageCorrelation(MSG_NAME_STATUS).processInstanceBusinessKey("123").setVariable(VAR_NAME_INTERNAL_NOTIFICATION_TYPE,false).correlate();
        assertThat(processInstance)
                .hasPassed(START_EVENT_STATUS)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_STATUS_NOTIFICATION);
        // run process appointment request notification
        execute(job(ACT_ID_PROCESS_STATUS_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_STATUS_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_SEND_STATUS_NOTIFICATION);
        // run send appointment request notification
        execute(job(ACT_ID_SEND_STATUS_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_STATUS_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a Internal Status notification when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_InternalStatusNotificationReceived() {

        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123",1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));
        // receives Undeliverable notification
        runtimeService().createMessageCorrelation(MSG_NAME_STATUS).processInstanceBusinessKey("123").setVariable(VAR_NAME_INTERNAL_NOTIFICATION_TYPE,true).correlate();
        assertThat(processInstance)
                .hasPassed(START_EVENT_STATUS)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_STATUS_NOTIFICATION);
        // run process appointment request notification
        execute(job(ACT_ID_PROCESS_STATUS_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_STATUS_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    ProcessInstance startSupplierProvisioningProcessAtSendOrderStep(String orderId, Long notificationId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(PARKED_NOTIFICATION_ID, notificationId);
        ProcessInstance processInstance = runtimeService()
                .createProcessInstanceByKey(PROC_DEF_KEY)
                .startBeforeActivity(ACT_ID_SEND_ORDER)
                .businessKey(orderId)
                .setVariables(variables)
                .execute();

        assertThat(processInstance).isStarted().isWaitingAt(ACT_ID_SEND_ORDER).isNotEnded();
        return processInstance;
    }

    ProcessInstance startSupplierProvisioningProcessAtSendAcceptNotificationStep(String orderId, Long notificationId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(PARKED_NOTIFICATION_ID, notificationId);
        ProcessInstance processInstance = runtimeService()
                .createProcessInstanceByKey(PROC_DEF_KEY)
                .startBeforeActivity(ACT_ID_SEND_ACCEPT_NOTIFICATION)
                .businessKey(orderId)
                .setVariables(variables)
                .execute();

        assertThat(processInstance).isStarted().isWaitingAt(ACT_ID_SEND_ACCEPT_NOTIFICATION).isNotEnded();
        return processInstance;
    }

    @Test
    @DisplayName("Received a Confirmation notification when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_ConfirmationReceived() {
        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123",1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        runtimeService().createMessageCorrelation(MSG_NAME_Confirmation).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(START_EVENT_CONFIRMATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_CONFIRMATION_NOTIFICATION);
        // run process appointment request notification
        execute(job(ACT_ID_PROCESS_CONFIRMATION_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_CONFIRMATION_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_SEND_CONFIRMATION_NOTIFICATION);
        // run send appointment request notification
        execute(job(ACT_ID_SEND_CONFIRMATION_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_CONFIRMATION_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a Notes notification when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_NotesNotificationReceived() throws PBTDCOrderClientException {

        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123",1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        Map<String, Boolean> resultMap = new HashMap<>();
        resultMap.put(PbtdcOrderProcessConstants.VAR_NAME_DELIVERY_ON_TRACK_CHANGED, false);
        resultMap.put(PbtdcOrderProcessConstants.VAR_NAME_NOTES_TEXT_EMPTY, false);
        // receives Notes notification
        runtimeService().createMessageCorrelation(MSG_NAME_NOTES).processInstanceBusinessKey("123").setVariable(VAR_NAME_DELIVERY_ON_TRACK_CHANGED,false).correlate();
        Mockito.when(pbtdcNotificationProcessorService.processNotesNotification(123L, 1L)).thenReturn(resultMap);
        assertThat(processInstance)
                .hasPassed(START_EVENT_NOTES)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_NOTES_NOTIFICATION);
        // run process notes notification
        execute(job(ACT_ID_PROCESS_NOTES_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_NOTES_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_SEND_NOTES_NOTIFICATION);
        // run send notes notification
        execute(job(ACT_ID_SEND_NOTES_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_NOTES_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a Notes notification with DeliveryOnTrack Changed when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_NotesNotificationWithDeliveryOnTrackChangedReceived() throws PBTDCOrderClientException {

        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123", 1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        Map<String, Boolean> resultMap = new HashMap<>();
        resultMap.put(PbtdcOrderProcessConstants.VAR_NAME_DELIVERY_ON_TRACK_CHANGED, true);
        resultMap.put(PbtdcOrderProcessConstants.VAR_NAME_NOTES_TEXT_EMPTY, false);
        // receives notes notification
        runtimeService().createMessageCorrelation(MSG_NAME_NOTES).processInstanceBusinessKey("123").setVariable(VAR_NAME_DELIVERY_ON_TRACK_CHANGED,true).correlate();
        Mockito.when(pbtdcNotificationProcessorService.processNotesNotification(123L, 1L)).thenReturn(resultMap);
        assertThat(processInstance)
                .hasPassed(START_EVENT_NOTES)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_NOTES_NOTIFICATION);
        // run process notes notification
        execute(job(ACT_ID_PROCESS_NOTES_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_NOTES_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_SEND_NOTES_NOTIFICATION);
        // run send notes notification
        execute(job(ACT_ID_SEND_NOTES_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_NOTES_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_SEND_CF_NOTIFICATION_AFTER_NOTES_NOTIFICATION);

        // run send cf notification
        execute(job(ACT_ID_SEND_CF_NOTIFICATION_AFTER_NOTES_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_CF_NOTIFICATION_AFTER_NOTES_NOTIFICATION, END_EVENT_NOTES)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a Notes notification with Empty Text and DeliveryOnTrack as False when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_NotesNotificationWithEmptyTextAndDeliveryOnTrackFalseReceived() throws PBTDCOrderClientException {

        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123", 1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        Map<String, Boolean> resultMap = new HashMap<>();
        resultMap.put(PbtdcOrderProcessConstants.VAR_NAME_DELIVERY_ON_TRACK_CHANGED, false);
        resultMap.put(PbtdcOrderProcessConstants.VAR_NAME_NOTES_TEXT_EMPTY, true);
        // receives Notes notification
        runtimeService().createMessageCorrelation(MSG_NAME_NOTES).processInstanceBusinessKey("123").setVariable(VAR_NAME_DELIVERY_ON_TRACK_CHANGED,false).correlate();
        Mockito.when(pbtdcNotificationProcessorService.processNotesNotification(123L, 1L)).thenReturn(resultMap);
        assertThat(processInstance)
                .hasPassed(START_EVENT_NOTES)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_NOTES_NOTIFICATION);
        // run process notes notification
        execute(job(ACT_ID_PROCESS_NOTES_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_NOTES_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a Notes notification with Empty Text and DeliveryOnTrack Changed when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_NotesNotificationWithEmptyTextAndDeliveryOnTrackChangedReceived() throws PBTDCOrderClientException {

        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123", 1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        Map<String, Boolean> resultMap = new HashMap<>();
        resultMap.put(PbtdcOrderProcessConstants.VAR_NAME_DELIVERY_ON_TRACK_CHANGED, true);
        resultMap.put(PbtdcOrderProcessConstants.VAR_NAME_NOTES_TEXT_EMPTY, true);
        // receives notes notification
        runtimeService().createMessageCorrelation(MSG_NAME_NOTES).processInstanceBusinessKey("123").setVariable(VAR_NAME_DELIVERY_ON_TRACK_CHANGED,true).correlate();
        Mockito.when(pbtdcNotificationProcessorService.processNotesNotification(123L, 1L)).thenReturn(resultMap);
        assertThat(processInstance)
                .hasPassed(START_EVENT_NOTES)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_NOTES_NOTIFICATION);
        // run process notes notification
        execute(job(ACT_ID_PROCESS_NOTES_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_NOTES_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_SEND_CF_NOTIFICATION_AFTER_NOTES_NOTIFICATION);

        // run send cf notification
        execute(job(ACT_ID_SEND_CF_NOTIFICATION_AFTER_NOTES_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_CF_NOTIFICATION_AFTER_NOTES_NOTIFICATION, END_EVENT_NOTES)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a Accept notification when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_AcceptNotificationReceived() {

        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendOrderStep("123",1L);

        execute(job(ACT_ID_SEND_ORDER));

        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_ORDER)
                .isWaitingAt(ACT_ID_SUPPLIER_ACCEPT_PROCESS, GATEWAY_SUPPLIER_PROVISIONING_ACCEPT_OR_REJECT);

        // receives Notes notification
        runtimeService().createMessageCorrelation(MSG_NAME_ACCEPT).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(GATEWAY_SUPPLIER_PROVISIONING_ACCEPT_OR_REJECT)
                .isWaitingAt(ACT_ID_SUPPLIER_ACCEPT_PROCESS, ACT_ID_PROCESS_ACCEPT_NOTIFICATION);
        // run process notes notification
        execute(job(ACT_ID_PROCESS_ACCEPT_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_ACCEPT_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_ACCEPT_PROCESS, ACT_ID_SEND_ACCEPT_NOTIFICATION);
        // run send notes notification
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_ACCEPT_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a CI when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_CircuitIdReceived() {
        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123", 1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));
        // receives CI notification
        runtimeService().createMessageCorrelation(MSG_NAME_CIRCUIT_ID).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(START_EVENT_CIRCUIT_ID)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_CIRCUIT_ID_NOTIFICATION);
        // run process CI notification
        execute(job(ACT_ID_PROCESS_CIRCUIT_ID_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_CIRCUIT_ID_NOTIFICATION)
                .isWaitingAt(ACT_ID_SEND_CIRCUIT_ID_NOTIFICATION);
        // run send CI notification
        execute(job(ACT_ID_SEND_CIRCUIT_ID_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_CIRCUIT_ID_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a Complete notification when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_CompleteNotificationReceived() {

        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendOrderStep("123",1L);

        execute(job(ACT_ID_SEND_ORDER));

        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_ORDER)
                .isWaitingAt(ACT_ID_SUPPLIER_ACCEPT_PROCESS, GATEWAY_SUPPLIER_PROVISIONING_ACCEPT_OR_REJECT);

        // receives Notes notification
        runtimeService().createMessageCorrelation(MSG_NAME_ACCEPT).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(GATEWAY_SUPPLIER_PROVISIONING_ACCEPT_OR_REJECT)
                .isWaitingAt(ACT_ID_SUPPLIER_ACCEPT_PROCESS, ACT_ID_PROCESS_ACCEPT_NOTIFICATION);
        // run process notes notification
        execute(job(ACT_ID_PROCESS_ACCEPT_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_ACCEPT_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_ACCEPT_PROCESS, ACT_ID_SEND_ACCEPT_NOTIFICATION);
        // run send notes notification
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_ACCEPT_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);

        // receives Complete notification
        runtimeService().createMessageCorrelation(MSG_NAME_COMPLETED).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_ACCEPT_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_COMPLETE_NOTIFICATION);
        // run process complete notification
        execute(job(ACT_ID_PROCESS_COMPLETE_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_COMPLETE_NOTIFICATION)
                .isWaitingAt(ACT_ID_SEND_COMPLETE_NOTIFICATION);
        // run send complete notification
        execute(job(ACT_ID_SEND_COMPLETE_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_COMPLETE_NOTIFICATION)
                .isWaitingAt(ACT_ID_ORDER_STATUS_UPDATE_COMPLETED);

    }

    @Test
    @DisplayName("Received a OMD notification when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_OMDNotificationReceived() {

        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123", 1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        // receives OMD notification
        runtimeService().createMessageCorrelation(MSG_ORDER_MANAGER_DETAILS).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(START_EVENT_ORDER_MANAGER_DETAILS)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_ORDER_MANAGER_DET_NOTIFICATION);
        // run process OMD notification
        execute(job(ACT_ID_PROCESS_ORDER_MANAGER_DET_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_ORDER_MANAGER_DET_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_SEND_OMD_CONFIRMATION_NOTIFICATION);
        // run send confirmation notification
        execute(job(ACT_ID_SEND_OMD_CONFIRMATION_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_OMD_CONFIRMATION_NOTIFICATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);
    }

    @Test
    @DisplayName("Received a GUI notification when in Supplier provisioning process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_GUINotificationReceived() {

        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123", 1L);
        execute(job(ACT_ID_SEND_ACCEPT_NOTIFICATION));

        // receives GUI notification
        runtimeService().createMessageCorrelation(MSG_NAME_GLAN_ID_UPDATION).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(START_EVENT_GLAN_ID_UPDATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS, ACT_ID_PROCESS_GLAN_ID_UPDATION_NOTIFICATION);
        // run process GUI notification
        execute(job(ACT_ID_PROCESS_GLAN_ID_UPDATION_NOTIFICATION));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_GLAN_ID_UPDATION_NOTIFICATION,END_EVENT_GLAN_ID_UPDATION)
                .isWaitingAt(ACT_ID_SUPPLIER_PROVISIONING_PROCESS);

    }

    @Test
    @DisplayName("Received a GUI notification before Accept when in Supplier Accept process")
    @Deployment(resources = "bpmn/Pbtdc.bpmn")
    public void supplierProvisioning_GUINotificationReceivedBeforeAccept() {

        ProcessInstance processInstance = startSupplierProvisioningProcessAtSendAcceptNotificationStep("123", 1L);

        // receives GUI notification
        runtimeService().createMessageCorrelation(MSG_NAME_GLAN_ID_UPDATION).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(START_EVENT_GLAN_ID_UPDATION_BEFORE_ACCEPT)
                .isWaitingAt(ACT_ID_SUPPLIER_ACCEPT_PROCESS, ACT_ID_PROCESS_GLAN_ID_UPDATION_NOTIFICATION_BEFORE_ACCEPT);
        // run process GUI notification
        execute(job(ACT_ID_PROCESS_GLAN_ID_UPDATION_NOTIFICATION_BEFORE_ACCEPT));
        assertThat(processInstance)
                .hasPassed(ACT_ID_PROCESS_GLAN_ID_UPDATION_NOTIFICATION_BEFORE_ACCEPT,END_EVENT_GLAN_ID_UPDATION_BEFORE_ACCEPT)
                .isWaitingAt(ACT_ID_SUPPLIER_ACCEPT_PROCESS);

    }


}
