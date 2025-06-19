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
public class SupplierOrder implements Serializable {

    @Column(name = "supplier_order_number")
    @Size(max = 20)
    private String orderNumber;

    @Column(name = "siebel_number")
    @Size(max = 30)
    private String siebelNumber;

 }
