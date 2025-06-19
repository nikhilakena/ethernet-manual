package com.btireland.talos.ethernet.engine.client.asset.ordermanager;

import com.btireland.talos.ethernet.engine.dto.RejectionDetailsDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteDTO {

    private Long id;

    private String changedAt;

    private String createdAt;

    @NotNull
    private Long orderId;

    private String oao;

    private String group;

    private String product;

    private String connectionType;

    private Integer term;

    private Integer ipRange;

    private String logicalActionFlag;

    private String logicalBandwidth;

    private String logicalProfile;

    private String aendNetworkStatus;

    private String aendMultiEircode;

    private String aendAddress;

    private String aendEircode;

    private String aendActionFlag;

    private String aendTargetAccessSupplier;

    private String aendReqAccessSupplier;

    private String aendBandwidth;

    private String aendSla;

    private String bendHandOverNode;

    private String bendActionFlag;

    private String nonRecurringPrice;

    private String recurringPrice;

    private String etherwayRecurringPrice;

    private String etherflowRecurringPrice;

    private String nonRecurringCost;

    private String recurringCost;

    private String status;

    private String notes;

    private String offlineQuoted;

    private RejectionDetailsDTO rejectDetails;

}
