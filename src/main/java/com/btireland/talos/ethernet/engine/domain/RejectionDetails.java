package com.btireland.talos.ethernet.engine.domain;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rejection_details")
public class RejectionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Orders orders;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_item_id", referencedColumnName = "quote_item_id")
    private QbtdcQuote quoteItem;

    @Column(name = "reject_code")
    @Size(max = 32)
    private String rejectCode;

    @Column(name = "reject_reason")
    @Size(max = 255)
    private String rejectReason;

    public void setRejectCode(String rejectCode) {
        this.rejectCode = StringUtils.truncate(rejectCode,32);
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = StringUtils.truncate(rejectReason,255);
    }
}
