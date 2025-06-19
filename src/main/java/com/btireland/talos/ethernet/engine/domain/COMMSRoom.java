package com.btireland.talos.ethernet.engine.domain;


import com.btireland.talos.ethernet.engine.enums.PowerSocketType;
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
public class COMMSRoom implements Serializable {

    @Column(name = "comms_room_details", nullable = false)
    @Size(max = 150)
    private String details;

    @Column(name = "comms_room_power_socket_type", nullable = false)
    private PowerSocketType powerSocketType;

}
