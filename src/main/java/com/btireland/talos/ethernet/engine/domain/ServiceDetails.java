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
@Table(name = "service_details")
public class ServiceDetails {

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

    @Column(name = "service_name")
    @Size(max = 20)
    private String serviceName;

    @Column(name = "service_value")
    @Size(max = 20)
    private String serviceValue;

    @Column(name = "action", nullable = false)
    private ActionFlag action;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "logical_link_id", referencedColumnName = "id")
    private LogicalLink logicalLink;

}
