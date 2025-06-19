package com.btireland.talos.ethernet.engine.util;

import au.com.dius.pact.consumer.dsl.DslPart;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.OrdersDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCOrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PBTDCOrderDTOFactory {

    public static String asJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(object);
    }

    public static PBTDCOrderDTO defaultPBTDCOrderDTO() {
        Map<String, String> customerInternalDetails = new HashMap<>();
        customerInternalDetails.put("customerInternalDetailsName1", "name1");
        customerInternalDetails.put("customerInternalDetailsValue1", "value1");
        customerInternalDetails.put("customerInternalDetailsName2", "name2");
        customerInternalDetails.put("customerInternalDetailsValue2", "value2");
        return PBTDCOrderDTO.builder()
                .orders(OrdersDTO.builder()
                        .oao("string")
                        .obo("string")
                        .operatorName("SKY")
                        .operatorCode("83758")
                        .dataContractName("WAG")
                        .originatorCode("EXT")
                        .transactionId("100")
                        .orderRequestDateTime("03/02/2022 10:43:35")
                        .orderNumber("1")
                        .orderServiceType("PBTDC")
                        .orderStatus("Talos Pending")
                        .workflowStatus("Talos In System")
                        .build())
                .pbtdcs(PBTDCDTO.builder()
                        .organisationName("ABC Limited")
                        .applicationDate("03/02/2022")
                        .refQuoteItemId(1)
                        .firstSiteContactFirstName("Paul")
                        .firstSiteContactLastName("Cronin")
                        .firstSiteContactContactNumber("0749152601")
                        .firstSiteContactEmail("paul@xyz.com")
                        .landlordFirstName("Cian")
                        .landlordLastName("Cronin")
                        .landlordContactNumber("045575768")
                        .landlordEmail("cian@abc.com")
                        .buildingMgrFirstName("Harry")
                        .buildingMgrLastName("Ryan")
                        .buildingMgrContactNumber("3545467")
                        .buildingMgrEmail("harry@abc.com")
                        .methodInsuranceCert("Y")
                        .siteInduction("xyz")
                        .commsRoomDetails("xyz")
                        .readyForDelivery("Y")
                        .powerSocketType("3-PIN")
                        .cableManager("Y")
                        .aEndActionFlag("CH")
                        .circuitReference("xyz")
                        .presentation("RJ45")
                        .nniId("R3445")
                        .bEndActionFlag("CH")
                        .notes("PBTDC")
                        .leadDeliveryManagerFirstName("Alex")
                        .leadDeliveryManagerLastName("Cronin")
                        .leadDeliveryManagerContactNumber("3243535")
                        .leadDeliveryManagerEmail("alex@abc.com")
                        .customerInternalDetails(customerInternalDetails)
                        .build())
                .build();

    }

    public static PBTDCOrderDTO defaultPBTDCOrderDTOWithEmptyOBO() throws IOException {
        PBTDCOrderDTO pbtdcOrder = defaultPBTDCOrderDTO();
        pbtdcOrder.getOrders().setObo(null);
        pbtdcOrder.getOrders().setOao("sky");
        return pbtdcOrder;
    }

    public static com.btireland.talos.ethernet.engine.dto.OrdersDTO defaultOrdersDTO() {
        return com.btireland.talos.ethernet.engine.dto.OrdersDTO.builder()
                .orderId(1L)
                .talosOrderId(1L)
                .oao("sky")
                .operatorCode("83758")
                .dataContract("WAG")
                .originatorCode("EXT")
                .resellerTransactionId("100")
                .build();
    }

    public static PBTDCOrderDTO savedPBTDCResponse() throws IOException {
        PBTDCOrderDTO pbtdcOrderDTO = defaultPBTDCOrderDTO();
        pbtdcOrderDTO.getOrders().setOrderId(1L);
        pbtdcOrderDTO.getOrders().setCreatedAt("2022-03-03T10:55:11");
        pbtdcOrderDTO.getPbtdcs().setId(2L);
        pbtdcOrderDTO.getPbtdcs().setOrderId(1L);

        return pbtdcOrderDTO;
    }

    public static DslPart defaultPBTDCOrderDTOResponsePactFormat() throws IOException {
        var pbtdcOrderDTO = savedPBTDCResponse();
        return LambdaDsl.newJsonBody((o) -> o
                .object("orders", (orders) -> {
                    orders.numberType("orderId", pbtdcOrderDTO.getOrders().getOrderId());
                    orders.stringType("orderNumber", pbtdcOrderDTO.getOrders().getOrderNumber());
                })
                .object("pbtdcs", (pbtdcs) -> {
                    pbtdcs.numberType("id", pbtdcOrderDTO.getPbtdcs().getId());
                }))
                .build();
    }

    public static String defaultPBTDCOrderRequestAsJson() throws Exception {
        return asJson(defaultPBTDCOrderDTOWithOriginalRequest());
    }

    public static PBTDCOrderDTO defaultPBTDCOrderDTOWithOriginalRequest() throws IOException {
        PBTDCOrderDTO pbtdcOrderDTO = defaultPBTDCOrderDTO();
        pbtdcOrderDTO.setOriginalXMLRequest(IOUtils.toByteArray(new ClassPathResource("/data/OrderManagerClientTest/PBTDCOrder.xml", PBTDCOrderDTOFactory.class).getInputStream()));
        return pbtdcOrderDTO;
    }
}
