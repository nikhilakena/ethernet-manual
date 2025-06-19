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
public class TerminatingEquipmentDTO {

    private String portSetting;

    private String presentation;

    private Boolean cableManager;

    private String port;

    private String portSpeed;
}
