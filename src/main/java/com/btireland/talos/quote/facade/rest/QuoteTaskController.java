package com.btireland.talos.quote.facade.rest;

import static com.btireland.talos.quote.facade.base.constant.FeatureFlagConstants.QUOTE_FACADE_ENABLED;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.quote.facade.base.constant.RestMapping;
import com.btireland.talos.quote.facade.dto.businessconsole.OfflinePricingTaskResponse;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemTask;
import com.btireland.talos.quote.facade.service.api.OfflinePricingAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@ConditionalOnProperty(name = QUOTE_FACADE_ENABLED,havingValue = "true")
@Tag(name= "Quote Task Controller", description = "Controller to update Quote Task")
@RequestMapping(value = RestMapping.QUOTE_TASK_BASE_PATH)
public class QuoteTaskController {

    private OfflinePricingAPI offlinePricingAPI;

    public QuoteTaskController (OfflinePricingAPI offlinePricingAPI) {
        this.offlinePricingAPI = offlinePricingAPI;
    }

    @Operation(summary = "change task assignee  ", description = "change the assignee for the quote item task",
        responses = {
        @ApiResponse(responseCode = "500", description = "Technical error while processing the request"),
        @ApiResponse(responseCode = "204", description = "Successfully processed the change assignee request")})

    @PutMapping(path = RestMapping.QUOTE_TASK_CHANGE_ASSIGNEE_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeAssignee(@PathVariable String taskId, @RequestBody QuoteItemTask taskDto) {
        offlinePricingAPI.changeAssignee(taskId, taskDto.getAssignee());
    }

    @Operation(summary = "fetch quote details for a task", description = "Get quote details for a user task",
            responses = {
                    @ApiResponse(responseCode = "500", description = "Technical error while processing the request"),
                    @ApiResponse(responseCode = "404", description = "Quote Not Found"),
                    @ApiResponse(responseCode = "200", description = "Successfully processed the quote request")})
    @GetMapping(path = RestMapping.OFFLINE_PRICING_GET_QUOTE_PATH, produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OfflinePricingTaskResponse> getQuoteDetails(@PathVariable String taskId) throws TalosNotFoundException {
        return ResponseEntity.ok(offlinePricingAPI.getQuoteDetails(taskId));
    }
}