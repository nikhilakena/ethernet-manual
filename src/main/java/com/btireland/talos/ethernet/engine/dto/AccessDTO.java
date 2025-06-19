package com.btireland.talos.ethernet.engine.dto;

import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessDTO {

    private Long id;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime changedAt;

    private String circuitReference;

    private ActionFlag action;

    private String serviceClass;

    private String bandWidth;

    private String sla;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime appointmentRequestReceivedTimestamp;

    private TerminatingEquipmentDTO terminatingEquipment;

    SiteDTO site;

    AccessInstallDTO accessInstall;

    private String circuitType;

    private String supplier;

    private String targetAccessSupplier;

}
