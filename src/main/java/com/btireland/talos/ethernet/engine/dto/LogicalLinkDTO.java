package com.btireland.talos.ethernet.engine.dto;

import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogicalLinkDTO {

    private String circuitReference;

    private String vlan;

    private ActionFlag action;

    private String serviceClass;

    private String bandWidth;

    private String ip;

    private Integer voiceChannels;

    private List<ServiceDetailsDTO> serviceDetails;

    private String circuitType;

    private String profile;

    private String ipRange;

}
