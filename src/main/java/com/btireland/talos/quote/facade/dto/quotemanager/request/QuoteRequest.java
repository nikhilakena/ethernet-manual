package com.btireland.talos.quote.facade.dto.quotemanager.request;

import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ServiceClassType;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


public class QuoteRequest {
    private Long externalId;
    private String projectIdentifier;
    private Boolean syncRequired;
    private Integer validFor;
    private String resellerTransactionId;
    private ServiceClassType serviceClass;
    private ConnectionType connectionType;
    private String group;
    private List<RelatedPartyRequest> relatedParties = new ArrayList<>();
    private QuotePriceRequest quotePrice;
    private List<QuoteItemRequest> quoteItems = new ArrayList<>();

    private QuoteRequest() {
    } //For Json Serialization

    public QuoteRequest(@Nullable String projectIdentifier, @NotNull Boolean syncRequired,
                        @NotBlank String resellerTransactionId,
                        @NotNull ServiceClassType serviceClass,
                        @Nullable ConnectionType connectionType,
                        @Nullable String group,
                        @NotEmpty List<@Valid RelatedPartyRequest> relatedParties,
                        @Valid @NotNull QuotePriceRequest quotePrice,
                        @Size(min = 3, max = 3) List<@Valid QuoteItemRequest> quoteItems) {
        this.projectIdentifier = projectIdentifier;
        this.syncRequired = syncRequired;
        this.resellerTransactionId = resellerTransactionId;
        this.serviceClass = serviceClass;
        this.connectionType = connectionType;
        this.group = group;
        this.relatedParties = relatedParties;
        this.quotePrice = quotePrice;
        this.quoteItems = quoteItems;
    }

    @Nullable
    public Long getExternalId() {
        return externalId;
    }

    @Nullable
    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    @NotNull
    public Boolean getSyncRequired() {
        return syncRequired;
    }

    @Nullable
    public Integer getValidFor() {
        return validFor;
    }

    @NotBlank
    public String getResellerTransactionId() {
        return resellerTransactionId;
    }

    @NotNull
    public ServiceClassType getServiceClass() {
        return serviceClass;
    }

    @Nullable
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @Nullable
    public String getGroup() {
        return group;
    }
    @NotEmpty
    public List<@Valid RelatedPartyRequest> getRelatedParties() {
        return relatedParties;
    }

    @NotNull
    @Valid
    public QuotePriceRequest getQuotePrice() {
        return quotePrice;
    }

    @Size(min = 3, max = 3)
    public List<@Valid QuoteItemRequest> getQuoteItems() {
        return quoteItems;
    }

    @Override
    public String toString() {
        return "QuoteRequest{" +
                "externalId='" + externalId + '\'' +
                ", projectIdentifier='" + projectIdentifier + '\'' +
                ", syncRequired='" + syncRequired + '\'' +
                ", validFor='" + validFor + '\'' +
                ", resellerTransactionId='" + resellerTransactionId + '\'' +
                ", serviceClass='" + serviceClass + '\'' +
                ", connectionType='" + connectionType + '\'' +
                ", group='" + group + '\'' +
                ", relatedParty=" + relatedParties +
                ", quotePrice=" + quotePrice +
                ", quoteItem=" + quoteItems +
                '}';
    }
}
