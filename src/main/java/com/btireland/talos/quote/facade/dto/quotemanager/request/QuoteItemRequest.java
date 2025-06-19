package com.btireland.talos.quote.facade.dto.quotemanager.request;

import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteItemNameType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class QuoteItemRequest {

    private QuoteItemNameType name;
    private Integer quoteItemTerm;
    private QuoteProductRequest quoteProduct;

    public QuoteItemRequest(@NotNull QuoteItemNameType name, @NotNull Integer quoteItemTerm,
                            @Valid @NotNull QuoteProductRequest quoteProduct) {
        this.name = name;
        this.quoteItemTerm = quoteItemTerm;
        this.quoteProduct = quoteProduct;
    }

    private QuoteItemRequest() {
    }//For Json Serialization

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
    public QuoteProductRequest getQuoteProduct() {
        return quoteProduct;
    }

    @Override
    public String toString() {
        return "QuoteItemRequest{" +
                "name='" + name + '\'' +
                ", quoteItemTerm=" + quoteItemTerm +
                ", quoteProduct=" + quoteProduct +
                '}';
    }
}
