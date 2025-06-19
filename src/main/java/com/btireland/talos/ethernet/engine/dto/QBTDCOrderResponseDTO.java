package com.btireland.talos.ethernet.engine.dto;

import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.quotemanager.response.CreateQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.qbtdcnotificationresponse.NotificationDataResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QBTDCOrderResponseDTO {
    private Long wagOrderId;
    private String version;
    private NotificationDataResponse notificationDataResponse;


}
