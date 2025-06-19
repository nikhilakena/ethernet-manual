package com.btireland.talos.quote.facade.dto.quotemanager.response;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class QuoteProductResponse {
    private QuoteProductPlaceResponse quoteProductPlace;
    private QuoteProductConfigResponse quoteProductConfig;

    private QuoteProductResponse() {
    }//For Json Deserialization

    public QuoteProductResponse(@Valid @Nullable QuoteProductPlaceResponse quoteProductPlace,
                                @Valid @NotNull QuoteProductConfigResponse quoteProductConfig) {
        this.quoteProductPlace = quoteProductPlace;
        this.quoteProductConfig = quoteProductConfig;
    }

    @Nullable
    @Valid
    public QuoteProductPlaceResponse getQuoteProductPlace() {
        return quoteProductPlace;
    }

    @NotNull
    @Valid
    public QuoteProductConfigResponse getQuoteProductConfig() {
        return quoteProductConfig;
    }

    @Override
    public String toString() {
        return "QuoteProductResponse{" +
                "quoteProductPlace=" + quoteProductPlace +
                ", quoteProductConfig=" + quoteProductConfig +
                '}';
    }
}
