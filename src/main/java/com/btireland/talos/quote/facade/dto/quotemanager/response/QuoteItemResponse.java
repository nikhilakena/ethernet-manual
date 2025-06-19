package com.btireland.talos.quote.facade.dto.quotemanager.response;


import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteItemNameType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class QuoteItemResponse {
    private QuoteItemNameType name;
    private Integer quoteItemTerm;
    private QuoteProductResponse quoteProduct;

    public QuoteItemResponse(@NotNull QuoteItemNameType name, @NotNull Integer quoteItemTerm,
                             @Valid @NotNull QuoteProductResponse quoteProduct) {
        this.name = name;
        this.quoteItemTerm = quoteItemTerm;
        this.quoteProduct = quoteProduct;
    }

    private QuoteItemResponse() {
    }//For Json Deserialization

    @NotNull
    public QuoteItemNameType getName() {
        return name;
    }

    @NotNull
    public Integer getQuoteItemTerm() {
        return quoteItemTerm;
    }

    @NotNull
    @Valid
    public QuoteProductResponse getQuoteProduct() {
        return quoteProduct;
    }

    @Override
    public String toString() {
        return "QuoteItemResponse{" +
                "name='" + name + '\'' +
                ", quoteItemTerm=" + quoteItemTerm +
                ", quoteProduct=" + quoteProduct +
                '}';
    }
}
