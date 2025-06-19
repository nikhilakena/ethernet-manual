package com.btireland.talos.ethernet.engine.domain;



import com.btireland.talos.ethernet.engine.util.Color;
import com.btireland.talos.ethernet.engine.util.Status;
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
@Table(name = "intervention_details")
public class InterventionDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "create_timestamp", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private Orders orders;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(name = "notes")
    @Size(max = 255)
    private String notes;

    @Column(name = "workflow")
    @Size(max = 64)
    private String workflow;

    @Column(name = "agent")
    @Size(max = 32)
    private String agent;

    @Column(name = "closing_agent")
    @Size(max = 32)
    private String closingAgent;

    @Column(name = "closing_notes")
    @Size(max = 255)
    private String closingNotes;

    public boolean isOpen() {
        return status != null && Status.OPEN.equals(status);
    }

}
