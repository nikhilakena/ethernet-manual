package com.btireland.talos.quote.facade.domain.entity;

import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "qbtdc_order_map")
public class QuoteOrderMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_group_id", nullable = false)
    private Long quoteGroupId;

    @Column(name = "operator_code", nullable = false)
    private String operatorCode;

    @Column(name = "operator_name", nullable = false)
    private String operatorName;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "sync_flag", nullable = false, columnDefinition = "BIT")
    private Boolean syncFlag;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private QuoteOrderMapStatus status;

    @Column(name = "supplier")
    private String supplier;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "quoteOrderMap")
    @JsonManagedReference
    private List<QuoteItemMapEntity> quoteItemMapList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "quoteOrderMap")
    @JsonManagedReference
    private List<QuoteEmailEntity> quoteEmails = new ArrayList<>();

    QuoteOrderMapEntity() {
    }

    public QuoteOrderMapEntity(String operatorCode, String operatorName, String orderNumber,
                               Boolean syncFlag, String supplier) {
        this.operatorCode = operatorCode;
        this.operatorName = operatorName;
        this.orderNumber = orderNumber;
        this.syncFlag = syncFlag;
        this.supplier = supplier;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public Long getQuoteGroupId() {
        return quoteGroupId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Boolean getSyncFlag() {
        return syncFlag;
    }

    public QuoteOrderMapStatus getStatus() {
        return status;
    }

    public String getSupplier() {
        return supplier;
    }

    public List<QuoteItemMapEntity> getQuoteItemMapList() {
        return quoteItemMapList;
    }

    public List<QuoteEmailEntity> getQuoteEmails() {
        return quoteEmails;
    }

    public void setQuoteGroupId(Long quoteGroupId) {
        this.quoteGroupId = quoteGroupId;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setStatus(QuoteOrderMapStatus status) {
        this.status = status;
    }

    public void setQuoteItemMapList(List<QuoteItemMapEntity> quoteItemMapList) {
        this.quoteItemMapList = quoteItemMapList;
    }

    public void setQuoteEmails(List<QuoteEmailEntity> quoteEmails) {
        this.quoteEmails = quoteEmails;
    }

    @Override
    public String toString() {
        return "QuoteOrderMapEntity{" +
                "quoteGroupId=" + quoteGroupId +
                ", operatorCode='" + operatorCode + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", syncFlag=" + syncFlag +
                ", status=" + status +
                ", supplier='" + supplier + '\'' +
                ", quoteEmails=" + quoteEmails +
                '}';
    }
}
