package com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing;

import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.DurationUnit;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteStateType;
import com.btireland.talos.quote.facade.base.enumeration.internal.RecurringChargePeriodType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ServiceClassType;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class GetQuoteResponse {

    private Long quoteId;

    private QuoteStateType quoteState;

    private RecurringChargePeriodType recurringChargePeriod;

    private ServiceClassType serviceClass;
    private ConnectionType connectionType;

    private Integer quoteItemTerm;

    private DurationUnit durationUnit;

    private GetQuoteAEndResponse aEndResponse;

    private GetQuoteBEndResponse bEndResponse;

    private GetQuoteLogicalResponse logicalResponse;

    private List<GetQuotePriceResponse> quotePriceResponses = new ArrayList<>();

    private GetRejectionDetailsResponse rejectionDetailsResponse;

    private String notes;

    private String group;

    //For json serialization
    public GetQuoteResponse() {
    }

    @NotNull
    public RecurringChargePeriodType getRecurringChargePeriod() {
        return recurringChargePeriod;
    }

    @NotNull
    public ServiceClassType getServiceClass() {
        return serviceClass;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @NotNull
    public Integer getQuoteItemTerm() {
        return quoteItemTerm;
    }

    @NotNull
    public DurationUnit getDurationUnit() {
        return durationUnit;
    }

    @NotNull
    public GetQuoteAEndResponse getaEndResponse() {
        return aEndResponse;
    }

    @NotNull
    public GetQuoteBEndResponse getbEndResponse() {
        return bEndResponse;
    }

    @NotNull
    public GetQuoteLogicalResponse getLogicalResponse() {
        return logicalResponse;
    }

    @NotEmpty
    public List<@Valid GetQuotePriceResponse> getQuotePriceResponses() {
        return quotePriceResponses;
    }

    @Nullable
    public GetRejectionDetailsResponse getRejectionDetailsResponse() {
        return rejectionDetailsResponse;
    }

    @Nullable
    public String getNotes() {
        return notes;
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

    private GetQuoteResponse(Builder builder) {
        quoteId = builder.quoteId;
        quoteState = builder.quoteState;
        recurringChargePeriod = builder.recurringChargePeriod;
        serviceClass = builder.serviceClass;
        connectionType = builder.connectionType;
        quoteItemTerm = builder.quoteItemTerm;
        durationUnit = builder.durationUnit;
        aEndResponse = builder.aEndResponse;
        bEndResponse = builder.bEndResponse;
        logicalResponse = builder.logicalResponse;
        quotePriceResponses = builder.quotePriceResponses;
        rejectionDetailsResponse = builder.rejectionDetailsResponse;
        notes = builder.notes;
        group = builder.group;
    }

    public static Builder getQuoteResponseBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private Long quoteId;
        private QuoteStateType quoteState;
        private RecurringChargePeriodType recurringChargePeriod;
        private ServiceClassType serviceClass;
        private ConnectionType connectionType;
        private Integer quoteItemTerm;
        private DurationUnit durationUnit;
        private GetQuoteAEndResponse aEndResponse;
        private GetQuoteBEndResponse bEndResponse;
        private GetQuoteLogicalResponse logicalResponse;
        private List<GetQuotePriceResponse> quotePriceResponses;
        private GetRejectionDetailsResponse rejectionDetailsResponse;
        private String notes;
        private String group;

        private Builder() {
        }

        public Builder withQuoteId(@NotNull Long val) {
            quoteId = val;
            return this;
        }

        public Builder withQuoteState(@NotNull QuoteStateType val) {
            quoteState = val;
            return this;
        }

        public Builder withRecurringChargePeriod(@NotNull RecurringChargePeriodType val) {
            recurringChargePeriod = val;
            return this;
        }

        public Builder withServiceClass(@NotNull ServiceClassType val) {
            serviceClass = val;
            return this;
        }

        public Builder withConnectionType(@NotNull ConnectionType val) {
            connectionType = val;
            return this;
        }

        public Builder withQuoteItemTerm(@NotNull Integer val) {
            quoteItemTerm = val;
            return this;
        }

        public Builder withDurationUnit(@NotNull DurationUnit val) {
            durationUnit = val;
            return this;
        }

        public Builder withEndResponse(@NotNull GetQuoteAEndResponse val) {
            aEndResponse = val;
            return this;
        }

        public Builder withEndResponse(@NotNull GetQuoteBEndResponse val) {
            bEndResponse = val;
            return this;
        }

        public Builder withLogicalResponse(@NotNull GetQuoteLogicalResponse val) {
            logicalResponse = val;
            return this;
        }

        public Builder withQuotePriceResponses(@NotEmpty List<GetQuotePriceResponse> val) {
            quotePriceResponses = val;
            return this;
        }

        public Builder withRejectionDetailsResponse(@Nullable GetRejectionDetailsResponse val) {
            rejectionDetailsResponse = val;
            return this;
        }

        public Builder withNotes(@Nullable String val) {
            notes = val;
            return this;
        }

        public Builder withGroup(@Nullable String val) {
            group = val;
            return this;
        }

        public GetQuoteResponse build() {
            return new GetQuoteResponse(this);
        }
    }

    @Override
    public String toString() {
        return "GetQuoteResponse{" +
            "quoteId=" + quoteId +
            ", quoteState=" + quoteState +
            ", recurringChargePeriod=" + recurringChargePeriod +
            ", serviceClass=" + serviceClass +
            ", connectionType=" + connectionType +
            ", quoteItemTerm=" + quoteItemTerm +
            ", durationUnit=" + durationUnit +
            ", aEndResponse=" + aEndResponse +
            ", bEndResponse=" + bEndResponse +
            ", logicalResponse=" + logicalResponse +
            ", quotePriceResponses=" + quotePriceResponses +
            ", rejectionDetailsResponse=" + rejectionDetailsResponse +
            ", notes='" + notes + '\'' +
            ", group='" + group + '\'' +
            '}';
    }
}
