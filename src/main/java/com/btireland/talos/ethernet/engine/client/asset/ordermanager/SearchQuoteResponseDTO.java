package com.btireland.talos.ethernet.engine.client.asset.ordermanager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuoteResponseDTO {
    private List<SearchQuoteQueryResponseDTO> quotes;
    private Integer total;
}
