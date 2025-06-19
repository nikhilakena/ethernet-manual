package com.btireland.talos.quote.facade.dto.quotemanager.response;

import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.NotificationType;
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

public class CreateQuoteResponse {

    private Long quoteGroupId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime quoteDate;

    private NotificationType notificationType;

    private List<QuoteResponse> quotes = new ArrayList<>();

    private RejectionDetailsResponse rejectionDetails;

    private String projectIdentifier;

    private ServiceClassType serviceClass;

    private ConnectionType connectionType;

    private String resellerTransactionId;

    private CreateQuoteResponse() {
    } //For json deserialization

    public CreateQuoteResponse(@NotNull Long quoteGroupId,
                               @NotNull LocalDateTime quoteDate,
                               @NotNull NotificationType notificationType,
                               @NotEmpty List<@Valid QuoteResponse> quotes,
                               @Nullable String projectIdentifier,
                               @NotNull ServiceClassType serviceClass,
                               @Nullable ConnectionType connectionType,
                               @NotBlank String resellerTransactionId) {
        this.quoteGroupId = quoteGroupId;
        this.notificationType = notificationType;
        this.quotes = quotes;
        this.quoteDate = quoteDate;
        this.projectIdentifier = projectIdentifier;
        this.serviceClass = serviceClass;
        this.connectionType = connectionType;
        this.resellerTransactionId = resellerTransactionId;
    }

    public CreateQuoteResponse(@NotNull Long quoteGroupId,
                               @NotNull LocalDateTime quoteDate,
                               @NotNull NotificationType notificationType,
                               @NotNull RejectionDetailsResponse rejectionDetails) {
        this.quoteGroupId = quoteGroupId;
        this.quoteDate = quoteDate;
        this.notificationType = notificationType;
        this.rejectionDetails = rejectionDetails;
    }

    @NotNull
    public Long getQuoteGroupId() {
        return quoteGroupId;
    }

    @NotNull
    public LocalDateTime getQuoteDate() {
        return quoteDate;
    }

    @NotNull
    public NotificationType getNotificationType() {
        return notificationType;
    }

    @NotEmpty
    public List<@Valid QuoteResponse> getQuotes() {
        return quotes;
    }

    @Nullable
    public RejectionDetailsResponse getRejectionDetails() {
        return rejectionDetails;
    }

    @Nullable
    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    @NotNull
    public ServiceClassType getServiceClass() {
        return serviceClass;
    }

    @Nullable
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @NotBlank
    public String getResellerTransactionId() {
        return resellerTransactionId;
    }

    public void setRejectionDetails(@Nullable RejectionDetailsResponse rejectionDetails) {
        this.rejectionDetails = rejectionDetails;
    }

    @Override
    public String toString() {
        return "CreateQuoteResponse{" +
                "quoteGroupId=" + quoteGroupId +
                ", quoteDate=" + quoteDate +
                ", notificationType=" + notificationType +
                ", quotes=" + quotes +
                ", rejectionDetails=" + rejectionDetails +
                ", projectIdentifier='" + projectIdentifier + '\'' +
                ", serviceClass=" + serviceClass +
                ", connectionType=" + connectionType +
                ", resellerTransactionId='" + resellerTransactionId + '\'' +
                '}';
    }
}
