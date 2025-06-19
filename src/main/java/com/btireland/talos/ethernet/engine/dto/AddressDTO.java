package com.btireland.talos.ethernet.engine.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDTO {

    private String locationLine1;

    private String locationLine2;

    private String locationLine3;

    private String city;

    private String county;

    private String eircode;

    private String fullAddress;

}
