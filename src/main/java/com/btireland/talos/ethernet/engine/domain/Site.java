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
public class Site {

    @Column(name = "site_name")
    @Size(max = 50)
    private String name;

    @Embedded
    COMMSRoom commsRoom;

    @Embedded
    Location location;

}
