package com.btireland.talos.ethernet.engine.client.asset.ordermanager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuoteQueryResponseDTO {
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name = "quote_item_id")
    private Integer quoteItemId;
    @Column(name = "a_end_bandwidth")
    private String aEndBandwidth;
    @Column(name = "a_end_req_access_supplier")
    private String aEndReqAccessSupplier;
    @Column(name = "a_end_eircode")
    private String aEndEircode;
    @Column(name = "requested_connection_type")
    private String requestedConnectionType;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "a_end_sla")
    private String aEndSla;
    @Column(name = "b_end_handover_node")
    private String bEndHandoverNode;
    @Column(name = "ip_range")
    private Integer ipRange;
    @Column(name = "logical_bandwidth")
    private String logicalBandwidth;
    private Integer term;
    private String product;
    @Column(name = "connection_type")
    private String connectionType;
    @Column(name = "a_end_target_access_supplier")
    private String aEndTargetAccessSupplier;
    @Column(name = "recurring_price")
    private String recurringPrice;
    private String status;
    @Column(name = "non_recurring_price")
    private String nonRecurringPrice;
    @Column(name = "offline_quoted")
    private String offlineQuoted;
    @Column(name = "message_date")
    private LocalDateTime messageDate;
    @Column(name = "recurring_frequency")
    private String recurringFrequency;
    private String oao;
    @Column(name = "project_key")
    private String projectKey;
    @Column(name = "order_number")
    private String orderNumber;
    private LocalDateTime created;
    private String name;
    @Column(name = "message_type")
    private String messageType;
    @Column(name = "pbtdc_order_id")
    private Integer pbtdcOrderId;
    @Column(name = "pbtdc_grouper")
    private String pbtdcGrouper;
    @Column(name = "enable_offline_quote_features")
    private String enableOfflineQuoteFeature;
}
