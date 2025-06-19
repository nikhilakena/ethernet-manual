package com.btireland.talos.ethernet.engine.client.asset.ordermanager;

import com.btireland.talos.ethernet.engine.dto.RejectionDetailsDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersDTO {

    private Long orderId;

    private String changedAt;

    private String createdAt;

    private String oao;

    private String obo;

    private String operatorName;

    private String operatorCode;

    private String dataContractName;

    private String originatorCode;

    private String transactionId;

    private String orderRequestDateTime;

    @NotEmpty(message = "Please provide a valid order number or a value of AUTOGENERATE if required to be generated")
    private String orderNumber;

    @NotEmpty(message = "Please provide a valid Service type")
    private String orderServiceType;

    private String telephoneNo;

    private String accountNumber;

    private String targetAccount;

    private String wassetId;

    private String orderStatus;

    private String workflowStatus;

    private String referenceOrderId;

    private String siebelNumber;

    private String esla;

    private String agent;

    private String dsl;

    private String supplierName;

    private String supplierOrderId;

    private String supplierOrderReference;

    private String dueCompletionDate;

    private String pcn;

    private String projectKey;

    private RejectionDetailsDTO rejectDetails;

}
