package com.btireland.talos.quote.facade.dto.quotemanager.response.qbtdcnotificationresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AendAccess {
    @NotEmpty
    private String actionFlag;
    private String reqAccessSupplier;
    private String targetAccessSupplier;
    private String bandwidth;
    private String sla;
}
