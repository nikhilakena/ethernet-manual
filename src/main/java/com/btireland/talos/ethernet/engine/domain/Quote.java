package com.btireland.talos.ethernet.engine.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "quote")
public class Quote {

    @Id
    @Column(name = "quote_item_id")
    private Long quoteItemId;

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

    @Column(name = "recurring_frequency")
    @Size(max = 7)
    private String recurringFrequency;

    @Column(name = "qbtdc_order_number")
    @Size(max = 32)
    private String orderNumber;

    @Column(name = "qbtdc_project_key")
    @Size(max = 32)
    private String projectKey;

    @Column(name = "a_end_target_access_supplier")
    @Size(max = 7)
    private String aendTargetAccessSupplier;

    @Column(name = "ip_range")
    private Integer ipRange;

    @Column(name = "logical_bandwidth")
    @Size(max = 5)
    private String logicalBandwidth;

    @Column(name = "a_end_bandwidth")
    @Size(max = 3)
    private String aendBandwidth;

    @Column(name = "status")
    @Size(max = 8)
    private String status;
}
