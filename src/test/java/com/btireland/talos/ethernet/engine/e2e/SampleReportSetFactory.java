package com.btireland.talos.ethernet.engine.e2e;

import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportDTO;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportSetDTO;

import java.util.List;

public class SampleReportSetFactory {

    public static PBTDCOrderReportSetDTO getSampleReportSet(){
        return PBTDCOrderReportSetDTO.builder()
                .internalReportEntries( List.of(
                    PBTDCOrderReportDTO.builder()
                            .orderManager("John Smith")
                            .orderManagerEmail("john.smith@company.com")
                            .siteName("Anytown")
                            .dateReceivedByDelivery("2022-01-01")
                            .referenceIds("MyReferenceIds")
                            .product("EtherWay")
                            .deliveryType("New Delivery")
                            .aendSiteDetails("This is the A End Site Details")
                            .bendSiteDetails("This is the B End Site Details")
                            .aendContactDetails("This is the A End Site Contact")
                            .bendContactDetails("This is the B End Site Contact")
                            .accessSpeed("1234")
                            .portSpeed("2MB")
                            .aendPresentation("LC")
                            .bendPresentation("RJ45")
                            .notes("Notes on delivery")
                            .indicativeDeliveryDate("2022-03-01")
                            .deliveryDate("2022-03-02")
                            .deliveryOnTrack("Yes")
                            .orderEntryAndValidationStatus("COMPLETE")
                            .planningStatus("COMPLETE")
                            .accessInstallation("Customer Delay")
                            .testingCpeInstallation("Customer Delay")
                            .serviceCompleteAndOperational("TRUE")
                            .build(),
                    PBTDCOrderReportDTO.builder()
                            .orderManager("Dave Brown")
                            .orderManagerEmail("dave.brown@company.com")
                            .siteName("Anytown")
                            .dateReceivedByDelivery("2022-01-01")
                            .referenceIds("MyReferenceIds")
                            .product("EtherWay")
                            .deliveryType("New Delivery")
                            .aendSiteDetails("This is the A End Site Details")
                            .bendSiteDetails("This is the B End Site Details")
                            .aendContactDetails("This is the A End Site Contact")
                            .bendContactDetails("This is the B End Site Contact")
                            .accessSpeed("1234")
                            .portSpeed("2MB")
                            .aendPresentation("LC")
                            .bendPresentation("RJ45")
                            .notes("Notes on delivery")
                            .indicativeDeliveryDate("2022-03-01")
                            .deliveryDate("2022-03-02")
                            .deliveryOnTrack("No")
                            .orderEntryAndValidationStatus("COMPLETE")
                            .planningStatus("COMPLETE")
                            .accessInstallation("Customer Delay")
                            .testingCpeInstallation("Customer Delay")
                            .serviceCompleteAndOperational("TRUE")
                            .build()
                )
        ).build();
    }
}
