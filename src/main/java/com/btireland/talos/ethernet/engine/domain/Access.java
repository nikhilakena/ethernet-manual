package com.btireland.talos.ethernet.engine.domain;

import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "access")
public class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @UpdateTimestamp
    @Column(name = "last_changed", nullable = false)
    private LocalDateTime changedAt;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "circuit_reference")
    @Size(max = 20)
    private String circuitReference;

    @Column(name = "action", nullable = false)
    private ActionFlag action;

    @Column(name = "service_class")
    @Size(max = 20)
    private String serviceClass;

    @Column(name = "bandwidth")
    @Size(max = 5)
    private String bandWidth;

    @Column(name = "sla")
    @Size(max = 8)
    private String sla;

    @Column(name = "appointment_request_received_timestamp")
    private LocalDateTime appointmentRequestReceivedTimestamp;

    @Embedded
    TerminatingEquipment terminatingEquipment;

    @Embedded
    Site site;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY,mappedBy = "access")
    AccessInstall accessInstall;

    @Column(name = "glan_id")
    @Size(max = 20)
    private String glanId;

    @Column(name = "supplier")
    @Size(max = 7)
    private String supplier;

    @Column(name = "target_access_supplier")
    @Size(max = 7)
    private String targetAccessSupplier;

}
