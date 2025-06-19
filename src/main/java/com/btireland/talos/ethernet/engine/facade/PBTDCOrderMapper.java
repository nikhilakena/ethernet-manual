package com.btireland.talos.ethernet.engine.facade;

import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.OrdersDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCOrderDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.RejectDTO;
import com.btireland.talos.ethernet.engine.domain.*;
import com.btireland.talos.ethernet.engine.enums.ActionFlag;
import com.btireland.talos.ethernet.engine.enums.PowerSocketType;
import com.btireland.talos.ethernet.engine.soap.orders.*;
import com.btireland.talos.ethernet.engine.util.ContactTypes;
import com.btireland.talos.ethernet.engine.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
@Mapper(componentModel = "spring")
public class PBTDCOrderMapper {
    static final DateTimeFormatter BTDATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    static final DateTimeFormatter BT_DATE_WITHOUT_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PBTDCOrderDTO buildOrderManagerRequest(PBTDCOrder pbtdcOrderRequest, String oao) {

        PBTDCDTO pbtdcsDTO = PBTDCDTO.builder()
                .organisationName(pbtdcOrderRequest.getORDER().getORDERDATA().getCUSTOMERDETAILS().getORGANISATIONNAME())
                .applicationDate(pbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getAPPLICATIONDATE())
                .refQuoteItemId(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getREFQUOTEITEMID() == null ? null : Integer.valueOf(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getREFQUOTEITEMID()))
                .connectionType(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getCONNECTIONTYPE() == null ? null : pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getCONNECTIONTYPE())
                .btGroupRef(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getBTGROUPREF() == null ? null : pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getBTGROUPREF())
                .firstSiteContactFirstName(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(0).getFIRSTNAME())
                .firstSiteContactLastName(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(0).getLASTNAME())
                .firstSiteContactContactNumber(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(0).getCONTACTNUMBER())
                .firstSiteContactEmail(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(0).getEMAIL())
                .aEndActionFlag(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getACCESS().getACTIONFLAG())
                .circuitReference(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getACCESS().getCIRCUITREFERENCE())
                .presentation(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getACCESS().getPRESENTATION())
                .notes(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getNOTES())
                .build();

        if (pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().size() > 1) {
            pbtdcsDTO.setSecSiteContactFirstName(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(1).getFIRSTNAME());
            pbtdcsDTO.setSecSiteContactLastName(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(1).getLASTNAME());
            pbtdcsDTO.setSecSiteContactContactNumber(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(1).getCONTACTNUMBER());
            pbtdcsDTO.setSecSiteContactEmail(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(1).getEMAIL());

        }
        if (pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS() != null) {
            if (pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getLANDLORDCONTACT() != null) {
                pbtdcsDTO.setLandlordFirstName(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getLANDLORDCONTACT().getFIRSTNAME());
                pbtdcsDTO.setLandlordLastName(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getLANDLORDCONTACT().getLASTNAME());
                pbtdcsDTO.setLandlordContactNumber(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getLANDLORDCONTACT().getCONTACTNUMBER());
                pbtdcsDTO.setLandlordEmail(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getLANDLORDCONTACT().getEMAIL());

            }
            if (pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getBUILDINGMGRCONTACT() != null) {

                pbtdcsDTO.setBuildingMgrFirstName(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getBUILDINGMGRCONTACT().getFIRSTNAME());
                pbtdcsDTO.setBuildingMgrLastName(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getBUILDINGMGRCONTACT().getLASTNAME());
                pbtdcsDTO.setBuildingMgrContactNumber(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getBUILDINGMGRCONTACT().getCONTACTNUMBER());
                pbtdcsDTO.setBuildingMgrEmail(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getBUILDINGMGRCONTACT().getEMAIL());
            }
            pbtdcsDTO.setMethodInsuranceCert(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getMETHODINSURANCECERT());
            pbtdcsDTO.setSiteInduction(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getSITEINDUCTION());

        }
        if (pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getCOMMSROOM() != null) {
            pbtdcsDTO.setCommsRoomDetails(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getCOMMSROOM().getDETAILS());
            pbtdcsDTO.setReadyForDelivery(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getCOMMSROOM().getREADYFORDELIVERY());
            pbtdcsDTO.setPowerSocketType(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getCOMMSROOM().getPOWERSOCKETTYPE());
            pbtdcsDTO.setCableManager(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getCOMMSROOM().getCABLEMANAGER());
        }
        if (pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getBEND() != null) {
            pbtdcsDTO.setNniId(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getBEND().getLOCATION().getNNIID());
            pbtdcsDTO.setBEndActionFlag(pbtdcOrderRequest.getORDER().getORDERDATA().getCIRCUIT().getBEND().getACCESS().getACTIONFLAG());
        }
        if (pbtdcOrderRequest.getORDER().getORDERDATA().getCUSTOMERDETAILS().getLEADDELIVERYMANAGERCONTACT() != null) {
            pbtdcsDTO.setLeadDeliveryManagerFirstName(pbtdcOrderRequest.getORDER().getORDERDATA().getCUSTOMERDETAILS().getLEADDELIVERYMANAGERCONTACT().getFIRSTNAME());
            pbtdcsDTO.setLeadDeliveryManagerLastName(pbtdcOrderRequest.getORDER().getORDERDATA().getCUSTOMERDETAILS().getLEADDELIVERYMANAGERCONTACT().getLASTNAME());
            pbtdcsDTO.setLeadDeliveryManagerContactNumber(pbtdcOrderRequest.getORDER().getORDERDATA().getCUSTOMERDETAILS().getLEADDELIVERYMANAGERCONTACT().getCONTACTNUMBER());
            pbtdcsDTO.setLeadDeliveryManagerEmail(pbtdcOrderRequest.getORDER().getORDERDATA().getCUSTOMERDETAILS().getLEADDELIVERYMANAGERCONTACT().getEMAIL());
        }
        int index = 1;
        if (pbtdcOrderRequest.getORDER().getORDERDATA().getCUSTOMERDETAILS().getCUSTOMERINTERNALDETAILS() != null) {
            for (ORDER.ORDERDATA.CUSTOMERDETAILS.CUSTOMERINTERNALDETAILS.ATTRIBUTE attribute : pbtdcOrderRequest.getORDER().getORDERDATA().getCUSTOMERDETAILS().getCUSTOMERINTERNALDETAILS().getATTRIBUTE()) {

                pbtdcsDTO.addCustomerInternalDetails("customerInternalDetailsName" + index, attribute.getNAME());
                pbtdcsDTO.addCustomerInternalDetails("customerInternalDetailsValue" + index, attribute.getVALUE());
                index++;
            }
        }

        PBTDCOrderDTO pbtdcOrderData = PBTDCOrderDTO.builder()
                .orders(OrdersDTO.builder()
                        .oao(pbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getOBO() != null ? pbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getOBO() : oao)
                        .obo(pbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getOBO())
                        .operatorName(pbtdcOrderRequest.getORDER().getORDERDATA().getOPERATORDETAILS().getNAME())
                        .operatorCode(pbtdcOrderRequest.getORDER().getORDERDATA().getOPERATORDETAILS().getCODE())
                        .dataContractName(pbtdcOrderRequest.getORDER().getHEADER().getDATACONTRACTNAME())
                        .originatorCode(pbtdcOrderRequest.getORDER().getHEADER().getORIGINATORCODE())
                        .transactionId((pbtdcOrderRequest.getORDER().getHEADER().getTRANSACTIONID()).toString())
                        .orderRequestDateTime(pbtdcOrderRequest.getORDER().getHEADER().getDATETIMESTAMP())
                        .orderNumber(pbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getORDERNUMBER())
                        .orderServiceType(pbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getTYPE())
                        .orderStatus("Talos Pending")
                        .workflowStatus("Talos In System")
                        .build())

                .pbtdcs(pbtdcsDTO)
                .build();

        return pbtdcOrderData;

    }

