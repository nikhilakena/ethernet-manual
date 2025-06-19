@BDDTest
@PBTDC
Feature: PBTDC Order Cucumber BDD Tests

 @HappyPath
 Scenario:PBTDC Order Happy Path Scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  When PBTDC Order is received by Ethernet Engine
  Then Workflow has passed activity "Activity_CheckQuote"
  And Get Quote API is called with Quote Item Id "1"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And A notification of type "POA" with reference "BT-PBTDC-1" has been sent to NotCom
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  Given Generate API is called with date "2022-01-01"
  And the expected JSON internal report file is "PBTDCOrderHappyPath/expected_internal_report.json"
  When the JSON report requested with date "2022-01-01" and oao "SKY"
  Then the internal report matches the expected internal report

 @HappyPath_Accept
 Scenario:PBTDC Order Accept Notification received scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  When "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  Then Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  And A notification of type "A" with reference "BT-PBTDC-1" has been sent to NotCom
  Given Generate API is called with date "2022-01-01"
  And the expected JSON internal report file is "PBTDCOrderHappyPath_Accept/expected_internal_report.json"
  When the JSON report requested with date "2022-01-01" and oao "SKY"
  Then the internal report matches the expected internal report

 @HappyPath_Accept_Confirmation
 Scenario:PBTDC Order Accept and Confirmation Notification received scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  Then Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  And A notification of type "A" with reference "BT-PBTDC-1" has been sent to NotCom
  When "Confirmation" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "CF"
  And Workflow has passed activity "Activity_ProcessConfirmationNotification"
  And Workflow has passed activity "Activity_SendConfirmationNotification"
  And A notification of type "CF" with reference "BT-PBTDC-1" has been sent to NotCom
  Given Generate API is called with date "2022-01-01"
  And the expected JSON internal report file is "PBTDCOrderHappyPath_Accept_Confirmation/expected_internal_report.json"
  When the JSON report requested with date "2022-01-01" and oao "SKY"
  Then the internal report matches the expected internal report

 @HappyPath_Accept_Confirmation_Notes
 Scenario:PBTDC Order Accept,Confirmation and Notes Notification received scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  Then Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  And A notification of type "A" with reference "BT-PBTDC-1" has been sent to NotCom
  When "Confirmation" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "CF"
  And Workflow has passed activity "Activity_ProcessConfirmationNotification"
  And Workflow has passed activity "Activity_SendConfirmationNotification"
  And A notification of type "CF" with reference "BT-PBTDC-1" has been sent to NotCom
  When "Notes" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "N"
  And Workflow has passed activity "Activity_ProcessNotesNotification"
  And Workflow has passed activity "Activity_SendNotesNotification"
  And A notification of type "N" with reference "BT-PBTDC-1" has been sent to NotCom
  Given Generate API is called with date "2022-01-01"
  And the expected JSON internal report file is "PBTDCOrderHappyPath_Accept_Confirmation_Notes/expected_internal_report.json"
  When the JSON report requested with date "2022-01-01" and oao "SKY"
  Then the internal report matches the expected internal report

 @HappyPath_Accept_GlanIdUpdation
 Scenario:PBTDC Order Accept and GlanIdUpdation Notification received scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Glan Ids are updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  Then Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  And A notification of type "A" with reference "BT-PBTDC-1" has been sent to NotCom
  When "GlanIdUpdation" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "GIU"
  And Workflow has passed activity "Activity_ProcessGlanIdUpdationNotification"
  Given Generate API is called with date "2022-01-01"
  And the expected JSON internal report file is "PBTDCOrderHappyPath_Accept_GIU/expected_internal_report.json"
  When the JSON report requested with date "2022-01-01" and oao "SKY"
  Then the internal report matches the expected internal report

  @SadPath_Accept_GlanIdUpdateFails
  Scenario:PBTDC Order Accept and GlanIdUpdation Notification failure with Glan ID update
   Given PFIB Order is sent by RSP
   And Order Manager is called to persist data
   And Notification is send to Notcom
   And Quote Data is fetched from Order Manager
   And Quote Data is updated to Order Manager
   And Glan Ids Fails to update on Order Manager
   And Order Manager is called to get agent details
   And Seal has an API to send PBTDC order
   And PBTDC Order is received by Ethernet Engine
   And Workflow has passed activity "Activity_CheckQuote"
   And Workflow has passed activity "Activity_CheckOrderDuplicate"
   And Workflow has passed activity "Activity_SendPOANotification"
   And Workflow has passed activity "Activity_SendOrder"
   And PBTDC Order is sent to Seal
   And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
   Then Workflow has passed activity "Activity_ProcessAcceptNotification"
   And Workflow has passed activity "Activity_SendAcceptNotification"
   And A notification of type "A" with reference "BT-PBTDC-1" has been sent to NotCom
   When "GlanIdUpdation" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "GIU"
   Then an intervention has been created with id 1
   And the created intervention has field "notes" set to "couldn't execute activity <serviceTask id=\"Activity_ProcessGlanIdUpdationNotification\" ...>: Failed to update glanId for Order Id 1"
   And the created intervention has field "workflow" set to "Activity_ProcessGlanIdUpdationNotification"

 @HappyPath_Accept_Confirmation_Complete
  Scenario:PBTDC Order Accept, Confirmation and Completion Notification received scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  Then Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  And A notification of type "A" with reference "BT-PBTDC-1" has been sent to NotCom
  When "Confirmation" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "CF"
  And Workflow has passed activity "Activity_ProcessConfirmationNotification"
  And Workflow has passed activity "Activity_SendConfirmationNotification"
  And A notification of type "CF" with reference "BT-PBTDC-1" has been sent to NotCom
  When "Complete" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "C"
  And Workflow has passed activity "Activity_ProcessCompleteNotification"
  Then Workflow has passed activity "Activity_SendCompleteNotification"
  Given Generate API is called with date "2022-01-01"
  And the expected JSON internal report file is "PBTDCOrderHappyPath_Accept_Conf_Complete/expected_internal_report.json"
  When the JSON report requested with date "2022-01-01" and oao "SKY"
  Then the internal report matches the expected internal report

 @HappyPath_Complete
 Scenario:PBTDC Order Complete Notification received scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  And Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  When "Complete" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "C"
  Then Workflow has passed activity "Activity_ProcessCompleteNotification"

 @HappyPath_Accept_Status
 Scenario:PBTDC Order Accept and Status Notification received scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  Then Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  And A notification of type "A" with reference "BT-PBTDC-1" has been sent to NotCom
  When "Status" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "S"
  And Workflow has passed activity "Activity_ProcessStatusNotification"
  And Workflow has passed activity "Activity_SendStatusNotification"
  And A notification of type "S" with reference "BT-PBTDC-1" has been sent to NotCom
  Given Generate API is called with date "2022-01-01"
  And the expected JSON internal report file is "PBTDCOrderHappyPath_Accept_Status/expected_internal_report.json"
  When the JSON report requested with date "2022-01-01" and oao "SKY"
  Then the internal report matches the expected internal report

 @SadPath_Accept_Status_quote_update_fails
 Scenario:PBTDC Order Accept but updating the quote fails
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data fails to update to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  Then an intervention has been created with id 1
  And the created intervention has field "notes" set to "Failed in updating quote with quote item id 1"
  And the created intervention has field "workflow" set to "Activity_SendPOANotification"

  @HappyPath_Accept_APTR
   Scenario:PBTDC Order Accept and Appointment Request Notification received scenario
    Given PFIB Order is sent by RSP
    And Order Manager is called to persist data
    And Notification is send to Notcom
    And Quote Data is fetched from Order Manager
    And Quote Data is updated to Order Manager
    And Order Manager is called to get agent details
    And Seal has an API to send PBTDC order
    And PBTDC Order is received by Ethernet Engine
    And Workflow has passed activity "Activity_CheckQuote"
    And Workflow has passed activity "Activity_CheckOrderDuplicate"
    And Workflow has passed activity "Activity_SendPOANotification"
    And Workflow has passed activity "Activity_SendOrder"
    And PBTDC Order is sent to Seal
    And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
    Then Workflow has passed activity "Activity_ProcessAcceptNotification"
    And Workflow has passed activity "Activity_SendAcceptNotification"
    And A notification of type "A" with reference "BT-PBTDC-1" has been sent to NotCom
    When "AppointmentRequest" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "APTR"
    And Workflow has passed activity "Activity_ProcessAppointmentRequestNotification"
    And Workflow has passed activity "Activity_SendAppointmentRequestNotification"
    And A notification of type "APTR" with reference "BT-PBTDC-1" has been sent to NotCom
    Given Generate API is called with date "2022-01-01"
    And the expected JSON internal report file is "PBTDCOrderHappyPath_Accept_APTR/expected_internal_report.json"
    When the JSON report requested with date "2022-01-01" and oao "SKY"
    Then the internal report matches the expected internal report

 @HappyPath_Accept_DS
 Scenario:PBTDC Order Accept and CustomerDelayStart Notification received scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  Then Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  And A notification of type "A" with reference "BT-PBTDC-1" has been sent to NotCom
  When "CustomerDelayStart" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "DS"
  And Workflow has passed activity "Activity_ProcessCustomerDelayStartNotification"
  And Workflow has passed activity "Activity_SendCustomerDelayStartNotification"
  And A notification of type "DS" with reference "BT-PBTDC-1" has been sent to NotCom
  Given Generate API is called with date "2022-01-01"
  And the expected JSON internal report file is "PBTDCOrderHappyPath_Accept_DS/expected_internal_report.json"
  When the JSON report requested with date "2022-01-01" and oao "SKY"
  Then the internal report matches the expected internal report

 @HappyPath_Accept_DE
 Scenario:PBTDC Order Accept and CustomerDelayEnd Notification received scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  Then Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  And A notification of type "A" with reference "BT-PBTDC-1" has been sent to NotCom
  When "CustomerDelayEnd" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "DE"
  And Workflow has passed activity "Activity_ProcessCustomerDelayEndNotification"
  And Workflow has passed activity "Activity_SendCustomerDelayEndNotification"
  And A notification of type "DE" with reference "BT-PBTDC-1" has been sent to NotCom
  Given Generate API is called with date "2022-01-01"
  And the expected JSON internal report file is "PBTDCOrderHappyPath_Accept_DE/expected_internal_report.json"
  When the JSON report requested with date "2022-01-01" and oao "SKY"
  Then the internal report matches the expected internal report

 @HappyPath_Accept_CI
 Scenario:PBTDC Order Accept and CircuitId Notification received scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  Then Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  And A notification of type "A" with reference "BT-PBTDC-1" has been sent to NotCom
  When "CircuiId" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "CI"
  And Workflow has passed activity "Activity_ProcessCircuitIdNotification"
  And Workflow has passed activity "Activity_SendCircuitIdNotification"
  And A notification of type "CI" with reference "BT-PBTDC-1" has been sent to NotCom
  Given Generate API is called with date "2022-01-01"
  And the expected JSON internal report file is "PBTDCOrderHappyPath_Accept_CI/expected_internal_report.json"
  When the JSON report requested with date "2022-01-01" and oao "SKY"
  Then the internal report matches the expected internal report

 @Undeliverable
 Scenario:PBTDC Order Undeliverable Notification received scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  And Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  When "Undeliverable" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "U"
  Then Workflow has passed activity "Activity_PbtdcProcessUndeliverable"
  And Workflow has passed activity "Activity_SendUndeliverableNotification"
  And A notification of type "U" with reference "BT-PBTDC-1" has been sent to NotCom
  And Workflow has passed activity "Activity_OrderStatusUpdateUndeliverable"
  Given Generate API is called with date "2022-01-01"
  And the expected JSON internal report file is "PBTDCOrderUnhappyPath_Accept_Undeliverable/expected_internal_report.json"
  When the JSON report requested with date "2022-01-01" and oao "SKY"
  Then the internal report matches the expected internal report


