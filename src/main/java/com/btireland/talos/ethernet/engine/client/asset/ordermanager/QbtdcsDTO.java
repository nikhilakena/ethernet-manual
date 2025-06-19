package com.btireland.talos.ethernet.engine.client.asset.ordermanager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QbtdcsDTO {

    private Long id;

    @NotNull(message = "Please provide a valid order id")
    private Long orderId;

    private String mode;

    private String applicationDate;

    private String recurringFrequency;

    private String delayReason;

}
