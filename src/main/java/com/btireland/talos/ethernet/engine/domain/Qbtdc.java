package com.btireland.talos.ethernet.engine.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "qbtdc")
@PrimaryKeyJoinColumn(name = "order_id")
public class Qbtdc extends Orders implements Serializable {

    @Column(name = "recurring_frequency")
    @Size(max = 7)
    private String recurringFrequency;

    @Column(name = "sync_flag")
    @Size(max = 1)
    private String syncFlag;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "qbtdc")
    private List<QbtdcQuote> quoteItems;

    @Column(name = "delay_reason")
    @Size(max = 500)
    private String delayReason;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "qbtdc")
    private List<QbtdcEmailDetails> qbtdcEmailDetailsList;
}
