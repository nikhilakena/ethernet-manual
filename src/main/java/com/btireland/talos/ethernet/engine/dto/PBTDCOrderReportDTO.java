package com.btireland.talos.ethernet.engine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PBTDCOrderReportDTO {
    private String orderManager;
    private String orderManagerEmail;
    private String customerName;

    private String siteName;
    private String dateReceivedByDelivery;

    // Reference IDs. This is a formatted string including the following data :
    // - NOG Order Number
    // - Siebel Account Number
    // - Siebel Order Ref
    // - Logical BT Circuit ID
    // - A End Etherway BT Circuit ID
    // - B End Etherway BT Circuit ID
    // - BT Logical VLAN
    // - Lead Delivery Manager firstname, lastname, contact number, email
    // - customer internal details 1-5

    private String referenceIds;

    private String product;
    private String connectionType;

    private String deliveryType;

    // a-end = customer
    // b-end = wholesaler

    private String aendSiteDetails;
    private String bendSiteDetails;

    private String aendContactDetails;
    private String bendContactDetails;

    private String accessSpeed;

    private String portSpeed;

    private String aendPresentation;
    private String bendPresentation;

    private String notes;

    private String indicativeDeliveryDate;

    private String deliveryDate;

    private String deliveryOnTrack;

    // Phase - Order Entry and Validation Status
    private String orderEntryAndValidationStatus;
    private String planningStatus;

    private String accessInstallation;
    private String testingCpeInstallation;

    private String serviceCompleteAndOperational;

}
