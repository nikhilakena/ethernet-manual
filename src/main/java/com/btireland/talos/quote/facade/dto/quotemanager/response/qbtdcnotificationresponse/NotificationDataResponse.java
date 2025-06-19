package com.btireland.talos.quote.facade.dto.quotemanager.response.qbtdcnotificationresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDataResponse {

    @NotEmpty
    private String dataContractName;

    @NotEmpty
    private String originatorCode;

    @NotEmpty
    private BigInteger transactionId;

    @NotEmpty
    private String dateTimeStamp;

    @NotEmpty
    private String name;

    @NotEmpty
    private String code;

    @NotEmpty
    private String ordernumber;

    private String projectkey;

    private String recurringfrequency;

    @NotEmpty
    private String applicationdate;

    @NotEmpty
    private String receiveddate;

    @NotEmpty
    private String messagetype;

    @NotEmpty
    private String messagedate;

    private List<RejectionDTO> rejectreason;

    private List<QuoteItemDTO> quoteitem;
}
