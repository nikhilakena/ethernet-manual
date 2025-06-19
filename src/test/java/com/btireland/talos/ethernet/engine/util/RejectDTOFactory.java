package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.client.asset.ordermanager.RejectDTO;

public class RejectDTOFactory {

    public static RejectDTO emptyRejectDTO(){
        return RejectDTO.builder().build();
    }

    public static RejectDTO rejectDTO(RejectCode rejectCodeEnum){
       return  RejectDTO.builder()
                .rejectCode(rejectCodeEnum.getCode())
                .rejectReason(rejectCodeEnum.getRejectReason())
                .build();
    }


}
