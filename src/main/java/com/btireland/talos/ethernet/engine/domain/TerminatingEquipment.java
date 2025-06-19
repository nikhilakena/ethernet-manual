package com.btireland.talos.ethernet.engine.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class TerminatingEquipment implements Serializable {

    @Column(name = "term_equipment_port_setting")
    @Size(max = 20)
    private String portSetting;

    @Column(name = "term_equipment_presentation")
    @Size(max = 10)
    private String presentation;

    @Column(name = "term_equipment_cable_manager", nullable = false)
    private Boolean cableManager;

    @Column(name = "term_equipment_port", nullable = false)
    @Size(max = 10)
    private String port;

    @Column(name = "term_equipment_port_speed", nullable = false)
    @Size(max = 10)
    private String portSpeed;


}
