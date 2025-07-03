package com.btireland.talos.quote.facade.connector.rest.quotemanager;

import static com.btireland.talos.quote.facade.base.constant.RestMapping.GROUP_PATH_VARIABLE;
import static com.btireland.talos.quote.facade.base.constant.RestMapping.QUOTE_ID_PATH_VARIABLE;

import com.btireland.talos.core.common.rest.exception.checked.TalosBadRequestException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.quote.facade.base.constant.RestMapping;
import com.btireland.talos.quote.facade.config.QuoteManagerFeignConfiguration;
import com.btireland.talos.quote.facade.dto.quotemanager.request.CreateQuoteRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.offlinepricing.OfflinePricingNotificationRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.offlinepricing.SearchQuoteRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.offlinepricing.UpdateQuoteRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.response.CreateQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.email.QuoteGroupEmailResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteGroupResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.SearchQuoteResponse;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "quotemanager", url = "${application.talos.quotemanager.url}", configuration =
        {QuoteManagerFeignConfiguration.class})
@Validated
public interface QuoteManagerClient {

    @PostMapping(value = RestMapping.CREATE_QUOTE, consumes = MediaType.APPLICATION_JSON_VALUE)
    CreateQuoteResponse createQuote(@Valid @RequestBody CreateQuoteRequest createQuoteRequest) throws TalosBadRequestException,
            TalosNotFoundException;

    @GetMapping(value = RestMapping.QUOTE_MANAGER_EMAIL_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    QuoteGroupEmailResponse getQuoteGroupEmail(@PathVariable(GROUP_PATH_VARIABLE) Long quoteGroupId) throws TalosNotFoundException;

    @GetMapping(value = RestMapping.SEARCH_QUOTE, consumes = MediaType.APPLICATION_JSON_VALUE)
    SearchQuoteResponse searchQuotes(@SpringQueryMap SearchQuoteRequest searchQuoteRequest) throws TalosNotFoundException;

    @GetMapping(value = RestMapping.GET_QUOTE_DETAILS)
    GetQuoteResponse getQuoteDetails(@PathVariable(QUOTE_ID_PATH_VARIABLE) Long quoteId) throws TalosNotFoundException;

    @PutMapping(value = RestMapping.UPDATE_QUOTE_NOTIFICATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateQuoteNotification(@Valid OfflinePricingNotificationRequest notificationRequest,
                              @PathVariable Long quoteGroupId) throws TalosNotFoundException,TalosBadRequestException;

    @PutMapping(value = RestMapping.UPDATE_QUOTE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateQuoteForUserTask(@Valid UpdateQuoteRequest updateQuoteRequest) throws TalosNotFoundException;

    @GetMapping(value = RestMapping.GET_QUOTE_GROUP_DETAILS, consumes = MediaType.APPLICATION_JSON_VALUE)
    GetQuoteGroupResponse getQuoteGroupDetails(@PathVariable(GROUP_PATH_VARIABLE) Long groupId) throws TalosNotFoundException;
}
