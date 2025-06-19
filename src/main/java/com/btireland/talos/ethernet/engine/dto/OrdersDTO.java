package com.btireland.talos.ethernet.engine.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersDTO {

    private Long orderId;

    private Long talosOrderId;

    private String oao;

    private String agent;

    private String agentEmail;

    private String obo;

    private String dataContract;

    private String originatorCode;

    private String organisationName;

    private String resellerTransactionId;

    private String resellerOrderRequestDateTime;

    private String operatorName;

    private String operatorCode;

    private String orderNumber;

    private String createdAt;

    private String serviceType;

    private String serviceClass;

    private String connectionType;

    private String btGroupRef;

    private String serviceLocationAddressId;

    private String locationDescription;

    private String orderType;

    private String technicianNotes;

    private String internalTrackingOrderReference;

    private String supplierOrderId;

    private String accountNumber;

    private String majorAccountNumber;

    private String dueCompletionDate;

    private String estimatedCompletionDate;

    private InterventionDetailsDTO interventionDetails;

    private String nonStandardFlag;

    private String nonStandardReason;

    private String confirmationResult;

    private String reforecastDueDate;

    private String orderFileName;

    private String mode;

    private String lastNotificationType;

    private String uniqueId;

    private String wasset;

    private String productGroup;

    private String orderStatus;

    private String statusNotes;

    private String projectKey;

    private String delayReason;

    private BigInteger supplierNotificationId;

    private RejectionDetailsDTO rejectionDetails;

    private ContactDetailsDTO contactDetails;

    private String originalOrderRef;

    private CustomerDelayDTO customerDelay;

    private PbtdcDTO pbtdc;

    @JsonIgnore
    public String getProductDesc(){
        if(getServiceClass().equalsIgnoreCase("WIC"))
            return "Wholesale Internet Connect";
        else if(getServiceClass().equalsIgnoreCase("WEC"))
            return "Wholesale Ethernet Connect";
        else
            return null;
    }


}
