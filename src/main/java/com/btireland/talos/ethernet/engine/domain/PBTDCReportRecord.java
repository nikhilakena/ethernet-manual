package com.btireland.talos.ethernet.engine.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pbtdc_report")
public class PBTDCReportRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "report_date")
    @Temporal(TemporalType.DATE)
    private Date reportDate;

    @Lob
    @Column(name = "compressed_report")
    private byte[] serializedReportInput;

    @Column(name = "oao")
    private String oao;
}
