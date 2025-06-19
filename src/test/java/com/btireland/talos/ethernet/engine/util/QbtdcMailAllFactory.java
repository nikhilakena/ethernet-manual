package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.dto.QBTDCEmailRequest;

import java.util.List;

public class QbtdcMailAllFactory {

    public static QBTDCEmailRequest defaultEmailRequest(){
        return new QBTDCEmailRequest("BT-QBTCDC-12345",null,List.of("user1@bt.com", "user2@bt.com"),Boolean.FALSE,"sky");
    }
}
