package com.btireland.talos.ethernet.engine.dto;


import com.btireland.talos.ethernet.engine.enums.PowerSocketType;
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
public class COMMSRoomDTO {

    private String details;

    private PowerSocketType powerSocketType;

}
