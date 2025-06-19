package com.btireland.talos.quote.facade.dto.quotemanager.request;

import com.btireland.talos.quote.facade.base.enumeration.internal.RecurringChargePeriodType;

import javax.validation.constraints.NotNull;


public class QuotePriceRequest {
    private RecurringChargePeriodType recurringChargePeriod;

    private QuotePriceRequest() {
    } //For Json Serialization

    public QuotePriceRequest(@NotNull RecurringChargePeriodType recurringChargePeriod) {
        this.recurringChargePeriod = recurringChargePeriod;
    }

    @NotNull
    public RecurringChargePeriodType getRecurringChargePeriod() {
        return recurringChargePeriod;
    }

    @Override
    public String toString() {
        return "QuotePriceRequest{" +
                "recurringChargePeriod='" + recurringChargePeriod + '\'' +
                '}';
    }
}
