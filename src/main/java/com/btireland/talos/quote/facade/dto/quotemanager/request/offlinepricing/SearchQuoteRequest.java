package com.btireland.talos.quote.facade.dto.quotemanager.request.offlinepricing;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class SearchQuoteRequest {

    private List<Long> quoteGroupIdList = new ArrayList<>();

    private SearchQuoteRequest() {
        //For json serialization
    }

    public SearchQuoteRequest(@NotEmpty List<Long> quoteGroupIdList) {
        this.quoteGroupIdList = quoteGroupIdList;
    }

    @NotEmpty
    public List<Long> getQuoteGroupIdList() {
        return quoteGroupIdList;
    }

    @Override
    public String toString() {
        return "SearchQuoteRequest{" +
                "quoteGroupIdList=" + quoteGroupIdList +
                '}';
    }
}
