package com.btireland.talos.ethernet.engine.workflow.pbtdc;

public final class PbtdcOrderProcessConstants {
    
    private PbtdcOrderProcessConstants() {
    }

    public enum Error {
        QUOTE_INVALID,
        USER_UNAUTHORIZED,
        DUPLICATE_ORDER_FOUND,
        ETHERWAY_NOT_BELONG_TO_OAO,
        ORDER_INVALID
    }

    public static final String PROC_DEF_KEY = "DataCircuitProvisionningOrderProcess";

    public static final String START_EVENT_PFBTDC_ORDER = "StartEvent_PbtdcOrder";
    public static final String START_EVENT_CUSTOMER_DELAY_START = "StartEvent_CustomerDelayStart";
    public static final String START_EVENT_CUSTOMER_DELAY_END = "StartEvent_CustomerDelayEnd";
    public static final String START_EVENT_CIRCUIT_ID = "StartEvent_CircuitId";
    public static final String START_EVENT_APPOINTMENT_REQUEST = "StartEvent_AppointmentRequest";
    public static final String START_EVENT_STATUS = "StartEvent_Status";
    public static final String START_EVENT_CONFIRMATION = "StartEvent_Confirmation";
    public static final String START_EVENT_NOTES = "StartEvent_Notes";
    public static final String START_EVENT_ORDER_MANAGER_DETAILS = "StartEvent_OrderManagerDetails";
    public static final String START_EVENT_GLAN_ID_UPDATION = "StartEvent_GlanIdUpdation";
    public static final String START_EVENT_GLAN_ID_UPDATION_BEFORE_ACCEPT = "StartEvent_GlanIdUpdationBeforeAccept";
    public static final String ACT_ID_ORDER_STATUS_UPDATE_COMPLETED = "Activity_OrderStatusUpdateCompleted";
    public static final String END_EVENT_PFIB_ORDER = "EndEvent_PfibOrderCompleted";
    public static final String END_EVENT_NOTES = "EndEvent_Notes";

    public static final String ACT_ID_SUPPLIER_PROVISIONING_PROCESS = "Activity_SupplierProvisioningProcess";
    public static final String ACT_ID_SUPPLIER_ACCEPT_PROCESS = "Activity_SupplierAcceptProcess";
    public static final String ACT_ID_SEND_REJECT_NOTIFICATION = "Activity_SendRejectNotification";
    public static final String ACT_ID_ORDER_STATUS_UPDATE_REJECTED = "Activity_OrderStatusUpdateRejected";
    public static final String END_EVENT_PFIB_ORDER_REJECTED = "EndEvent_PfibOrderRejected";
    public static final String ACT_ID_SEND_UNDELIVERABLE_NOTIFICATION = "Activity_SendUndeliverableNotification";
    public static final String ACT_ID_PROCESS_UNDELIVERABLE_NOTIFICATION = "Activity_PbtdcProcessUndeliverable";
    public static final String ACT_ID_SEND_DELAY_START_NOTIFICATION = "Activity_SendCustomerDelayStartNotification";
    public static final String ACT_ID_SEND_CIRCUIT_ID_NOTIFICATION = "Activity_SendCircuitIdNotification";
    public static final String ACT_ID_SEND_DELAY_END_NOTIFICATION = "Activity_SendCustomerDelayEndNotification";
    public static final String ACT_ID_ORDER_STATUS_UPDATE_UNDELIVERABLE = "Activity_OrderStatusUpdateUndeliverable";
    public static final String ACT_ID_SEND_APPOINTMENT_REQUEST_NOTIFICATION = "Activity_SendAppointmentRequestNotification";
    public static final String ACT_ID_PROCESS_APPOINTMENT_REQUEST_NOTIFICATION = "Activity_ProcessAppointmentRequestNotification";
    public static final String ACT_ID_SEND_STATUS_NOTIFICATION = "Activity_SendStatusNotification";
    public static final String ACT_ID_SEND_CONFIRMATION_NOTIFICATION = "Activity_SendConfirmationNotification";
    public static final String ACT_ID_SEND_NOTES_NOTIFICATION = "Activity_SendNotesNotification";
    public static final String ACT_ID_SEND_OMD_CONFIRMATION_NOTIFICATION = "Activity_SendOMDConfirmationNotification";
    public static final String ACT_ID_PROCESS_STATUS_NOTIFICATION = "Activity_ProcessStatusNotification";
    public static final String ACT_ID_PROCESS_CONFIRMATION_NOTIFICATION = "Activity_ProcessConfirmationNotification";
    public static final String ACT_ID_PROCESS_NOTES_NOTIFICATION = "Activity_ProcessNotesNotification";
    public static final String ACT_ID_PROCESS_ORDER_MANAGER_DET_NOTIFICATION = "Activity_ProcessOrderManagerDetailsNotification";
    public static final String ACT_ID_PROCESS_GLAN_ID_UPDATION_NOTIFICATION = "Activity_ProcessGlanIdUpdationNotification";
    public static final String ACT_ID_PROCESS_GLAN_ID_UPDATION_NOTIFICATION_BEFORE_ACCEPT = "Activity_ProcessGlanIdUpdationNotificationBeforeAccept";
    public static final String ACT_ID_SEND_CF_NOTIFICATION_AFTER_NOTES_NOTIFICATION = "Activity_SendCFNotificationAfterNotesNotification";
    public static final String END_EVENT_PBTDC_ORDER_UNDELIVERABLE = "EndEvent_PbtdcOrderUndeliverable";
    public static final String END_EVENT_APPOINTMENT_REQUEST = "EndEvent_AppointmentRequest";
    public static final String END_EVENT_GLAN_ID_UPDATION = "EndEvent_GlanIdUpdation";
    public static final String END_EVENT_GLAN_ID_UPDATION_BEFORE_ACCEPT = "EndEvent_GlanIdUpdationBeforeAccept";

