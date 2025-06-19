package com.btireland.talos.ethernet.engine.dto;

import com.btireland.talos.ethernet.engine.util.Color;
import com.btireland.talos.ethernet.engine.util.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Tag(name = "interventions")
public class InterventionDetailsDTO {

    public static final String DEFAULT_AGENT = "ethernet engine";
    public static final String DEFAULT_CLOSING_NOTES = "Resolved through Camunda UI";

    @Schema(description = "The id of the intervention in intervention details table", example = "1l")
    private Long id;
    @Schema(description = "The status of the intervention", example = "OPEN")
    private Status status;
    @Schema(description = "Represents the color of the intervention", example = "RED")
    private Color color;
    @Schema(description = "The notes which are added describing intervention", example = "couldn't execute activity < SPQR response unavailable for D6WAC15, with, serviceClass=NFIB")
    private String notes;
    @Schema(description="The workflow in which intervention occured",example= "Activity_ValidateSpqr")
    private String workflow;
    @Schema(description = "agent handling intervention",example = "fibre engine")
    private String agent;
    @Schema(description = "agent who closes the intervention", example="")
    private String closingAgent;
    @Schema(description = "closing notes about the intervention closed", example="")
    private String closingNotes;

    public boolean isOpen() {
        return status != null && Status.OPEN.equals(status);
    }



}
