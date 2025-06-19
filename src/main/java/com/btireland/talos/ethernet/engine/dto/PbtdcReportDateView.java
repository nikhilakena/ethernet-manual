package com.btireland.talos.ethernet.engine.dto;

import java.time.LocalDate;
import java.util.Date;

public interface PbtdcReportDateView {

    Date getReportDate();
    void setReportDate(LocalDate localDate);
}
