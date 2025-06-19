package com.btireland.talos.ethernet.engine.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class CustomerDelay implements Serializable {

    @Column(name = "cust_delay_start_date")
    private LocalDateTime startDate;

    @Column(name = "cust_delay_end_date")
    private LocalDateTime endDate;

    @Column(name = "cust_delay_reason")
    @Size(max = 50)
    private String reason;

}
