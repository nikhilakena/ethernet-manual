package com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing;

import com.btireland.talos.quote.facade.base.enumeration.internal.RecurringChargePeriodType;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class QuoteOfflinePricingResponse {

    private Long quoteId;

    private String eircode;

    private RecurringChargePeriodType recurringChargePeriod;

    private String group;

    private QuoteOfflinePricingResponse() {
    } //For json deserialization

    public QuoteOfflinePricingResponse(@NotNull Long quoteId, @NotBlank String eircode,
                                       @NotNull RecurringChargePeriodType recurringChargePeriod,
                                       @Nullable String group) {
        this.quoteId = quoteId;
        this.eircode = eircode;
        this.recurringChargePeriod = recurringChargePeriod;
        this.group = group;
    }

    @NotNull
    public Long getQuoteId() {
        return quoteId;
    }

    @NotBlank
    public String getEircode() {
        return eircode;
    }

    @NotNull
    public RecurringChargePeriodType getRecurringChargePeriod() {
        return recurringChargePeriod;
    }

    @Nullable
    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "QuoteOfflinePricingResponse{" +
                "quoteId=" + quoteId +
                ", eircode='" + eircode + '\'' +
                ", recurringChargePeriod=" + recurringChargePeriod +
                ", group=" + group +
                '}';
    }
}
