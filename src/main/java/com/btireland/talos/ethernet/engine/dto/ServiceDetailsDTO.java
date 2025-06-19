package com.btireland.talos.ethernet.engine.dto;

import com.btireland.talos.ethernet.engine.enums.ActionFlag;
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
public class ServiceDetailsDTO {

    private String serviceName;

    private String serviceValue;

    private ActionFlag action;

}
