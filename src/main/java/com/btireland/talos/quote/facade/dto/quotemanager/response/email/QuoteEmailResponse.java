package com.btireland.talos.quote.facade.dto.quotemanager.response.email;

import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteStateType;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuotePriceResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.RejectionDetailsResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class QuoteEmailResponse {

    private Long quoteId;

    private QuoteStateType quoteState;

    private Integer term;

    private QuoteEmailAEndResponse aEndResponse;

    private QuoteEmailLogicalResponse logicalResponse;

    private String bEndHandOverNode;

    private List<QuotePriceResponse> quotePriceResponses = new ArrayList<>();

    private RejectionDetailsResponse rejectionDetails;

    private String notes;

    private String delayReason;

    private ConnectionType connectionType;

    private String group;

    private QuoteEmailResponse(){
        //For Json Deserialization
    }

    public QuoteEmailResponse(@NotNull Long quoteId, @NotNull QuoteStateType quoteState, @NotNull Integer term,
        @NotNull QuoteEmailAEndResponse aEndResponse, @NotNull QuoteEmailLogicalResponse logicalResponse,
        @NotBlank String bEndHandOverNode, @Nullable List<@Valid QuotePriceResponse> quotePriceResponses,
        @Nullable RejectionDetailsResponse rejectionDetails,
        @Nullable String notes, @Nullable String delayReason, @Nullable ConnectionType connectionType,
        @Nullable String group) {
        this.quoteId = quoteId;
        this.quoteState = quoteState;
        this.term = term;
        this.aEndResponse = aEndResponse;
        this.logicalResponse = logicalResponse;
        this.bEndHandOverNode = bEndHandOverNode;
        this.quotePriceResponses = quotePriceResponses;
        this.rejectionDetails = rejectionDetails;
        this.notes = notes;
        this.delayReason = delayReason;
        this.connectionType = connectionType;
        this.group = group;
    }

    @NotNull
    public Long getQuoteId() {
        return quoteId;
    }

    @NotNull
    public QuoteStateType getQuoteState() {
      return quoteState;
    }

    @NotNull
    public Integer getTerm() {
        return term;
    }

    @NotNull
    public QuoteEmailAEndResponse getaEndResponse() {
        return aEndResponse;
    }

    @NotNull
    public QuoteEmailLogicalResponse getLogicalResponse() {
        return logicalResponse;
    }

    @NotBlank
    public String getbEndHandOverNode() {
        return bEndHandOverNode;
    }

    @NotNull
    public List<@Valid QuotePriceResponse> getQuotePriceResponses() {
        return quotePriceResponses;
    }

    @Nullable
    public RejectionDetailsResponse getRejectionDetails() {
        return rejectionDetails;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    @Nullable
    public String getDelayReason() {
        return delayReason;
    }

    @Nullable
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @Nullable
    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
      return "QuoteEmailResponse{" +
          "quoteId=" + quoteId +
          ", quoteState=" + quoteState +
          ", term=" + term +
          ", aEndResponse=" + aEndResponse +
          ", logicalResponse=" + logicalResponse +
          ", bEndHandOverNode='" + bEndHandOverNode + '\'' +
          ", quotePriceResponses=" + quotePriceResponses +
          ", rejectionDetails=" + rejectionDetails +
          ", notes='" + notes + '\'' +
          ", delayReason'" + delayReason + '\'' +
          ", connectionType" + connectionType +
          ", group" + group +
          '}';
    }
}
