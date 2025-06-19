package com.btireland.talos.ethernet.engine.domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contact")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Contact.class)
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "access_install_id")
    private AccessInstall accessInstall;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @Column(name = "first_name", nullable = false)
    @Size(max = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(max = 20)
    private String lastName;

    @Column(name = "number")
    @Size(max = 20)
    private String number;

    @Column(name = "email")
    @Size(max = 40)
    private String email;

    @Column(name = "role")
    @Size(max = 40)
    private String role;

    public String formatToString(){
        StringBuilder contactStringBuilder = new StringBuilder().append("");
        contactStringBuilder.append(this.getRole())
                .append(" : ")
                .append(StringUtils.defaultString(this.getFirstName()))
                .append(' ')
                .append(StringUtils.defaultString(this.getLastName()))
                .append(" / ")
                .append(StringUtils.defaultString(this.getEmail()))
                .append(" / ")
                .append(StringUtils.defaultString(this.getNumber()));
        return contactStringBuilder.toString();
    }
}
