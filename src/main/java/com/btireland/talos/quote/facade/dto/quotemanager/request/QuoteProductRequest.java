package com.btireland.talos.quote.facade.dto.quotemanager.request;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class QuoteProductRequest {
    private QuoteProductPlaceRequest quoteProductPlace;
    private QuoteProductConfigRequest quoteProductConfig;

    private QuoteProductRequest() {
    }//For Json Serialization

    public QuoteProductRequest(@Valid @Nullable QuoteProductPlaceRequest quoteProductPlace,
                               @Valid @NotNull QuoteProductConfigRequest quoteProductConfig) {
        this.quoteProductPlace = quoteProductPlace;
        this.quoteProductConfig = quoteProductConfig;
    }

    public QuoteProductRequest(@Valid @NotNull QuoteProductConfigRequest quoteProductConfig) {
        this.quoteProductConfig = quoteProductConfig;
    }

    @Nullable
    @Valid
    public QuoteProductPlaceRequest getQuoteProductPlace() {
        return quoteProductPlace;
    }

    @NotNull
    @Valid
    public QuoteProductConfigRequest getQuoteProductConfig() {
        return quoteProductConfig;
    }

    @Override
    public String toString() {
        return "QuoteProductRequest{" +
                "quoteProductPlace=" + quoteProductPlace +
                ", quoteProductConfig=" + quoteProductConfig +
                '}';
    }
}
