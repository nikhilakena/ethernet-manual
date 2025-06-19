package com.btireland.talos.quote.facade.dto.quotemanager.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class RejectionDetailsResponse {
    private String rejectCode;
    private String rejectReason;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime quoteRejectedDate;
    private String delayReason;

    private RejectionDetailsResponse() {
    } //For Json Deserialization

    public RejectionDetailsResponse(@NotBlank String rejectCode, @NotBlank String rejectReason,
        @NotNull LocalDateTime quoteRejectedDate) {
        this.rejectCode = rejectCode;
        this.rejectReason = rejectReason;
        this.quoteRejectedDate = quoteRejectedDate;
    }

    public RejectionDetailsResponse(@NotBlank String rejectCode, @NotBlank String rejectReason,
                                    @NotNull LocalDateTime quoteRejectedDate, @Nullable String delayReason) {
        this.rejectCode = rejectCode;
        this.rejectReason = rejectReason;
        this.quoteRejectedDate = quoteRejectedDate;
        this.delayReason = delayReason;
    }

    @NotBlank
    public String getRejectCode() {
        return rejectCode;
    }

    @NotBlank
    public String getRejectReason() {
        return rejectReason;
    }

    @NotNull
    public LocalDateTime getQuoteRejectedDate() {
        return quoteRejectedDate;
    }

    @Nullable
    public String getDelayReason() {
        return delayReason;
    }

    @Override
    public String toString() {
        return "RejectionDetailsResponse{" +
                "rejectCode='" + rejectCode + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", quoteRejectedDate=" + quoteRejectedDate +
                ", delayReason=" + delayReason +
                '}';
    }
}
