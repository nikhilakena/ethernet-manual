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
public class Address implements Serializable {

    @Column(name = "site_location_line1")
    @Size(max = 50)
    private String locationLine1;

    @Column(name = "site_location_line2")
    @Size(max = 50)
    private String locationLine2;

    @Column(name = "site_location_line3")
    @Size(max = 50)
    private String locationLine3;

    @Column(name = "site_city")
    @Size(max = 20)
    private String city;

    @Column(name = "site_county")
    @Size(max = 20)
    private String county;

    @Column(name = "full_address")
    @Size(max = 255)
    private String fullAddress;
}
