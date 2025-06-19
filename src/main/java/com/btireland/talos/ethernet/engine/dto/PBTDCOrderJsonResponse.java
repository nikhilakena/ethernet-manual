package com.btireland.talos.ethernet.engine.dto;

import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCOrderDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.RejectDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PBTDCOrderJsonResponse {

    private PBTDCOrderDTO pbtdcOrderData;
    private Notifications notifications;
    private RejectDTO rejectDTO;
    private String version;
}