    public PBTDCOrderresponse generatePBTDCOrderResponse(PBTDCOrderDTO pbtdcOrderData, Notifications notifications, String version) {

        PBTDCOrderresponse pbtdcOrderresponse = new PBTDCOrderresponse();
        pbtdcOrderresponse.setVERSION(version);
        pbtdcOrderresponse.setORDERID(pbtdcOrderData.getOrders().getOrderId());

        Notification notificationResponse = new Notification();
        ObjectFactory objectFactory = new ObjectFactory();
        NotificationHeader notificationHeader = objectFactory.createNotificationHeader();
        notificationHeader.setDATACONTRACTNAME(pbtdcOrderData.getOrders().getDataContractName());
        notificationHeader.setORIGINATORCODE(pbtdcOrderData.getOrders().getOriginatorCode());
        notificationHeader.setDATETIMESTAMP(pbtdcOrderData.getOrders().getOrderRequestDateTime());
        notificationHeader.setTRANSACTIONID(BigInteger.valueOf(notifications.getTransactionId()));

        NotificationData notificationData = objectFactory.createNotificationData();
        notificationData.setNAME(pbtdcOrderData.getOrders().getOperatorName());
        notificationData.setCODE(pbtdcOrderData.getOrders().getOperatorCode());
        notificationData.setORDERNUMBER(pbtdcOrderData.getOrders().getOrderNumber());
        notificationData.setMESSAGETYPE(notifications.getType());
        notificationData.setMESSAGEDATE(notifications.getCreatedAt().format(BTDATE_TIME_FORMAT));
        notificationData.setAPPLICATIONDATE(LocalDateTime.parse(pbtdcOrderData.getOrders().getCreatedAt()).format(BT_DATE_WITHOUT_TIME_FORMAT));
        notificationData.setRECEIVEDDATE(LocalDateTime.parse(pbtdcOrderData.getOrders().getCreatedAt()).format(BTDATE_TIME_FORMAT));
        notificationResponse.setHEADER(notificationHeader);
        notificationResponse.setNOTIFICATIONDATA(notificationData);

        pbtdcOrderresponse.setNOTIFICATION(notificationResponse);

        return pbtdcOrderresponse;

    }

