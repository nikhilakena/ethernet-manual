package com.btireland.talos.quote.facade.dto.businessconsole;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "Quote Task details for Offline Pricing")
public class OfflinePricingTaskResponse extends RepresentationModel<OfflinePricingTaskResponse> {

    @Schema(description = "Quote task details", required = true, type = "object")
    private QuoteItemTask task;

    @Schema(description = "Buyer or requestor party name", example = "sky", required = true, type = "string")
    private String oao;

    @Schema(description = "Wag order number for a quote group", example = "BT-QBTDC-1", required = true, type =
            "string")
    private String orderNumber;

    @Schema(description = "Recurring Charge Period", example = "Annual", allowableValues = {"Monthly", "Annual"},
            required = true, type = "string")
    private String recurringFrequency;

    public OfflinePricingTaskResponse(@NotNull QuoteItemTask task, @NotBlank String oao,@NotBlank String orderNumber,
                                      @NotBlank String recurringFrequency) {
        this.task = task;
        this.oao = oao;
        this.orderNumber = orderNumber;
        this.recurringFrequency = recurringFrequency;
    }

    @NotNull
    public QuoteItemTask getTask() {
        return task;
    }

    @NotBlank
    public String getOao() {
        return oao;
    }

    @NotBlank
    public String getOrderNumber() {
        return orderNumber;
    }

    @NotBlank
    public String getRecurringFrequency() {
        return recurringFrequency;
    }

    @Override
    public String toString() {
        return "OfflinePricingTaskResponse{" +
                "task=" + task +
                ", oao='" + oao + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", recurringFrequency='" + recurringFrequency + '\'' +
                '}';
    }
}
