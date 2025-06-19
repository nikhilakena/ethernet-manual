package com.btireland.talos.ethernet.engine.rest;

import com.btireland.talos.core.common.rest.exception.ApiError;
import com.btireland.talos.ethernet.engine.dto.CloseInterventionDTO;
import com.btireland.talos.ethernet.engine.dto.InterventionDetailsDTO;
import com.btireland.talos.ethernet.engine.facade.PBTDCOrderFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private PBTDCOrderFacade ordersFacade;

    public AdminController(PBTDCOrderFacade ordersFacade){


        this.ordersFacade=ordersFacade;

    }


    @Operation(
            summary = "closes intervention based on id",
            description = "Closes intervention based on the id and save agent name and closing notes ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Intervention closed"),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @PostMapping(path ="/interventions/{id}:close",  produces = MediaType.APPLICATION_JSON_VALUE)
    public void closeIntervention(@PathVariable Long id, @RequestBody CloseInterventionDTO closeInterventionDTO){

        ordersFacade.closeIntervention(id, closeInterventionDTO);

    }


    @Operation(
            summary = "Gets the the list of interventions by ID and status",
            description = "Retrieves a list of interventions for a given wag order id and status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Interventions returned"),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @GetMapping(path="/{wagOrderId}/interventions",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<InterventionDetailsDTO> getInterventionDetails(@QueryParam("status") String status, @PathVariable(value="wagOrderId") Long wagOrderId ){
        return ordersFacade.getInterventions(wagOrderId,status);
    }
}
