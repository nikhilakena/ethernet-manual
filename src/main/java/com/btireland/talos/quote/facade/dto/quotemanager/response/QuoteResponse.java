package com.btireland.talos.quote.facade.dto.quotemanager.response;


import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteStateType;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


public class QuoteResponse {
    private Long quoteId;
    private QuoteStateType quoteState;
    private String group;
    private List<RelatedPartyResponse> relatedParties = new ArrayList<>();
    private List<QuotePriceResponse> quotePrice;
    private List<QuoteItemResponse> quoteItems = new ArrayList<>();
    private RejectionDetailsResponse rejectionDetails;
    private String notes;

    private QuoteResponse() {
        //For Json Deserialization
    }

    public QuoteResponse(@NotNull Long quoteId, @NotNull QuoteStateType quoteState,
                         @Nullable String group,
                         @NotEmpty List<@Valid RelatedPartyResponse> relatedParties,
                         @Nullable List<@Valid QuotePriceResponse> quotePrice,
                         @Size(min = 3, max = 3) List<@Valid QuoteItemResponse> quoteItems,
                         @Nullable RejectionDetailsResponse rejectionDetails,@Nullable String notes) {
        this.quoteId = quoteId;
        this.quoteState = quoteState;
        this.group = group;
        this.relatedParties = relatedParties;
        this.quotePrice = quotePrice;
        this.quoteItems = quoteItems;
        this.rejectionDetails = rejectionDetails;
        this.notes = notes;
    }

    @NotNull
    public Long getQuoteId() {
        return quoteId;
    }

    @NotNull
    public QuoteStateType getQuoteState() {
        return quoteState;
    }

    @Nullable
    public String getGroup() {
        return group;
    }

    @NotEmpty
    public List<@Valid RelatedPartyResponse> getRelatedParties() {
        return relatedParties;
    }

    @Nullable
    @Valid
    public List<QuotePriceResponse> getQuotePrice() {
        return quotePrice;
    }

    @Size(min = 3, max = 3)
    public List<@Valid QuoteItemResponse> getQuoteItems() {
        return quoteItems;
    }

    @Nullable
    public RejectionDetailsResponse getRejectionDetails() {
        return rejectionDetails;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "QuoteResponse{" +
                "quoteId=" + quoteId +
                ", quoteState=" + quoteState +
                ", group=" + group +
                ", relatedParties=" + relatedParties +
                ", quotePrice=" + quotePrice +
                ", quoteItems=" + quoteItems +
                ", rejectionDetails=" + rejectionDetails +
                ", notes='" + notes + '\'' +
                '}';
    }
}
