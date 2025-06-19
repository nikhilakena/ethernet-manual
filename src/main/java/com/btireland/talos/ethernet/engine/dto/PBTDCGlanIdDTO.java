package com.btireland.talos.ethernet.engine.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * class use to expose our API to the outside world.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PBTDCGlanIdDTO {
    @NotNull(message = "Please provide a valid GLAN ID")

    private String glanId;
    @NotNull(message = "Please provide a valid GLAN ID Type")
    private GLANIDType type;
    private GLANIDSite site;

    public enum GLANIDType{
        BT_ETHERFLOW,
        BT_ETHERWAY
    }

    public enum GLANIDSite{
        A,
        B
    }
}