@Intervention_Scenario_Confirmation_Received_Before_Accept
 Scenario:PBTDC Order Confirmation Notification received before Accept scenario
  Given PFIB Order is sent by RSP
  And Order Manager is called to persist data
  And Notification is send to Notcom
  And Quote Data is fetched from Order Manager
  And Quote Data is updated to Order Manager
  And Order Manager is called to get agent details
  And Seal has an API to send PBTDC order
  And PBTDC Order is received by Ethernet Engine
  And Workflow has passed activity "Activity_CheckQuote"
  And Workflow has passed activity "Activity_CheckOrderDuplicate"
  And Workflow has passed activity "Activity_SendPOANotification"
  And Workflow has passed activity "Activity_SendOrder"
  And PBTDC Order is sent to Seal
  When "Confirmation" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "CF"
  Then Get Intervention api is called
  And Close Intervention api is called
  And "Accept" Notification received from Seal with orderReference "TALOS-1-PBTDC-1" and notification type "A"
  And Workflow has passed activity "Activity_ProcessAcceptNotification"
  And Workflow has passed activity "Activity_SendAcceptNotification"
  And Notification processor is triggered
  And Workflow has passed activity "Activity_ProcessConfirmationNotification"
  And Workflow has passed activity "Activity_SendConfirmationNotification"
  And A notification of type "CF" with reference "BT-PBTDC-1" has been sent to NotCom