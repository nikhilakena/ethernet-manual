package com.btireland.talos.ethernet.engine.repository;

import com.btireland.talos.ethernet.engine.domain.PBTDCReportRecord;
import com.btireland.talos.ethernet.engine.dto.PbtdcReportDateView;

import java.util.Date;
import java.util.List;

public interface ReportRepository extends JpaRsqlRepository<PBTDCReportRecord, Long>{
    PBTDCReportRecord findByReportDateAndOao(Date reportDate, String oao);

    boolean existsByReportDate(Date reportDate);

    long deleteByReportDateEquals(Date reportDate);

    List<PbtdcReportDateView> findByOaoAndReportDateBetween(String oao, Date startDate, Date endDate);
}
