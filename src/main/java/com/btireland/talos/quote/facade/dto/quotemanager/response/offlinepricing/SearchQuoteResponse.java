package com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class SearchQuoteResponse {

    private List<QuoteGroupOfflinePricingResponse> quoteGroups = new ArrayList<>();

    private SearchQuoteResponse() {
    } //For json deserialization

    public SearchQuoteResponse(@NotNull List<@Valid QuoteGroupOfflinePricingResponse> quoteGroups) {
        this.quoteGroups = quoteGroups;
    }

    @NotNull
    public List<@Valid QuoteGroupOfflinePricingResponse> getQuoteGroups() {
        return quoteGroups;
    }

    @Override
    public String toString() {
        return "SearchQuoteResponse{" +
                "quotes=" + quoteGroups +
                '}';
    }
}
