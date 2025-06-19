package com.btireland.talos.quote.facade.process.mapper.quotemanager.offlinepricing;

import com.btireland.talos.quote.facade.base.enumeration.internal.NotificationType;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteNotificationRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.offlinepricing.OfflinePricingNotificationRequest;
import org.springframework.ws.soap.SoapFault;
import java.util.Arrays;

public class NotificationRequestMapper {

    /**
     * Map offline pricing notification request to Quote Manager request.
     *
     * @param request the request {@link QuoteNotificationRequest}
     * @return the offline pricing notification request {@link OfflinePricingNotificationRequest}
     */
    public static OfflinePricingNotificationRequest map(QuoteNotificationRequest request) {
        NotificationType notificationType = Arrays.stream(NotificationType.values()).filter(type -> type.getValue().equals(request.getType())).findFirst().get();
        return new OfflinePricingNotificationRequest(notificationType, request.getDelayedReason());
    }
}
