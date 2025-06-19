package com.btireland.talos.quote.facade.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "qbtdc_item_map")
public class QuoteItemMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "quote_id", nullable = false)
    private Long quoteId;

    @Column(name = "wag_quote_id", nullable = false)
    private Long wagQuoteId;

    @Column(name = "offline_quoted", nullable = false, columnDefinition = "BIT")
    private boolean offlineQuoted;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quote_group_id", referencedColumnName = "quote_group_id", nullable = false, foreignKey = @ForeignKey(name = "fk__quote_item_map__quote_group_id"))
    @JsonBackReference
    private QuoteOrderMapEntity quoteOrderMap;

    public QuoteItemMapEntity() {
        //For Hibernate
    }

    public QuoteItemMapEntity(Long quoteId, Long wagQuoteId, QuoteOrderMapEntity quoteOrderMap) {
        this.quoteId = quoteId;
        this.wagQuoteId = wagQuoteId;
        this.quoteOrderMap = quoteOrderMap;
    }

    public Long getId() {
        return id;
    }

    public Long getQuoteId() {
        return quoteId;
    }

    public Long getWagQuoteId() {
        return wagQuoteId;
    }

    public QuoteOrderMapEntity getQuoteOrderMap() {
        return quoteOrderMap;
    }

    public boolean isOfflineQuoted() {
        return offlineQuoted;
    }

    public void setOfflineQuoted(boolean offlineQuoted) {
        this.offlineQuoted = offlineQuoted;
    }

    @Override
    public String toString() {
        return "QuoteItemMapEntity{" +
            "id=" + id +
            ", quoteId=" + quoteId +
            ", wagQuoteId=" + wagQuoteId +
            ", offlineQuoted=" + offlineQuoted +
            ", quoteOrderMap=" + quoteOrderMap +
            '}';
    }
}
