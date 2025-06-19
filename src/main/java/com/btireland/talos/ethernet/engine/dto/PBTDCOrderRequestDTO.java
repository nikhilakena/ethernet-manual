package com.btireland.talos.ethernet.engine.dto;

import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class PBTDCOrderRequestDTO {

    private Pbtdc pbtdc;
    private PBTDCOtherDetailsDTO pbtdcOtherDetails;

}
