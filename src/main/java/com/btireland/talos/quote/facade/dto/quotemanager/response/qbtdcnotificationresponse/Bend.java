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
public class Bend {
    @NotEmpty
    private BendLocation bendLocation;
    @NotEmpty
    private BendAccess access;
}