    public PBTDCOrderresponse generatePBTDCRejectResponse(PBTDCOrderDTO pbtdcOrderData, RejectDTO pbtdcRejectDTO) {

        PBTDCOrderresponse pbtdcOrderresponse = new PBTDCOrderresponse();
        pbtdcOrderresponse.setORDERID(0);
        Notification notificationResponse = new Notification();
        ObjectFactory objectFactory = new ObjectFactory();
        NotificationHeader notificationHeader = objectFactory.createNotificationHeader();
        notificationHeader.setDATACONTRACTNAME(pbtdcOrderData.getOrders().getDataContractName());
        notificationHeader.setORIGINATORCODE(pbtdcOrderData.getOrders().getOriginatorCode());
        notificationHeader.setDATETIMESTAMP(pbtdcOrderData.getOrders().getOrderRequestDateTime());
        notificationHeader.setTRANSACTIONID(BigInteger.valueOf(0));

        NotificationData notificationData = objectFactory.createNotificationData();
        notificationData.setNAME(pbtdcOrderData.getOrders().getOperatorName());
        notificationData.setCODE(pbtdcOrderData.getOrders().getOperatorCode());
        notificationData.setORDERNUMBER(pbtdcOrderData.getOrders().getOrderNumber());

        String orderRequestedDateTime = LocalDateTime.now().format(BTDATE_TIME_FORMAT);
        notificationData.setAPPLICATIONDATE(LocalDate.now().format(BT_DATE_WITHOUT_TIME_FORMAT));
        notificationData.setRECEIVEDDATE(orderRequestedDateTime);
        notificationData.setMESSAGETYPE("R");
        notificationData.setMESSAGEDATE(orderRequestedDateTime);
        NotificationRejection notificationRejection = objectFactory.createNotificationRejection();
        notificationRejection.setCODE(BigInteger.valueOf(pbtdcRejectDTO.getRejectCode()));
        notificationRejection.setREJECTCOMMENT(pbtdcRejectDTO.getRejectReason());
        notificationData.getREJECTREASON().add(notificationRejection);
        notificationResponse.setHEADER(notificationHeader);
        notificationResponse.setNOTIFICATIONDATA(notificationData);

        pbtdcOrderresponse.setNOTIFICATION(notificationResponse);

        return pbtdcOrderresponse;

    }

