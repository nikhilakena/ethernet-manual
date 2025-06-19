package com.btireland.talos.ethernet.engine.client.asset.ordermanager;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PBTDCDTO {

    private Long id;

    private LocalDateTime changedAt;

    private LocalDateTime createdAt;

    @NotNull(message = "Please provide a valid order id")
    private Long orderId;

    private String applicationDate;

    private String organisationName;

    private Integer refQuoteItemId;

    private String connectionType;

    private String btGroupRef;

    private Integer originalOrderId;

    private String firstSiteContactFirstName;

    private String firstSiteContactLastName;

    private String firstSiteContactContactNumber;

    private String firstSiteContactEmail;

    private String secSiteContactFirstName;

    private String secSiteContactLastName;

    private String secSiteContactContactNumber;

    private String secSiteContactEmail;

    private String landlordFirstName;

    private String landlordLastName;

    private String landlordContactNumber;

    private String landlordEmail;

    private String buildingMgrFirstName;

    private String buildingMgrLastName;

    private String buildingMgrContactNumber;

    private String buildingMgrEmail;

    private String methodInsuranceCert;

    private String siteInduction;

    private String commsRoomDetails;

    private String readyForDelivery;

    private String powerSocketType;

    private String cableManager;

    private String aEndActionFlag;

    private String circuitReference;

    private String presentation;

    private String nniId;

    private String vlan;

    private String bEndActionFlag;

    private String notes;

    private String glanNumber;

    private String siebelNumber;

    private String leadDeliveryManagerFirstName;

    private String leadDeliveryManagerLastName;

    private String leadDeliveryManagerContactNumber;

    private String leadDeliveryManagerEmail;

    private Map<String,String> customerInternalDetails;

    @JsonAnyGetter
    public Map<String, String> getCustomerInternalDetails() {
        return customerInternalDetails;
    }

    public void addCustomerInternalDetails(String key, String value) {
        if (this.customerInternalDetails == null) {
            this.customerInternalDetails = new HashMap<>();
        }
        this.customerInternalDetails.put(key, value);
    }


}
