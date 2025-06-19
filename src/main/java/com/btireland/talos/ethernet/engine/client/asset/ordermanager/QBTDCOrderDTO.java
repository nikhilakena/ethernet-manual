package com.btireland.talos.ethernet.engine.client.asset.ordermanager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * class use to expose our API to the outside world.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QBTDCOrderDTO {

    private OrdersDTO orders;

    private QbtdcsDTO qbtdcs;

    private List<QuoteDTO> quoteList;

    private byte[] originalXMLRequest;
}
