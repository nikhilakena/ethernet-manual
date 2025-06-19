package com.btireland.talos.quote.facade.domain.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "quote_email")
public class QuoteEmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "quote_group_id", referencedColumnName = "quote_group_id", nullable = false)
    @JsonBackReference
    private QuoteOrderMapEntity quoteOrderMap;

    @Column(name = "email")
    private String email;

    QuoteEmailEntity() {
    }

    public QuoteEmailEntity(QuoteOrderMapEntity quoteOrderMap, String email) {
        this.quoteOrderMap = quoteOrderMap;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public QuoteOrderMapEntity getQuoteOrderMap() {
        return quoteOrderMap;
    }

    public String getEmail() {
        return email;
    }

    public void setQuoteOrderMap(QuoteOrderMapEntity quoteOrderMap) {
        this.quoteOrderMap = quoteOrderMap;
    }

    @Override
    public String toString() {
        return "QuoteEmailEntity{" +
                "id=" + id +
                ", quoteOrderMap=" + quoteOrderMap.getQuoteGroupId() +
                ", email='" + email + '\'' +
                '}';
    }
}
