package com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing;

import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ServiceClassType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuoteGroupOfflinePricingResponse {

    private Long quoteGroupId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime quoteDate;

    private List<QuoteOfflinePricingResponse> quotes = new ArrayList<>();

    private ServiceClassType serviceClass;
    private ConnectionType connectionType;

    private String resellerTransactionId;

    private String projectIdentifier;

    private String delayReason;


    private QuoteGroupOfflinePricingResponse() {
    } //For json deserialization

    public QuoteGroupOfflinePricingResponse(@NotNull Long quoteGroupId, @NotNull LocalDateTime quoteDate,
                                            @NotEmpty List<@Valid QuoteOfflinePricingResponse> quotes,
                                            @NotNull ServiceClassType serviceClass,
                                            @NotNull ConnectionType connectionType,
                                            @NotBlank String resellerTransactionId,
                                            @NotBlank String projectIdentifier, @Nullable String delayReason) {
        this.quoteGroupId = quoteGroupId;
        this.quoteDate = quoteDate;
        this.quotes = quotes;
        this.serviceClass = serviceClass;
        this.connectionType = connectionType;
        this.resellerTransactionId = resellerTransactionId;
        this.projectIdentifier = projectIdentifier;
        this.delayReason = delayReason;
    }

    @NotNull
    public Long getQuoteGroupId() {
        return quoteGroupId;
    }

    @NotNull
    public LocalDateTime getQuoteDate() {
        return quoteDate;
    }

    @NotEmpty
    public List<@Valid QuoteOfflinePricingResponse> getQuotes() {
        return quotes;
    }

    @NotNull
    public ServiceClassType getServiceClass() {
        return serviceClass;
    }

    @NotNull
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @NotBlank
    public String getResellerTransactionId() {
        return resellerTransactionId;
    }

    @NotBlank
    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    @Nullable
    public String getDelayReason() {
        return delayReason;
    }

    @Override
    public String toString() {
        return "QuoteGroupOfflinePricingResponse{" +
                "quoteGroupId=" + quoteGroupId +
                ", quoteDate=" + quoteDate +
                ", quotes=" + quotes +
                ", serviceClass=" + serviceClass +
                ", connectionType=" + connectionType +
                ", resellerTransactionId='" + resellerTransactionId + '\'' +
                ", projectIdentifier='" + projectIdentifier + '\'' +
                ", resellerTransactionId='" + resellerTransactionId + '\'' +
                ", projectIdentifier='" + projectIdentifier + '\'' +
                ", delayReason='" + delayReason + '\'' +
                '}';
    }
}
