package com.btireland.talos.ethernet.engine.client.asset.ordermanager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuoteRequestDTO {
    private String customer;
    private String startDate;
    private String endDate;
    private String product;
    private String customerReference;
    private String quoteId;
    private String eircode;
    private String status;
    private String orderNumber;
    private String orderBy;
    private String ordering;
//    @JsonProperty(defaultValue = "0")
    private String page = "0";
    private String oao;
}