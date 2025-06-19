package com.btireland.talos.quote.facade.dto.quotemanager.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateQuoteRequest {

    @Setter
    private Long quoteGroupId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime quoteDate;

    private List<QuoteRequest> quotes = new ArrayList<>();

    private CreateQuoteRequest() {
        //For json serialization
    }

    public CreateQuoteRequest(@NotNull Long quoteGroupId, @NotNull LocalDateTime quoteDate,
                              @NotEmpty List<@Valid QuoteRequest> quotes) {
        this.quoteGroupId = quoteGroupId;
        this.quoteDate = quoteDate;
        this.quotes = quotes;
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
    public List<@Valid QuoteRequest> getQuotes() {
        return quotes;
    }

    @Override
    public String toString() {
        return "CreateQuoteRequest{" +
                "quoteGroupId='" + quoteGroupId + '\'' +
                ", quoteDate='" + quoteDate + '\'' +
                ", quote=" + quotes +
                '}';
    }
}
