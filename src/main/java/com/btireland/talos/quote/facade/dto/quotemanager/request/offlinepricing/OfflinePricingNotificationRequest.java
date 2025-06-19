package com.btireland.talos.quote.facade.dto.quotemanager.request.offlinepricing;

import com.btireland.talos.quote.facade.base.enumeration.internal.NotificationType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OfflinePricingNotificationRequest {

    private NotificationType type;

    private String delayedReason;

    public OfflinePricingNotificationRequest() {
        //For json deserialization
    }

    public OfflinePricingNotificationRequest(@NotNull NotificationType type, @NotBlank String delayedReason) {
        this.type = type;
        this.delayedReason = delayedReason;
    }

    @NotNull
    public NotificationType getType() {
        return type;
    }

    @NotBlank
    public String getDelayedReason() {
        return delayedReason;
    }

    @Override
    public String toString() {
        return "OfflinePricingNotificationRequest{" +
                "type=" + type +
                ", delayedReason='" + delayedReason + '\'' +
                '}';
    }
}
