package com.btireland.talos.ethernet.engine.dto;

import com.btireland.talos.ethernet.engine.util.ContactTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessInstallDTO {

    private Long id;

    private String siteInduction;

    private Boolean siteMethodInsuranceCert;

    private Boolean commsRoomReadyForDelivery;

    private Set<ContactDTO> contacts;

    public ContactDTO getMainContact(){
        return contacts.stream().filter(contact->contact.getRole().equalsIgnoreCase(ContactTypes.MAIN_CONTACT.getContactOwner()))
                .findFirst().orElse(null);
    }

    public ContactDTO getSecondaryContact(){
        return contacts.stream().filter(contact->contact.getRole().equalsIgnoreCase(ContactTypes.SECONDARY_CONTACT.getContactOwner()))
                .findFirst().orElse(null);

    }

    public ContactDTO getBuildingManager(){
        return contacts.stream().filter(contact->contact.getRole().equalsIgnoreCase(ContactTypes.BUILDING_MANAGER.getContactOwner()))
                .findFirst().orElse(null);
    }

    public ContactDTO getLandlord(){
        return contacts.stream().filter(contact->contact.getRole().equalsIgnoreCase(ContactTypes.LANDLORD.getContactOwner()))
                .findFirst().orElse(null);

    }
}
