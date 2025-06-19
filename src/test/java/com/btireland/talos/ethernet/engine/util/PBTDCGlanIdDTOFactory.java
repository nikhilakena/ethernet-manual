package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.dto.PBTDCGlanIdDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PBTDCGlanIdDTOFactory {

    public static List<PBTDCGlanIdDTO> defaultPBTDCGlanIdDTO() {
        List<PBTDCGlanIdDTO> pbtdcGlanIdDTOList = new ArrayList<>();
        PBTDCGlanIdDTO pbtdcGlanIdDTO = PBTDCGlanIdDTO.builder()
                .type(PBTDCGlanIdDTO.GLANIDType.BT_ETHERFLOW)
                .glanId("111111")
                .build();

        PBTDCGlanIdDTO pbtdcGlanIdDTOForSiteA = PBTDCGlanIdDTO.builder()
                .type(PBTDCGlanIdDTO.GLANIDType.BT_ETHERWAY)
                .glanId("222222")
                .site(PBTDCGlanIdDTO.GLANIDSite.A)
                .build();

        PBTDCGlanIdDTO pbtdcGlanIdDTOForSiteB = PBTDCGlanIdDTO.builder()
                .type(PBTDCGlanIdDTO.GLANIDType.BT_ETHERWAY)
                .glanId("333333")
                .site(PBTDCGlanIdDTO.GLANIDSite.B)
                .build();

        pbtdcGlanIdDTOList.addAll(Arrays.asList(new PBTDCGlanIdDTO[]{pbtdcGlanIdDTO, pbtdcGlanIdDTOForSiteA, pbtdcGlanIdDTOForSiteB}));

        return pbtdcGlanIdDTOList;
    }
}
