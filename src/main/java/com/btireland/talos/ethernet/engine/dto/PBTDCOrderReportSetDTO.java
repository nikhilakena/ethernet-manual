package com.btireland.talos.ethernet.engine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PBTDCOrderReportSetDTO {
    List<PBTDCOrderReportDTO> internalReportEntries;
    List<PBTDCOrderReportDTO> externalReportEntries;
}
