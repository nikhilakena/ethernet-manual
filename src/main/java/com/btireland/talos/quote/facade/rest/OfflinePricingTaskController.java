package com.btireland.talos.quote.facade.rest;

import static com.btireland.talos.quote.facade.base.constant.FeatureFlagConstants.QUOTE_FACADE_ENABLED;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.quote.facade.base.constant.RestMapping;
import com.btireland.talos.quote.facade.dto.businessconsole.DefaultQuoteTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemAcceptTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemNoBidTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemRejectTask;
import com.btireland.talos.quote.facade.service.api.OfflinePricingAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
//@ConditionalOnProperty(name = QUOTE_FACADE_ENABLED,havingValue = "true")
@Tag(name = "Offline Pricing Controller", description = "Controller to handle OfflinePricing Task")
@RequestMapping(value = {RestMapping.OFFLINE_PRICING_TASK_BASE_PATH})
public class OfflinePricingTaskController {

  private final OfflinePricingAPI offlinePricingAPI;

  public OfflinePricingTaskController(OfflinePricingAPI offlinePricingAPI) {
    this.offlinePricingAPI = offlinePricingAPI;
  }

  @Operation(summary = "accept quote", description = "Accept the quote item task", responses = {
      @ApiResponse(responseCode = "500", description = "Technical error while processing the request"),
      @ApiResponse(responseCode = "404", description = "Quote data not found"),
      @ApiResponse(responseCode = "200", description = "Successfully processed the accept task")})
  @PutMapping(path = RestMapping.OFFLINE_PRICING_TASK_ACCEPT_PATH)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<QuoteItemAcceptTask> acceptQuote(@PathVariable String taskId,
      @RequestBody QuoteItemAcceptTask quoteItemAcceptTask) throws TalosNotFoundException {
    offlinePricingAPI.acceptQuote(quoteItemAcceptTask, taskId);
    return ResponseEntity.of(Optional.of(quoteItemAcceptTask));
  }

  @Operation(summary = "reject quote", description = "reject the quote item task", responses = {
      @ApiResponse(responseCode = "500", description = "Technical error while processing the request"),
      @ApiResponse(responseCode = "404", description = "Quote data not found"),
      @ApiResponse(responseCode = "200", description = "Successfully processed the reject task")})
  @PutMapping(path = RestMapping.OFFLINE_PRICING_TASK_REJECT_PATH)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<QuoteItemRejectTask> rejectQuote(@PathVariable String taskId,
      @RequestBody QuoteItemRejectTask quoteItemRejectTask) throws TalosNotFoundException {
    offlinePricingAPI.rejectQuote(quoteItemRejectTask, taskId);
    return ResponseEntity.of(Optional.of(quoteItemRejectTask));
  }

  @Operation(summary = "no bid quote", description = "no bid quote item task", responses = {
      @ApiResponse(responseCode = "500", description = "Technical error while processing the request"),
      @ApiResponse(responseCode = "404", description = "Quote data not found"),
      @ApiResponse(responseCode = "200", description = "Successfully processed the no bid task")})
  @PutMapping(path = RestMapping.OFFLINE_PRICING_TASK_NO_BID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<QuoteItemNoBidTask> noBidQuote(@PathVariable String taskId,
      @RequestBody QuoteItemNoBidTask quoteItemNoBidTask) throws TalosNotFoundException {
    offlinePricingAPI.noBidQuote(quoteItemNoBidTask, taskId);
    return ResponseEntity.of(Optional.of(quoteItemNoBidTask));
  }

  @Operation(summary = "update quote action", description = "update quote actions in HAL forms for " +
          "business console", responses = {@ApiResponse(responseCode = "200", description = "Successfully returns quote actions")})
  @PutMapping(path = RestMapping.OFFLINE_PRICING_TASK_QUOTE_ACTION_PATH)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<DefaultQuoteTask> updateQuoteActions(@RequestBody @Valid DefaultQuoteTask defaultQuoteTask) {
    return ResponseEntity.of(Optional.of(defaultQuoteTask));
  }

}