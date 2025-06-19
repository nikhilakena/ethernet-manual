package com.btireland.talos.ethernet.engine.domain;

import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "logical_link")
public class LogicalLink {

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

    @Column(name = "vlan")
    @Size(max = 20)
    private String vlan;

    @Column(name = "action", nullable = false)
    private ActionFlag action;

    @Column(name = "service_class")
    @Size(max = 20)
    private String serviceClass;

    @Column(name = "bandwidth")
    @Size(max = 5)
    private String bandWidth;

    @Column(name = "ip")
    @Size(max = 50)
    private String ip;

    @Column(name = "voice_channel")
    private Integer voiceChannels;

    @Column(name = "glan_id")
    @Size(max = 20)
    private String glanId;

    @Column(name = "profile")
    @Size(max = 15)
    private String profile;

    @Column(name = "ip_range")
    private Integer ipRange;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "logicalLink")
    Pbtdc pbtdc;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "logicalLink")
    List<ServiceDetails> serviceDetails;
}

