package com.btireland.talos.ethernet.engine.dto;

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
public class QuoteDTO {

    private Long quoteItemId;

    private Integer term;

    private String nonRecurringPrice;

    private String recurringPrice;

    private String recurringFrequency;

    private String orderNumber;

    private String projectKey;

    private String aendTargetAccessSupplier;

    private Integer ipRange;
}
