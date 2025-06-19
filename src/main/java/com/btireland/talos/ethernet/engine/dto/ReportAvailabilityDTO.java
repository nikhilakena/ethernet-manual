package com.btireland.talos.ethernet.engine.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportAvailabilityDTO {

    private Map<LocalDate,Boolean> reportAvailabilityDates;

    @JsonAnyGetter
    public Map<LocalDate, Boolean> getReportAvailabilityDates() {
        return reportAvailabilityDates;
    }
}
