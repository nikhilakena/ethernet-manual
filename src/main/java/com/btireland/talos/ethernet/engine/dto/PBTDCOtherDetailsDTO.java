package com.btireland.talos.ethernet.engine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class PBTDCOtherDetailsDTO {
    private String firstSiteContactFirstName;
    private String firstSiteContactLastName;
    private String firstSiteContactContactNumber;
    private String firstSiteContactEmail;
    private String methodInsuranceCert;
    private String siteInduction;
    private String commsRoomDetails;
    private String readyForDelivery;
    private String powerSocketType;
    private String cableManager;
    private String presentation;
    private String aendActionFlag;
}
