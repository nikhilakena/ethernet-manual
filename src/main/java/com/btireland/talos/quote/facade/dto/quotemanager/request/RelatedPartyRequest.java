package com.btireland.talos.quote.facade.dto.quotemanager.request;

import com.btireland.talos.quote.facade.base.enumeration.internal.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class RelatedPartyRequest {
    private String name;
    private Role role;

    private RelatedPartyRequest() {
    }//For Json Serialization

    public RelatedPartyRequest(@NotBlank String name, @NotNull Role role) {
        this.name = name;
        this.role = role;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    @NotNull
    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "RelatedPartyRequest{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