    public static final String MSG_PREFIX = "Msg_";
    public static final String MSG_NAME_UNDELIVERABLE = "Msg_SupplierUndeliverable";
    public static final String MSG_NAME_DELAY_START = "Msg_SupplierCustomerDelayStart";
    public static final String MSG_NAME_DELAY_END = "Msg_SupplierCustomerDelayEnd";
    public static final String MSG_NAME_CIRCUIT_ID = "Msg_SupplierCircuitId";
    public static final String MSG_NAME_APPOINTMENT_REQUEST = "Msg_SupplierAppointmentRequest";
    public static final String MSG_NAME_STATUS = "Msg_SupplierStatus";
    public static final String MSG_NAME_Confirmation = "Msg_SupplierConfirmation";
    public static final String MSG_NAME_NOTES = "Msg_SupplierNotes";
    public static final String MSG_NAME_ACCEPT = "Msg_SupplierAccept";
    public static final String MSG_NAME_COMPLETED = "Msg_SupplierCompleted";
    public static final String MSG_ORDER_MANAGER_DETAILS = "Msg_SupplierOrderManagerDetails";
    public static final String MSG_NAME_GLAN_ID_UPDATION = "Msg_SupplierGlanIdUpdation";
    public static final String PARKED_NOTIFICATION_ID = "parkedNotificationId";

    public static final String VAR_NAME_INTERNAL_NOTIFICATION_TYPE = "internalNotificationType";
    public static final String VAR_NAME_DELIVERY_ON_TRACK_CHANGED = "deliveryOnTrackChanged";
    public static final String VAR_NAME_NOTES_TEXT_EMPTY = "notesTextEmpty";
    public static final String VAR_NAME_WSIPT_PRODUCT= "wsiptProduct";

    public static final class OrderValidationProcess {
        
        private OrderValidationProcess() {
        }
        public static final String PROCESS_PBTDC_ORDER = "DataCircuitProvisionningOrderProcess";
        public static final String PROCESS_ORDER_VALIDATION = "Activity_OrderValidationProcess";
        public static final String ACT_ID_CHECK_QUOTE = "Activity_CheckQuote";
        public static final String ACT_ID_CHECK_ORDER_DUPLICATE = "Activity_CheckOrderDuplicate";
        public static final String ACT_ID_CHECK_INVENTORY = "Activity_CheckInventory";
        public static final String ACT_ID_SEND_POA = "Activity_SendPOANotification";
        public static final String BOUNDARY_EVENT_QUOTE_INVALID = "BoundaryEvent_QuoteInvalid";
        public static final String BOUNDARY_EVENT_USER_UNAUTHORIZED = "BoundaryEvent_UserUnauthorized";
        public static final String BOUNDARY_EVENT_DUPLICATE_ORDER_FOUND = "BoundaryEvent_DuplicateOrderFound";
        public static final String BOUNDARY_EVENT_ETHERWAY_NOT_BELONG_TO_OAO = "BoundaryEvent_EtherwayNotBelongToOao";
        public static final String BOUNDARY_EVENT_ORDER_INVALID = "BoundaryEvent_OrderInvalid";
        public static final String END_EVENT_ORDER_INVALID = "EndEvent_OrderInvalid";

    }

    public static final class SupplierProvisioningProcess {

        private SupplierProvisioningProcess() {
        }

        public static final String PROCESS_SUPPLIER_PROV = "Activity_SupplierProvisioningProcess";
        public static final String ACT_ID_SEND_ORDER = "Activity_SendOrder";
        public static final String ACT_ID_SEND_ACCEPT_NOTIFICATION = "Activity_SendAcceptNotification";
        public static final String ACT_ID_PROCESS_ACCEPT_NOTIFICATION = "Activity_ProcessAcceptNotification";
        public static final String ACT_ID_SEND_COMPLETE_NOTIFICATION = "Activity_SendCompleteNotification";
        public static final String ACT_ID_PROCESS_COMPLETE_NOTIFICATION = "Activity_ProcessCompleteNotification";
        public static final String GATEWAY_SUPPLIER_PROVISIONING_ACCEPT_OR_REJECT = "Gateway_SupplierProvisioningAcceptOrReject";
        public static final String ACT_ID_PROCESS_CUST_DELAY_START_NOTIFICATION = "Activity_ProcessCustomerDelayStartNotification";
        public static final String ACT_ID_PROCESS_CUST_DELAY_END_NOTIFICATION = "Activity_ProcessCustomerDelayEndNotification";
        public static final String ACT_ID_PROCESS_CIRCUIT_ID_NOTIFICATION = "Activity_ProcessCircuitIdNotification";

        public static final String BOUNDARY_EVENT_SUPPLIER_REJECTED_PROVISIONING = "BoundaryEvent_SupplierRejectProvisioning";
        public static final String BOUNDARY_EVENT_SUPPLIER_ORDER_REJECTED = "BoundaryEvent_SupplierOrderRejected";
        public static final String BOUNDARY_EVENT_SUPPLIER_ORDER_UNDELIVERABLE = "BoundaryEvent_SupplierOrderUndeliverable";
        public static final String END_EVENT_ORDER_REJECTED = "EndEvent_OrderRejected";

        public static final String PROCESS_COMPLETED = "Activity_CompletedNotificationProcess";

    }

}
