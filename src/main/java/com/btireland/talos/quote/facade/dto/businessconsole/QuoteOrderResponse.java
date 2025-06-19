package com.btireland.talos.quote.facade.dto.businessconsole;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class QuoteOrderResponse {

    private Long orderId;

    private String orderStatus;

    private String orderNumber;

    private LocalDateTime createdAt;

    private String oao;

    private String recurringFrequency;

    List<QuoteItemTask> quoteItemTaskDTOList;

    QuoteOrderResponse() {
        // for JSON Serialization
    }

    public QuoteOrderResponse(@NotNull Long orderId, @NotBlank String orderStatus, @NotBlank String orderNumber,
        @NotNull LocalDateTime createdAt, @NotBlank String oao, @NotBlank String recurringFrequency,
        List<QuoteItemTask> quoteItemTaskDTOList) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderNumber = orderNumber;
        this.createdAt = createdAt;
        this.oao = oao;
        this.recurringFrequency = recurringFrequency;
        this.quoteItemTaskDTOList = quoteItemTaskDTOList;
    }

    @NotNull
    public Long getOrderId() {
        return orderId;
    }

    @NotBlank
    public String getOrderStatus() {
        return orderStatus;
    }

    @NotBlank
    public String getOrderNumber() {
        return orderNumber;
    }

    @NotNull
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @NotBlank
    public String getOao() {
        return oao;
    }

    @NotBlank
    public String getRecurringFrequency() {
        return recurringFrequency;
    }

    @NotEmpty
    public List<QuoteItemTask> getQuoteItemTaskDTOList() {
        return quoteItemTaskDTOList;
    }

    @Override
    public String toString() {
        return "QuoteOrderResponse{" +
            "orderId=" + orderId +
            ", orderStatus='" + orderStatus + '\'' +
            ", orderNumber='" + orderNumber + '\'' +
            ", createdAt=" + createdAt +
            ", oao='" + oao + '\'' +
            ", recurringFrequency='" + recurringFrequency + '\'' +
            ", quoteItemTaskDTOList=" + quoteItemTaskDTOList +
            '}';
    }
}
