package com.btireland.talos.ethernet.engine.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.JOINED)
public class Orders {

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

    @Column(name = "wag_order_id", updatable = false)
    private Long wagOrderId;

    @Column(name = "order_number", updatable = false)
    @Size(max = 32)
    private String orderNumber;

    @Column(name = "tracking_order_reference")
    @Size(max = 50)
    private String internalTrackingOrderReference;

    @Column(name = "oao" ,nullable = false)
    @Size(max = 32)
    private String oao;

    @Column(name = "obo")
    @Size(max = 32)
    private String obo;

    @Column(name = "data_contract_name",nullable = false)
    @Size(max = 32)
    private String dataContract;

    @Column(name = "originator_code",nullable = false)
    @Size(max = 32)
    private String originatorCode;

    @Column(name = "transaction_id",nullable = false)
    @Size(max = 32)
    private String resellerTransactionId;

    @Column(name = "date_time_stamp",nullable = false)
    private LocalDateTime resellerOrderRequestDateTime;

    @Column(name = "application_date")
    private LocalDate applicationDate;

    @Column(name = "due_validation_date")
    private LocalDate dueValidationDate;

    @Column(name = "due_completion_date")
    private LocalDate dueCompletionDate;

    @Column(name = "estimated_completion_date")
    private LocalDate estimatedCompletionDate;

    @Column(name = "reforecast_due_date")
    private LocalDate reforecastDueDate;

    @Column(name="date_received_by_delivery")
    private LocalDateTime dateReceivedByDelivery;

    @Column(name = "operator_name")
    @Size(max = 32)
    private String operatorName;

    @Column(name = "operator_code")
    @Size(max = 32)
    private String operatorCode;

    @Column(name = "service_class")
    @Size(max = 20)
    private String serviceClass;

    @Column(name = "connection_type")
    @Size(max = 32)
    private String connectionType;

    @Column(name = "bt_group_ref")
    @Size(max = 32)
    private String btGroupRef;

    @Column(name = "service")
    @Size(max = 32)
    private String serviceType;

    @Column(name = "service_code")
    @Size(max = 20)
    private String serviceCode;

    @Column(name = "account_number")
    @Size(max = 20)
    private String accountNumber;

    @Column(name = "organisation_name")
    @Size(max = 50)
    private String organisationName;

    @Column(name = "project_key")
    @Size(max = 32)
    private String projectKey;

    @Column(name = "ref_quote_item_id")
    private Long refQuoteItemId;

    @Column(name = "wasset")
    @Size(max = 32)
    private String wasset;

    @Column(name = "status")
    @Size(max = 32)
    private String orderStatus;

    @Setter(AccessLevel.NONE)
    @Column(name = "workflow")
    @Size(max = 64)
    private String workflowStatus;

    @Column(name = "intervention_flag", columnDefinition = "BIT")
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Boolean interventionFlag;

    @Column(name = "paused_on_error_flag", columnDefinition = "BIT")
    private Boolean pausedOnErrorFlag;

    @Column(name = "last_not")
    @Size(max = 8)
    private String lastNotificationType;

    @Column(name = "notes")
    @Size(max = 1500)
    private String notes;

    @Column(name="notes_received_date")
    private LocalDateTime notesReceivedDate;

    @Column(name = "status_text")
    @Size(max = 1500)
    private String statusText;

    @Embedded
    SupplierOrder supplierOrder;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "orders")
    private List<InterventionDetails> interventionDetails;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "orders")
    private RejectionDetails rejectionDetails;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "orders")
    private List<Contact> contacts;

    @Version
    private Integer version;

    public Boolean getInterventionFlag() {
        Boolean flag = false;
        if (interventionDetails != null && !interventionDetails.isEmpty()) {
            for (InterventionDetails intDet : interventionDetails) {
                if (intDet.getStatus() != null && intDet.getStatus().getLabel().equalsIgnoreCase("OPEN")) {
                    flag = true;
                    break;
                }


            }
        }
        return flag;
    }

}

