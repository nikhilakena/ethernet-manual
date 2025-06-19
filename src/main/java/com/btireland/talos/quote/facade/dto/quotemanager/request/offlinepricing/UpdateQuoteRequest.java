package com.btireland.talos.quote.facade.dto.quotemanager.request.offlinepricing;

import com.btireland.talos.quote.facade.base.enumeration.internal.NetworkType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteStateType;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

public class UpdateQuoteRequest {

    private Long quoteId;

    private QuoteStateType quoteStateType;

    private NetworkType deliveryType;

    private String recurringCharge;

    private String nonRecurringCharge;

    private String etherflowRecurringCharge;

    private String etherwayRecurringCharge;

    private String rejectCode;

    private String rejectReason;

    private String notes;

    private UpdateQuoteRequest() {
        //For json serialization
    }

    public UpdateQuoteRequest(@NotNull Long quoteId, @NotNull QuoteStateType quoteStateType,
        @Nullable NetworkType deliveryType, @Nullable String recurringCharge, @Nullable String nonRecurringCharge,
        @Nullable String etherflowRecurringCharge, @Nullable String etherwayRecurringCharge, @Nullable String notes) {
        this.quoteId = quoteId;
        this.quoteStateType = quoteStateType;
        this.deliveryType = deliveryType;
        this.recurringCharge = recurringCharge;
        this.nonRecurringCharge = nonRecurringCharge;
        this.etherflowRecurringCharge = etherflowRecurringCharge;
        this.etherwayRecurringCharge = etherwayRecurringCharge;
        this.notes = notes;
    }

    public UpdateQuoteRequest(@NotNull Long quoteId, @NotNull QuoteStateType quoteStateType,
                              String rejectCode,
                              String rejectReason, String notes) {
        this.quoteId = quoteId;
        this.quoteStateType = quoteStateType;
        this.rejectCode = rejectCode;
        this.rejectReason = rejectReason;
        this.notes = notes;
    }

    public UpdateQuoteRequest(@NotNull Long quoteId, @NotNull QuoteStateType quoteStateType,
                              String notes) {
        this.quoteId = quoteId;
        this.quoteStateType = quoteStateType;
        this.notes = notes;
    }

    @NotNull
    public Long getQuoteId() {
        return quoteId;
    }

    @NotNull
    public QuoteStateType getQuoteStateType() {
        return quoteStateType;
    }

    @Nullable
    public NetworkType getDeliveryType() {
        return deliveryType;
    }

    @Nullable
    public String getRecurringCharge() {
        return recurringCharge;
    }

    @Nullable
    public String getNonRecurringCharge() {
        return nonRecurringCharge;
    }

    @Nullable
    public String getEtherflowRecurringCharge() {
        return etherflowRecurringCharge;
    }

    @Nullable
    public String getEtherwayRecurringCharge() {
        return etherwayRecurringCharge;
    }

    @Nullable
    public String getRejectCode() {
        return rejectCode;
    }

    @Nullable
    public String getRejectReason() {
        return rejectReason;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "UpdateQuoteRequest{" +
                "quoteId=" + quoteId +
                ", quoteStateType=" + quoteStateType +
                ", deliveryType=" + deliveryType +
                ", recurringCharge='" + recurringCharge + '\'' +
                ", nonRecurringCharge='" + nonRecurringCharge + '\'' +
                ", etherflowRecurringCharge='" + etherflowRecurringCharge + '\'' +
                ", etherwayRecurringCharge='" + etherwayRecurringCharge + '\'' +
                ", rejectCode='" + rejectCode + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
