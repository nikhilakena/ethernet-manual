package com.btireland.talos.ethernet.engine.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NotNull
public class OaoDetailDTO {
    private String legalEntityCode;
    @NotNull
    private String name;
    private Boolean enableKciTCBanner;
    private List<String> handovers;
}