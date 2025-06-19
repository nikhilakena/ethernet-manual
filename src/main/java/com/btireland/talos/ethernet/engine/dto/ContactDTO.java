package com.btireland.talos.ethernet.engine.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String number;

    private String email;

    private String role;
}
