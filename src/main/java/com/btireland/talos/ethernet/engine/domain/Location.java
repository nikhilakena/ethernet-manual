package com.btireland.talos.ethernet.engine.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class Location {

    @Column(name = "location_id")
    @Size(max = 20)
    private String id;

    @Column(name = "location_type")
    @Size(max = 10)
    private String type;

    @Column(name = "network_status")
    @Size(max = 7)
    private String networkStatus;

    @Column(name = "multi_eircode")
    @Size(max = 1)
    private String multiEircode;

    @Embedded
    Address address;

}
