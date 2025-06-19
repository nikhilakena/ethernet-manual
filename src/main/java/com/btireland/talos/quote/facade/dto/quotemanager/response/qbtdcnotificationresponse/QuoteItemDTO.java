package com.btireland.talos.quote.facade.dto.quotemanager.response.qbtdcnotificationresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuoteItemDTO {

    @NotEmpty
    private String quoteItemId;

    @NotEmpty
    private String status;

    private RejectionDTO rejectReason;

    private String notes;

    private String nonRecurringPrice;

    private String recurringPrice;

    private String group;

    @NotEmpty
    private String product;

    private String connectionType;

    @NotEmpty
    private String term;

    private String ipRange;

    private Logical logical;

    @NotEmpty
    private Aend aend;

    @NotEmpty
    private Bend bend;
}