    public Pbtdc createPBTDCEntries(PBTDCOrder ordersObj, PBTDCOrderresponse pbtdcOrderresponse, String oao) {

        Access customerAccess = populateCustomerAccess(ordersObj);

        Access wholesalerAccess = populateWholeSalerAccess(ordersObj);

        LogicalLink logicalLink = populateLogicalLink(ordersObj);

        Pbtdc pbtdcOrders = populateOrders(ordersObj,oao,pbtdcOrderresponse.getORDERID(),pbtdcOrderresponse.getNOTIFICATION().getNOTIFICATIONDATA().getORDERNUMBER());

        pbtdcOrders.setCustomerAccess(customerAccess);
        pbtdcOrders.setWholesalerAccess(wholesalerAccess);
        pbtdcOrders.setLogicalLink(logicalLink);

        return pbtdcOrders;

    }

    private LogicalLink populateLogicalLink(PBTDCOrder ordersObj) {
        String vlan = null;
        if (ordersObj.getORDER().getORDERDATA().getCIRCUIT().getBEND() != null) {
            vlan = ordersObj.getORDER().getORDERDATA().getCIRCUIT().getBEND().getLOCATION().getVLAN();
        }
        return LogicalLink.builder()
                .vlan(vlan).build();
    }

    private Access populateWholeSalerAccess(PBTDCOrder ordersObj) {
        Access wholesaleAccess = null;
        if (ordersObj.getORDER().getORDERDATA().getCIRCUIT().getBEND() != null) {
            wholesaleAccess = Access.builder()
                    .action(ActionFlag.fromString(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getBEND().getACCESS().getACTIONFLAG()))
                    .circuitReference(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getBEND().getLOCATION().getNNIID())
                    .build();
        }

        return wholesaleAccess;
    }

    private Access populateCustomerAccess(PBTDCOrder ordersObj) {

        Boolean cableManager = null;
        COMMSRoom commsRoom = null;

        if (ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getCOMMSROOM() != null) {
            commsRoom = COMMSRoom.builder().details(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getCOMMSROOM().getDETAILS())
                    .powerSocketType(PowerSocketType.fromString(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getCOMMSROOM().getPOWERSOCKETTYPE())).build();
            cableManager = ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getCOMMSROOM().getCABLEMANAGER().equalsIgnoreCase("Y") ? TRUE : FALSE;

        }

        Access customerAccess = Access.builder()
                .circuitReference(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getACCESS().getCIRCUITREFERENCE())
                .action(ActionFlag.fromString(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getACCESS().getACTIONFLAG()))
                .terminatingEquipment(TerminatingEquipment.builder()
                        .presentation(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getACCESS().getPRESENTATION())
                        .cableManager(cableManager)
                        .build())
                .site(Site.builder()
                        .commsRoom(commsRoom)
                        .build())
                .build();

        AccessInstall accessInstall = populateAccessInstallForCustomerAccess(ordersObj);

        accessInstall.setAccess(customerAccess);
        customerAccess.setAccessInstall(accessInstall);
        return customerAccess;
    }

