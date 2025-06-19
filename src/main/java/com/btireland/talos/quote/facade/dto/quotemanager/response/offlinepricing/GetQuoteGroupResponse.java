package com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing;

import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ServiceClassType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class GetQuoteGroupResponse {

    private Long quoteGroupId;

    private String projectIdentifier;

    private ServiceClassType serviceClass;
    private ConnectionType connectionType;

    private String resellerTransactionId;

    private List<GetQuoteResponse> quotes = new ArrayList<>();

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime quoteDate;

    private List<GetRelatedPartyResponse> relatedPartyResponses = new ArrayList<>();

    private GetQuoteGroupResponse() {
    } //For json serialization

    public GetQuoteGroupResponse(@NotNull Long quoteGroupId, @Nullable String projectIdentifier,
                                 @NotNull ServiceClassType serviceClass, @NotNull ConnectionType connectionType,
                                 @NotBlank String resellerTransactionId, @NotNull LocalDateTime quoteDate,
                                 @NotEmpty List<@Valid GetQuoteResponse> quotes,
                                 @NotEmpty List<GetRelatedPartyResponse> relatedPartyResponses) {
        this.quoteGroupId = quoteGroupId;
        this.projectIdentifier = projectIdentifier;
        this.serviceClass = serviceClass;
        this.connectionType = connectionType;
        this.resellerTransactionId = resellerTransactionId;
        this.quoteDate = quoteDate;
        this.quotes = quotes;
        this.relatedPartyResponses = relatedPartyResponses;
    }

    @NotNull
    public Long getQuoteGroupId() {
        return quoteGroupId;
    }

    @NotEmpty
    public List<@Valid GetQuoteResponse> getQuotes() {
        return quotes;
    }

    @Nullable
    public String getProjectIdentifier() {
        return projectIdentifier;
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

    @NotNull
    public LocalDateTime getQuoteDate() {
        return quoteDate;
    }

    @NotEmpty
    public List<@Valid GetRelatedPartyResponse> getRelatedPartyResponses() {
        return relatedPartyResponses;
    }

    @Override
    public String toString() {
        return "GetQuoteGroupResponse{" +
                "quoteGroupId=" + quoteGroupId +
                ", projectIdentifier='" + projectIdentifier + '\'' +
                ", serviceClass=" + serviceClass +
                ", connectionType=" + connectionType +
                ", resellerTransactionId='" + resellerTransactionId + '\'' +
                ", quotes=" + quotes +
                ", quoteDate=" + quoteDate +
                ", relatedPartyResponses=" + relatedPartyResponses +
                '}';
    }
}
