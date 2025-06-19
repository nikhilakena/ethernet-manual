package com.btireland.talos.quote.facade.dto.quotemanager.response.email;

import com.btireland.talos.quote.facade.base.enumeration.internal.ServiceClassType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class QuoteGroupEmailResponse {

  private Long quoteGroupId;

  private List<QuoteEmailResponse> quoteEmails = new ArrayList<>();

  private ServiceClassType serviceClass;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime quoteDate;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime quoteCompletionDate;

  private QuoteGroupEmailResponse() {
    //For Json serialization
  }

  public QuoteGroupEmailResponse(@NotNull Long quoteGroupId,
                                 @NotEmpty List<@Valid QuoteEmailResponse> quoteEmails,
                                 @NotNull ServiceClassType serviceClass,
                                 @NotNull LocalDateTime quoteDate,
                                 @NotNull LocalDateTime quoteCompletionDate) {
    this.quoteGroupId = quoteGroupId;
    this.quoteEmails = quoteEmails;
    this.serviceClass = serviceClass;
    this.quoteDate = quoteDate;
    this.quoteCompletionDate = quoteCompletionDate;
  }

  @NotNull
  public Long getQuoteGroupId() {
    return quoteGroupId;
  }

  @NotEmpty
  public List<@Valid QuoteEmailResponse> getQuoteEmails() {
    return quoteEmails;
  }

  @NotNull
  public ServiceClassType getServiceClass() {
    return serviceClass;
  }

  @NotNull
  public LocalDateTime getQuoteDate() {
    return quoteDate;
  }

  @NotNull
  public LocalDateTime getQuoteCompletionDate() {
    return quoteCompletionDate;
  }

  @Override
  public String toString() {
    return "QuoteGroupEmailResponse{" +
        "quoteGroupId=" + quoteGroupId +
        ", quoteEmails=" + quoteEmails +
        ", serviceClass=" + serviceClass +
        ", quoteDate=" + quoteDate +
        ", quoteCompletionDate=" + quoteCompletionDate +
        '}';
  }
}
