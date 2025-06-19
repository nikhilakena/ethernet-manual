package com.btireland.talos.ethernet.engine.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pbtdc_business_status")
public class PBTDCBusinessStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @UpdateTimestamp
    @Column(name = "last_changed", nullable = false)
    private LocalDateTime changedAt;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "order_entry_and_validation_status")
    @Size(max = 50)
    private String orderEntryAndValidationStatus;

    @Column(name = "planning_status")
    @Size(max = 50)
    private String planningStatus;

    @Column(name = "access_installation")
    @Size(max = 50)
    private String accessInstallation;

    @Column(name = "network_provisioning")
    @Size(max = 50)
    private String networkProvisioning;

    @Column(name = "testing_cpe_installation")
    @Size(max = 50)
    private String testingCpeInstallation;

    @Column(name = "service_complete_and_operational")
    @Size(max = 50)
    private String serviceCompleteAndOperational;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "delivery_on_track", columnDefinition = "BIT")
    private Boolean deliveryOnTrack;

}
