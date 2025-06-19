package com.btireland.talos.ethernet.engine.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "qbtdc_quote")
public class QbtdcQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_item_id")
    private Long quoteItemId;

    @Column(name = "ref_quote_item_id")
    private Long quoteRefItemId;

    @Column(name = "wag_quote_item_id")
    private Long wagQuoteItemId;

    @UpdateTimestamp
    @Column(name = "last_changed", nullable = false)
    private LocalDateTime changedAt;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "term")
    private Integer term;

    @Column(name = "non_recurring_price")
    @Size(max = 32)
    private String nonRecurringPrice;

    @Column(name = "recurring_price")
    @Size(max = 32)
    private String recurringPrice;

    @Column(name = "etherflow_recurring_price")
    @Size(max = 32)
    private String etherflowRecurringPrice;

    @Column(name = "etherway_recurring_price")
    @Size(max = 32)
    private String etherwayRecurringPrice;

    @Column(name = "status")
    @Size(max = 8)
    private String status;

    @Column(name = "notes")
    @Size(max=500)
    private String notes;

    @Column(name = "offline_quoted", columnDefinition = "BIT")
    private Boolean offlineQuoted;

    @Column(name = "group_name")
    @Size(max=3)
    private String group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private Qbtdc qbtdc;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "logical_link_id", referencedColumnName = "id")
    private LogicalLink logicalLink;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_access_id")
    private Access customerAccess;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "wholesaler_access_id")
    private Access wholesalerAccess;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "rejection_details_id")
    private RejectionDetails rejectionDetails;

}
