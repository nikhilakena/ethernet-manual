package com.btireland.talos.ethernet.engine.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "qbtdc_email_details")
public class QbtdcEmailDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private Qbtdc qbtdc;

    @Column(name="email")
    @Size(max = 40)
    private String email;

    QbtdcEmailDetails(){}

    public QbtdcEmailDetails(Qbtdc qbtdc, @Size(max = 40) String email) {
        this.qbtdc = qbtdc;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public Qbtdc getQbtdc() {
        return qbtdc;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "QbtdcEmailDetails{" +
                "id=" + id +
                ", qbtdc.orderId=" + qbtdc.getId() +
                ", email='" + email + '\'' +
                '}';
    }
}
