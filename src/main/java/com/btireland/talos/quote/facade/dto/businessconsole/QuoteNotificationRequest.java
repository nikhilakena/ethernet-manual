package com.btireland.talos.quote.facade.dto.businessconsole;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

@Schema(description = "Request to send a notification")
public class QuoteNotificationRequest {

    @Schema(description = "Type of notification", example = "D", required = true, type = "string")
    private String type;

    @Schema(description = "Reason for the delay in quote pricing", example = "Customer delay", required = true, type
            = "string")
    private String delayedReason;

    public QuoteNotificationRequest() {
        //For json deserailization
    }

    public QuoteNotificationRequest(@NotBlank String type, @NotBlank String delayedReason) {
        this.type = type;
        this.delayedReason = delayedReason;
    }

    @NotBlank
    public String getType() {
        return type;
    }

    @NotBlank
    public String getDelayedReason() {
        return delayedReason;
    }

    @Override
    public String toString() {
        return "QuoteNotificationRequest{" +
                "type='" + type + '\'' +
                ", delayedReason='" + delayedReason + '\'' +
                '}';
    }
}
