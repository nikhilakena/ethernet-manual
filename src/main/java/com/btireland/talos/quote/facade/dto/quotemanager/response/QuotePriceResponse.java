package com.btireland.talos.quote.facade.dto.quotemanager.response;

import com.btireland.talos.quote.facade.base.enumeration.internal.PriceType;
import com.btireland.talos.quote.facade.base.enumeration.internal.RecurringChargePeriodType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class QuotePriceResponse {
    private RecurringChargePeriodType recurringChargePeriod;
    private PriceType priceType;
    private String quotePrice;

    private QuotePriceResponse() {
    } //For Json Deserialization

    public QuotePriceResponse(@NotNull RecurringChargePeriodType recurringChargePeriod, @NotNull PriceType priceType,
                              @NotBlank String quotePrice) {
        this.recurringChargePeriod = recurringChargePeriod;
        this.priceType = priceType;
        this.quotePrice = quotePrice;
    }

    @NotNull
    public RecurringChargePeriodType getRecurringChargePeriod() {
        return recurringChargePeriod;
    }

    @NotNull
    public PriceType getPriceType() {
        return priceType;
    }

    @NotBlank
    public String getQuotePrice() {
        return quotePrice;
    }

    @Override
    public String toString() {
        return "QuotePriceResponse{" +
                "recurringChargePeriod=" + recurringChargePeriod +
                ", priceType=" + priceType +
                ", quotePrice='" + quotePrice + '\'' +
                '}';
    }
}
