package com.btireland.talos.quote.facade.dto.quotemanager.response.qbtdcnotificationresponse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RejectionDTO {
    private String code;
    private String rejectComment;
}
