package com.btireland.talos.quote.facade.dto.quotemanager.response;

import com.btireland.talos.quote.facade.base.enumeration.internal.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class RelatedPartyResponse {
    private String name;
    private Role role;

    private RelatedPartyResponse() {
    }//For Json Deserialization

    public RelatedPartyResponse(@NotBlank String name, @NotNull Role role) {
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
        return "RelatedPartyResponse{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