    private AccessInstall populateAccessInstallForCustomerAccess(PBTDCOrder ordersObj) {

        Boolean commsReadyForDelivery = null;

        if (ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getCOMMSROOM() != null) {
            commsReadyForDelivery = ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getCOMMSROOM().getREADYFORDELIVERY().equalsIgnoreCase("Y") ? TRUE : FALSE;

        }

        AccessInstall accessInstall = AccessInstall.builder()
                .commsRoomReadyForDelivery(commsReadyForDelivery)
                .contacts(new ArrayList<>())
                .build();

        if (ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS() != null) {

            accessInstall.setSiteMethodInsuranceCert(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getMETHODINSURANCECERT().equalsIgnoreCase("Y") ? TRUE : FALSE);
            accessInstall.setSiteInduction(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getSITEINDUCTION());

        }
        populateSiteContacts(accessInstall.getContacts(),ordersObj,accessInstall);
        return accessInstall;
    }



    private Pbtdc populateOrders(PBTDCOrder ordersObj, String oao, long wagOrderId, String orderNumber) {


         Pbtdc pbtdc = Pbtdc.builder()
                .changedAt(DateUtils.btStringToDateTime(ordersObj.getORDER().getHEADER().getDATETIMESTAMP()))
                .createdAt(DateUtils.btStringToDateTime(ordersObj.getORDER().getHEADER().getDATETIMESTAMP()))
                .accountNumber(ordersObj.getORDER().getORDERDATA().getCUSTOMERDETAILS().getACCOUNTNUMBER())
                .wagOrderId(wagOrderId)
                .orderNumber(orderNumber)
                .oao(ordersObj.getORDER().getORDERDATA().getORDERDETAILS().getOBO() != null ? ordersObj.getORDER().getORDERDATA().getORDERDETAILS().getOBO() : oao)
                .obo(ordersObj.getORDER().getORDERDATA().getORDERDETAILS().getOBO())
                .dataContract(ordersObj.getORDER().getHEADER().getDATACONTRACTNAME())
                .originatorCode(ordersObj.getORDER().getHEADER().getORIGINATORCODE())
                .resellerTransactionId(String.valueOf(ordersObj.getORDER().getHEADER().getTRANSACTIONID()))
                .resellerOrderRequestDateTime(DateUtils.btStringToDateTime(ordersObj.getORDER().getHEADER().getDATETIMESTAMP()))
                .applicationDate(DateUtils.btStringToDate(ordersObj.getORDER().getORDERDATA().getORDERDETAILS().getAPPLICATIONDATE()))
                .operatorCode(ordersObj.getORDER().getORDERDATA().getOPERATORDETAILS().getCODE())
                .operatorName(ordersObj.getORDER().getORDERDATA().getOPERATORDETAILS().getNAME())
                .serviceType(ordersObj.getORDER().getORDERDATA().getORDERDETAILS().getTYPE())
                .organisationName(ordersObj.getORDER().getORDERDATA().getCUSTOMERDETAILS().getORGANISATIONNAME())
                .refQuoteItemId(Long.parseLong(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getREFQUOTEITEMID()))
                .connectionType(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getCONNECTIONTYPE())
                .btGroupRef(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getBTGROUPREF())
                .orderStatus("Created")
                .notes(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getNOTES())
                .workflowStatus("PENDING VALIDATION")
                .version(1)
                .businessStatus(PBTDCBusinessStatus.builder().build())
                .build();

        List<CustomerInternalDetail> custInternalDetails = new ArrayList<>();
        if (ordersObj.getORDER().getORDERDATA().getCUSTOMERDETAILS().getCUSTOMERINTERNALDETAILS() != null) {
            ordersObj.getORDER().getORDERDATA().getCUSTOMERDETAILS().getCUSTOMERINTERNALDETAILS().getATTRIBUTE().forEach(attribute -> {
                custInternalDetails.add(CustomerInternalDetail.builder().name(attribute.getNAME())
                        .value(attribute.getVALUE())
                        .pbtdc(pbtdc)
                        .build());
            });
        }
        pbtdc.setCustomerInternalDetails(custInternalDetails);

        Contact leadDeliveryMgrContact = null;
        List<Contact> contacts = new ArrayList<>();
        if (ordersObj.getORDER().getORDERDATA().getCUSTOMERDETAILS().getLEADDELIVERYMANAGERCONTACT() != null) {
            leadDeliveryMgrContact = Contact.builder()
                    .number(ordersObj.getORDER().getORDERDATA().getCUSTOMERDETAILS().getLEADDELIVERYMANAGERCONTACT().getCONTACTNUMBER())
                    .email(ordersObj.getORDER().getORDERDATA().getCUSTOMERDETAILS().getLEADDELIVERYMANAGERCONTACT().getEMAIL())
                    .firstName(ordersObj.getORDER().getORDERDATA().getCUSTOMERDETAILS().getLEADDELIVERYMANAGERCONTACT().getFIRSTNAME())
                    .lastName(ordersObj.getORDER().getORDERDATA().getCUSTOMERDETAILS().getLEADDELIVERYMANAGERCONTACT().getLASTNAME())
                    .role(ContactTypes.LEAD_DELIVERY_MANAGER.getContactOwner())
                    .orders(pbtdc)
                    .build();
            contacts.add(leadDeliveryMgrContact);
        }
        pbtdc.setContacts(contacts);

        return pbtdc;

    }

