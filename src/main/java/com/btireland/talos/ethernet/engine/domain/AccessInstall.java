package com.btireland.talos.ethernet.engine.domain;

import com.btireland.talos.ethernet.engine.util.ContactTypes;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "access_install")
public class AccessInstall {

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

    @Column(name = "site_induction")
    @Size(max = 150)
    private String siteInduction;

    @Column(name = "method_insurance_cert")
    private Boolean siteMethodInsuranceCert;

    @Column(name = "comms_room_ready_for_delivery")
    private Boolean commsRoomReadyForDelivery;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "access_id", referencedColumnName = "id", nullable = false)
    private Access access;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "accessInstall")
    private List<Contact> contacts;

    private Contact lookupContactByType(ContactTypes contactType){
        return getContacts() != null ? getContacts().stream().filter(contact->contact.getRole().equalsIgnoreCase(contactType.getContactOwner()))
                .findFirst().orElse(null) : null;
    }

    public Contact getMainSiteContact(){
        return lookupContactByType(ContactTypes.MAIN_CONTACT);
    }

    public Contact getSecondarySiteContact(){
        return lookupContactByType(ContactTypes.SECONDARY_CONTACT);
    }

    public Contact getBuildingManagerContact(){
        return lookupContactByType(ContactTypes.BUILDING_MANAGER);
    }

    public Contact getLandlordContact(){
        return lookupContactByType(ContactTypes.LANDLORD);
    }

    public void addContact(Contact contactDetails){
        if(contacts==null){
            contacts = new ArrayList<>();
        }
        contacts.add(contactDetails);
    }

}
