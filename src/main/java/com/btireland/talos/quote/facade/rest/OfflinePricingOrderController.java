package com.btireland.talos.quote.facade.rest;

import static com.btireland.talos.quote.facade.base.constant.FeatureFlagConstants.QUOTE_FACADE_ENABLED;
import static com.btireland.talos.quote.facade.base.constant.RestMapping.OFFLINE_PRICING_FETCH_ALL_QUOTE_PATH;
import static com.btireland.talos.quote.facade.base.constant.RestMapping.OFFLINE_PRICING_NOTIFICATION_PATH;
import static com.btireland.talos.quote.facade.base.constant.RestMapping.OFFLINE_PRICING_SEARCH_QUOTE_PATH;
import static com.btireland.talos.quote.facade.base.constant.RestMapping.ORDER_NUMBER_PATH_VARIABLE;
import com.btireland.talos.core.common.rest.exception.checked.TalosBadRequestException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteOrderResponse;
import com.btireland.talos.quote.facade.base.constant.RestMapping;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteNotificationRequest;
import com.btireland.talos.quote.facade.service.api.OfflinePricingAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;

@RestController
@ConditionalOnProperty(name = QUOTE_FACADE_ENABLED,havingValue = "true")
@Tag(name = "Offline Pricing Controller", description = "Controller to fetch quotes for offline pricing")
@RequestMapping(value = RestMapping.OFFLINE_PRICING_BASE_PATH)
public class OfflinePricingOrderController {

    private final OfflinePricingAPI offlinePricingAPI;

    public OfflinePricingOrderController(OfflinePricingAPI offlinePricingAPI) {
        this.offlinePricingAPI = offlinePricingAPI;
    }

    @Operation(summary = "search for qbtdc order", description = "search all qbtdc orders by order number",
            responses = {
                    @ApiResponse(responseCode = "500", description = "Technical error while processing the request"),
                    @ApiResponse(responseCode = "404", description = "Quote data not found"),
                    @ApiResponse(responseCode = "200", description = "Successfully processed the quote request")})
    @GetMapping(path = OFFLINE_PRICING_SEARCH_QUOTE_PATH)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<QuoteOrderResponse>> searchOfflinePricingOrder(@RequestParam("reference") String orderNumber) throws TalosNotFoundException {
        return ResponseEntity.ok(offlinePricingAPI.searchQuote(orderNumber));
    }

    @Operation(summary = "fetch all offline pricing qbtdc orders", description = "fetch all qbtdc orders for offline " +
            "pricing",
            responses = {
                    @ApiResponse(responseCode = "500", description = "Technical error while processing the request"),
                    @ApiResponse(responseCode = "404", description = "Quote data not found"),
                    @ApiResponse(responseCode = "200", description = "Successfully processed the quote request")})
    @GetMapping(path = OFFLINE_PRICING_FETCH_ALL_QUOTE_PATH)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<QuoteOrderResponse>> getAllOfflinePricingOrders(Pageable pageable) throws TalosNotFoundException {
        return ResponseEntity.ok(offlinePricingAPI.fetchAllQuotes(pageable));
    }

    @Operation(summary = "process offline pricing notification", description = "process incoming notification for " +
            "quote",
            responses = {
                    @ApiResponse(responseCode = "500", description = "Technical error while processing the request"),
                    @ApiResponse(responseCode = "404", description = "Quote data not found"),
                    @ApiResponse(responseCode = "400", description = "Request validation error"),
                    @ApiResponse(responseCode = "204", description = "Successfully processed the quote request")})
    @PutMapping(path = OFFLINE_PRICING_NOTIFICATION_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void processNotification(@PathVariable(ORDER_NUMBER_PATH_VARIABLE) String orderNumber,
                                    @PathVariable String oao, @RequestBody @Valid QuoteNotificationRequest notification) throws TalosNotFoundException, TalosBadRequestException {

        offlinePricingAPI.processNotification(notification,orderNumber,oao);

    }
}