    private void populateSiteContacts(List<Contact> siteContacts, PBTDCOrder ordersObj, AccessInstall accessInstall){
        Contact mainContact = Contact.builder()
                .number(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(0).getCONTACTNUMBER())
                .email(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(0).getEMAIL())
                .firstName(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(0).getFIRSTNAME())
                .lastName(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(0).getLASTNAME())
                .role(ContactTypes.MAIN_CONTACT.getContactOwner())
                .accessInstall(accessInstall)
                .build();
        siteContacts.add(mainContact);

        if (ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().size() > 1) {
            Contact secondarySiteContact = Contact.builder()
                    .number(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(1).getCONTACTNUMBER())
                    .email(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(1).getEMAIL())
                    .firstName(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(1).getFIRSTNAME())
                    .lastName(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITECONTACT().get(1).getLASTNAME())
                    .role(ContactTypes.SECONDARY_CONTACT.getContactOwner())
                    .accessInstall(accessInstall)
                    .build();
            siteContacts.add(secondarySiteContact);
        }
        if (ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS() != null) {
            if (ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getLANDLORDCONTACT() != null) {
                Contact landlordContact = Contact.builder()
                        .number(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getLANDLORDCONTACT().getCONTACTNUMBER())
                        .email(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getLANDLORDCONTACT().getEMAIL())
                        .firstName(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getLANDLORDCONTACT().getFIRSTNAME())
                        .lastName(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getLANDLORDCONTACT().getLASTNAME())
                        .role(ContactTypes.LANDLORD.getContactOwner())
                        .accessInstall(accessInstall)
                        .build();
                siteContacts.add(landlordContact);

            }
            if (ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getBUILDINGMGRCONTACT() != null) {
                Contact buildingManagerContact = Contact.builder()
                        .number(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getBUILDINGMGRCONTACT().getCONTACTNUMBER())
                        .email(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getBUILDINGMGRCONTACT().getEMAIL())
                        .firstName(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getBUILDINGMGRCONTACT().getFIRSTNAME())
                        .lastName(ordersObj.getORDER().getORDERDATA().getCIRCUIT().getAEND().getSITE().getSITEREADINESS().getBUILDINGMGRCONTACT().getLASTNAME())
                        .role(ContactTypes.BUILDING_MANAGER.getContactOwner())
                        .accessInstall(accessInstall)
                        .build();
                siteContacts.add(buildingManagerContact);

            }

        }
    }

}
