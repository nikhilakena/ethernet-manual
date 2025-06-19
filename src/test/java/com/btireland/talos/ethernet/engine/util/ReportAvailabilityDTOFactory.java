package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.dto.ReportAvailabilityDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.LinkedHashMap;

public class ReportAvailabilityDTOFactory {
    private static String asJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public static ReportAvailabilityDTO reportAvailability(){
        LinkedHashMap<LocalDate, Boolean> availabilityMap = new LinkedHashMap<>();
        availabilityMap.put(LocalDate.parse("2022-02-01"),false);
        availabilityMap.put(LocalDate.parse("2022-02-02"),false);
        availabilityMap.put(LocalDate.parse("2022-02-03"),true);
        availabilityMap.put(LocalDate.parse("2022-02-04"),false);
        return ReportAvailabilityDTO.builder().reportAvailabilityDates(availabilityMap).build();
    }

    public static String asReportAvailabilityJson() throws JsonProcessingException {
        return asJson(reportAvailability());
    }
}
