package com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class GetRejectionDetailsResponse {

    private String rejectCode;

    private String rejectReason;

    private String delayReason;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime quoteRejectedDate;

    private GetRejectionDetailsResponse() {
    } //For json serialization

    public GetRejectionDetailsResponse(@NotBlank String rejectCode, @NotBlank String rejectReason,
                                       @Nullable String delayReason,
                                       @NotNull LocalDateTime quoteRejectedDate) {
        this.rejectCode = rejectCode;
        this.rejectReason = rejectReason;
        this.delayReason = delayReason;
        this.quoteRejectedDate = quoteRejectedDate;
    }

    public GetRejectionDetailsResponse(@NotBlank String rejectCode, @NotBlank String rejectReason,
                                       @NotNull LocalDateTime quoteRejectedDate) {
        this.rejectCode = rejectCode;
        this.rejectReason = rejectReason;
        this.quoteRejectedDate = quoteRejectedDate;
    }

    @NotBlank
    public String getRejectCode() {
        return rejectCode;
    }

    @NotBlank
    public String getRejectReason() {
        return rejectReason;
    }

    @Nullable
    public String getDelayReason() {
        return delayReason;
    }

    @NotNull
    public LocalDateTime getQuoteRejectedDate() {
        return quoteRejectedDate;
    }

    @Override
    public String toString() {
        return "RejectionDetailsResponse{" +
                "rejectCode='" + rejectCode + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", delayReason='" + delayReason + '\'' +
                ", quoteRejectedDate=" + quoteRejectedDate +
                '}';
    }
}
