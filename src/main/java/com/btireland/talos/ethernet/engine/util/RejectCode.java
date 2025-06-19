package com.btireland.talos.ethernet.engine.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RejectCode {

    CODE_3_APPL_DATE(3, "Invalid Format – The format or data type is not correct for the Tag : APPLICATION_DATE format"),
    CODE_3_ORDER_NUM(3, "Order Number must only contain letters, numbers, underscores or dashes"),
    CODE_3_EIRCODE(3, "Eircode is invalid"),
    CODE_7(7, "Order number already exists"),
    CODE_759(759, "Invalid Quote Item reference"),
    CODE_759_BT_GROUP_REF(759, "Invalid BT Group Ref"),
    CODE_760(760, "Active Order already in progress for the Quote Item Reference ID");
    private int code;
    private String rejectReason;

}
