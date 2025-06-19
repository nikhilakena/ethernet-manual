package com.btireland.talos.ethernet.engine.dto;

import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.quotemanager.request.CreateQuoteRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QBTDCOrderRequestDTO {
    private QuoteOrderMapEntity quoteOrderMap;
    private CreateQuoteRequest createQuoteRequest;
}
