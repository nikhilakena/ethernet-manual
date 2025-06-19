package com.btireland.talos.ethernet.engine.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parked_notifications")
public class ParkedNotifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "received_timestamp", nullable = false)
    private LocalDateTime receivedAt;

    @UpdateTimestamp
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Orders orders;

    @Column(name = "supplier_not_id")
    @Size(max = 100)
    private String supplierNotificationId;

    @Column(name = "notification_type")
    @Size(max = 32)
    private String notificationType;

    @Column(name = "processed_status")
    @Size(max = 1)
    private String processedStatus;

    @Lob
    @Column(name = "supplier_notification", columnDefinition="BLOB")
    private byte[] supplierNotification;

}
