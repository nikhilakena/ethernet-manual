package com.btireland.talos.quote.facade.process.mapper.quotemanager;

import static com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants.BT_DATE_TIME_FORMAT;
import static com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants.BT_DATE_WITHOUT_TIME_FORMAT;
import static com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants.QBTDC_CONTRACT_VERSION;
import static com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants.QBTDC_DATA_CONTRACT_NAME;
import static com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants.QBTDC_ORIGINATOR_CODE;

import com.btireland.talos.core.common.rest.exception.ResourceNotFoundException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.dto.QBTDCOrderResponseDTO;
import com.btireland.talos.ethernet.engine.soap.orders.ObjectFactory;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrderResponse;
import com.btireland.talos.ethernet.engine.soap.orders.QbtdcNotification;
import com.btireland.talos.ethernet.engine.soap.orders.QbtdcNotificationData;
import com.btireland.talos.ethernet.engine.soap.orders.QbtdcNotificationHeader;
import com.btireland.talos.ethernet.engine.soap.orders.QuoteItem;
import com.btireland.talos.ethernet.engine.soap.orders.Rejection;
import com.btireland.talos.ethernet.engine.util.QuoteStatus;
import com.btireland.talos.quote.facade.base.enumeration.internal.PriceType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteItemNameType;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.quotemanager.response.CreateQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteItemResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuotePriceResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteProductConfigResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.qbtdcnotificationresponse.*;
import com.btireland.talos.quote.facade.process.helper.QuoteHelper;

import javax.ws.rs.HEAD;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class QuoteManagerResponseMapper {

  static ObjectFactory objectFactory = new ObjectFactory();


  /**
   * Method to map Quote Manager reject response to QBTDCOrderResponse
   *
   * @param createQuoteResponse CreateQuoteOrderResponse object returned from QuoteManager
   * @param quoteOrderMap QuoteOrderMapEntity Object for getting OrderId and OrderNumber
   * @param notification Notcom Notification Object
   *
   * @return QBTDCOrderResponse xml object
   */
  public static QBTDCOrderResponse createQBTDCOrderRejectResponse(CreateQuoteResponse createQuoteResponse,
      QuoteOrderMapEntity quoteOrderMap, Notifications notification, Long wagOrderId) {
    QBTDCOrderResponse qbtdcOrderResponse = createQBTDCOrderResponse(wagOrderId);

    QbtdcNotification notificationResponse = buildNotificationResponse(createQuoteResponse, quoteOrderMap, notification);

    QbtdcNotificationData notificationData = notificationResponse.getNOTIFICATIONDATA();

    //Create rejection data.
    Rejection notificationRejection = objectFactory.createRejection();
    notificationRejection.setCODE(createQuoteResponse.getRejectionDetails().getRejectCode());
    notificationRejection.setREJECTCOMMENT(createQuoteResponse.getRejectionDetails().getRejectReason());

    //Add rejection details to notification.
    notificationData.getREJECTREASON().add(notificationRejection);

    //Set notification header and body.
    notificationResponse.setNOTIFICATIONDATA(notificationData);

    //Set notification response to order response.
    qbtdcOrderResponse.setNOTIFICATION(notificationResponse);

    return qbtdcOrderResponse;
  }

  public static QBTDCOrderResponseDTO createQBTDCOrderRejectResponseDTO(CreateQuoteResponse createQuoteResponse,
                                                                        QuoteOrderMapEntity quoteOrderMap, Notifications notification, Long wagOrderId) {
    QBTDCOrderResponseDTO qbtdcOrderResponseDTO = createQbtdcOrderResponseDTO(wagOrderId);
    NotificationDataResponse notificationDataResponse = createNotificationResponseDTO(createQuoteResponse, quoteOrderMap, notification);

    RejectionDTO rejectionDTO = RejectionDTO.builder().code(createQuoteResponse.getRejectionDetails().getRejectCode())
            .rejectComment(createQuoteResponse.getRejectionDetails().getRejectReason()).build();

    notificationDataResponse.setRejectreason(List.of(rejectionDTO));

    qbtdcOrderResponseDTO.setNotificationDataResponse(notificationDataResponse);
    return qbtdcOrderResponseDTO;
  }

  /**
   * Method to map Quote Manager complete response to QBTDCOrderResponse
   *
   * @param createQuoteResponse CreateQuoteOrderResponse object returned from QuoteManager
   * @param quoteOrderMap QuoteOrderMapEntity Object for getting OrderId and OrderNumber   *
   * @param notification Notcom Notification Object
   * @param wagOrderId   Wag order id
   *
   * @return QBTDCOrderResponse xml Object
   */
  public static QBTDCOrderResponse createQBTDCCompletionResponse(CreateQuoteResponse createQuoteResponse,
      QuoteOrderMapEntity quoteOrderMap, Notifications notification, Long wagOrderId) throws TalosNotFoundException {
    QBTDCOrderResponse qbtdcOrderResponse = createQBTDCOrderResponse(wagOrderId);

    QbtdcNotification notificationResponse = buildNotificationResponse(createQuoteResponse, quoteOrderMap, notification);

    //Create notification body data.
    QbtdcNotificationData notificationData = notificationResponse.getNOTIFICATIONDATA();
    notificationData.setPROJECTKEY(createQuoteResponse.getProjectIdentifier());
    notificationData.setRECURRINGFREQUENCY(QuoteHelper.fetchRecurringFrequency(createQuoteResponse.getQuotes().get(0)));

    //Build quote items.
    List<QuoteItem> quoteItems = notificationData.getQUOTEITEM();
    buildQuoteItems(quoteItems, quoteOrderMap.getQuoteItemMapList(), createQuoteResponse);

    notificationResponse.setNOTIFICATIONDATA(notificationData);

    qbtdcOrderResponse.setNOTIFICATION(notificationResponse);

    return qbtdcOrderResponse;
  }

  public static QBTDCOrderResponseDTO createQBTDCCompletionResponseDTO(CreateQuoteResponse createQuoteResponse,
                                                                       QuoteOrderMapEntity quoteOrderMap, Notifications notification, Long wagOrderId) throws TalosNotFoundException {
    QBTDCOrderResponseDTO qbtdcOrderResponseDTO = createQbtdcOrderResponseDTO(wagOrderId);
    NotificationDataResponse notificationDataResponse = createNotificationResponseDTO(createQuoteResponse, quoteOrderMap, notification);

    //Create notification body data.
    notificationDataResponse.setProjectkey(createQuoteResponse.getProjectIdentifier());
    notificationDataResponse.setRecurringfrequency(QuoteHelper.fetchRecurringFrequency(createQuoteResponse.getQuotes().get(0)));

    //Build quote items.
    List<QuoteItemDTO> quoteItemDTOS = new ArrayList<>();
    buildQuoteItemsForNotificationResponse(quoteItemDTOS, quoteOrderMap.getQuoteItemMapList(), createQuoteResponse);

    notificationDataResponse.setQuoteitem(quoteItemDTOS);

    qbtdcOrderResponseDTO.setNotificationDataResponse(notificationDataResponse);

    return qbtdcOrderResponseDTO;
  }

  /**
   * Method to map Quote Manager WSA response to QBTDCOrderResponse
   *
   * @param createQuoteResponse CreateQuoteOrderResponse object returned from QuoteManager
   * @param quoteOrderMap QuoteOrderMapEntity Object for getting OrderId and OrderNumber
   * @param notification Notcom Notification Object
   *
   * @return QBTDCOrderResponse xml object
   */
  public static QBTDCOrderResponse createQBTDCOrderWSAResponse(CreateQuoteResponse createQuoteResponse,
      QuoteOrderMapEntity quoteOrderMap, Notifications notification, Long wagOrderId) {
    QBTDCOrderResponse qbtdcOrderResponse = createQBTDCOrderResponse(wagOrderId);

    QbtdcNotification notificationResponse = buildNotificationResponse(createQuoteResponse, quoteOrderMap, notification);

    QbtdcNotificationData notificationData = notificationResponse.getNOTIFICATIONDATA();

    //Set notification header and body.
    notificationResponse.setNOTIFICATIONDATA(notificationData);

    //Set notification response to order response.
    qbtdcOrderResponse.setNOTIFICATION(notificationResponse);

    return qbtdcOrderResponse;
  }

  public static QBTDCOrderResponseDTO createQBTDCOrderWSAResponseDTO(CreateQuoteResponse createQuoteResponse,
                                                                     QuoteOrderMapEntity quoteOrderMap, Notifications notification, Long wagOrderId) {
    QBTDCOrderResponseDTO qbtdcOrderResponseDTO = createQbtdcOrderResponseDTO(wagOrderId);
    NotificationDataResponse notificationDataResponse = createNotificationResponseDTO(createQuoteResponse, quoteOrderMap, notification);

    qbtdcOrderResponseDTO.setNotificationDataResponse(notificationDataResponse);
    return qbtdcOrderResponseDTO;
  }

  /**
   * Method to build notification response
   * @param createQuoteResponse CreateQuoteOrderResponse object returned from QuoteManager
   * @param quoteOrderMap QuoteOrderMapEntity Object for getting OrderId and OrderNumber
   * @param notification Notification response
   *
   * @return Qbtdc Notification response.
   */
  private static QbtdcNotification buildNotificationResponse(CreateQuoteResponse createQuoteResponse,
      QuoteOrderMapEntity quoteOrderMap, Notifications notification) {
    QbtdcNotification notificationResponse = new QbtdcNotification();

    //Create notification header data
    QbtdcNotificationHeader notificationHeader = createNotificationHeader(createQuoteResponse, notification);

    //Create notification body data.
    QbtdcNotificationData notificationData = createNotificationData(createQuoteResponse, quoteOrderMap, notification);

    notificationResponse.setHEADER(notificationHeader);
    notificationResponse.setNOTIFICATIONDATA(notificationData);

    return notificationResponse;
  }

  /**
   * Method to build quote items.
   *
   * @param quoteItems Quote items
   */
  private static void buildQuoteItems(List<QuoteItem> quoteItems, List<QuoteItemMapEntity> quoteItemMapList,
      CreateQuoteResponse createQuoteResponse) throws TalosNotFoundException {

    //Iterate through the QuoteResponse to set the quote items.
    for (QuoteResponse quoteResponse : createQuoteResponse.getQuotes()) {
      //Create quote item from object factory
      QuoteItem quoteItem = objectFactory.createQuoteItem();

      //Set WAG id as quote item id and set status.
      quoteItem.setQUOTEITEMID(String.valueOf(getWagQuoteItemId(quoteItemMapList, quoteResponse.getQuoteId())));

      //Set status and build status parameters
      buildStatusParameters(quoteResponse, quoteItem);

      //Build AEnd and set in quote item.
      buildAEnd(quoteResponse, quoteItem);

      //Build BEnd and set in quote item.
      buildBEnd(quoteResponse, quoteItem);

      //Build Logical and set in quote item.
      buildLogical(quoteResponse, quoteItem, createQuoteResponse.getServiceClass().name());
      if (!quoteItemMapList.get(0).getQuoteOrderMap().getOperatorName().equals("LMX")) {
        quoteItem.setCONNECTIONTYPE(createQuoteResponse.getConnectionType() != null ? createQuoteResponse.getConnectionType().name() : null);
      }

      quoteItems.add(quoteItem);
    }
  }

  private static void buildQuoteItemsForNotificationResponse(List<QuoteItemDTO> quoteItems, List<QuoteItemMapEntity> quoteItemMapList,
                                                             CreateQuoteResponse createQuoteResponse) throws TalosNotFoundException {

    //Iterate through the QuoteResponse to set the quote items.
    for (QuoteResponse quoteResponse : createQuoteResponse.getQuotes()) {

      QuoteItemDTO quoteItem = QuoteItemDTO.builder().quoteItemId(String.valueOf(getWagQuoteItemId(quoteItemMapList, quoteResponse.getQuoteId()))).build();
      //Set status and build status parameters
      buildStatusParametersDTO(quoteResponse, quoteItem);

      //Build AEnd and set in quote item.
      buildAEndDTO(quoteResponse, quoteItem);

      //Build BEnd and set in quote item.
      buildBEndDTO(quoteResponse, quoteItem);

      //Build Logical and set in quote item.
      buildLogicalDTO(quoteResponse, quoteItem, createQuoteResponse.getServiceClass().name());
      if (!quoteItemMapList.get(0).getQuoteOrderMap().getOperatorName().equals("LMX")) {
        quoteItem.setConnectionType(createQuoteResponse.getConnectionType() != null ? createQuoteResponse.getConnectionType().name() : null);
      }

      quoteItems.add(quoteItem);
    }
  }

  /**
   * Method to set status and set status parameters.
   *
   * @param quoteResponse Quote response
   * @param quoteItem Quote item.
   */
  private static void buildStatusParameters(QuoteResponse quoteResponse, QuoteItem quoteItem) {
    quoteItem.setSTATUS(quoteResponse.getQuoteState().getValue());

    //If the status is rejected set rejection parameters else set price parameters.
    if (QuoteStatus.REJECTED.getValue().equalsIgnoreCase(quoteResponse.getQuoteState().getValue())) {
      Rejection rejection = objectFactory.createRejection();
      rejection.setCODE(quoteResponse.getRejectionDetails().getRejectCode());
      rejection.setREJECTCOMMENT(quoteResponse.getRejectionDetails().getRejectReason());
      quoteItem.setREJECTREASON(rejection);
    } else {
      quoteItem.setNONRECURRINGPRICE(getNonRecurringPrice(quoteResponse));
      quoteItem.setRECURRINGPRICE(getRecurringPrice(quoteResponse));
    }
  }

  private static void buildStatusParametersDTO(QuoteResponse quoteResponse, QuoteItemDTO quoteItem) {
    quoteItem.setStatus(quoteResponse.getQuoteState().getValue());

    //If the status is rejected set rejection parameters else set price parameters.
    if (QuoteStatus.REJECTED.getValue().equalsIgnoreCase(quoteResponse.getQuoteState().getValue())) {
      RejectionDTO rejectionDTO = RejectionDTO.builder().code(quoteResponse.getRejectionDetails().getRejectCode())
              .rejectComment(quoteResponse.getRejectionDetails().getRejectReason()).build();
      quoteItem.setRejectReason(rejectionDTO);
    } else {
      quoteItem.setNonRecurringPrice(getNonRecurringPrice(quoteResponse));
      quoteItem.setRecurringPrice(getRecurringPrice(quoteResponse));
    }
  }

  /**
   * Method to build AEnd and set in quote item.
   *
   * @param quoteResponse Quote response
   * @param quoteItem Quote item.
   */
  private static void buildAEnd(QuoteResponse quoteResponse, QuoteItem quoteItem) throws TalosNotFoundException {
    //Fetch AEnd quote item response from quote response
    QuoteItemResponse quoteItemResponseForAEnd = QuoteHelper.fetchQuoteItemResponse(quoteResponse, QuoteItemNameType.A_END);
    quoteItem.setTERM(String.valueOf(quoteItemResponseForAEnd.getQuoteItemTerm()));

    // Build AEnd access and location.
    QuoteItem.AEND aEnd = buildAEndAccess(quoteItemResponseForAEnd);
    aEnd.setLOCATION(buildAEndLocation(quoteItemResponseForAEnd));
    quoteItem.setAEND(aEnd);
  }

  private static void buildAEndDTO(QuoteResponse quoteResponse, QuoteItemDTO quoteItem) throws TalosNotFoundException {
    //Fetch AEnd quote item response from quote response
    QuoteItemResponse quoteItemResponseForAEnd = QuoteHelper.fetchQuoteItemResponse(quoteResponse, QuoteItemNameType.A_END);
    quoteItem.setTerm(String.valueOf(quoteItemResponseForAEnd.getQuoteItemTerm()));

    // Build AEnd access and location.
    Aend aEnd = buildAEndAccessDTO(quoteItemResponseForAEnd);
    aEnd.setAendLocation(buildAEndLocationDTO(quoteItemResponseForAEnd));
    quoteItem.setAend(aEnd);
  }

  /**
   * Method to build BEnd and set in quote item.
   *
   * @param quoteResponse Quote response
   * @param quoteItem Quote item.
   */
  private static void buildBEnd(QuoteResponse quoteResponse, QuoteItem quoteItem) throws TalosNotFoundException {
    //Get BEnd from Object factory
    QuoteItem.BEND bEnd = objectFactory.createQuoteItemBEND();

    //Fetch BEnd quote item response from quote response
    QuoteItemResponse quoteItemResponseForBEnd = QuoteHelper.fetchQuoteItemResponse(quoteResponse, QuoteItemNameType.B_END);

    //Build BEnd access.
    QuoteItem.BEND.ACCESS bEndAccess = objectFactory.createQuoteItemBENDACCESS();
    bEndAccess.setACTIONFLAG(quoteItemResponseForBEnd.getQuoteProduct().getQuoteProductConfig().getAction().getValue());
    bEnd.setACCESS(bEndAccess);

    //Build BEnd location.
    QuoteItem.BEND.LOCATION bEndLocation = objectFactory.createQuoteItemBENDLOCATION();
    bEndLocation.setHANDOVERNODE(quoteItemResponseForBEnd.getQuoteProduct().getQuoteProductPlace().getHandoverNode());
    bEnd.setLOCATION(bEndLocation);
    quoteItem.setBEND(bEnd);
  }

  private static void buildBEndDTO(QuoteResponse quoteResponse, QuoteItemDTO quoteItem) throws TalosNotFoundException {
    //Get BEnd from Object factory
    Bend bEnd = Bend.builder().build();

    //Fetch BEnd quote item response from quote response
    QuoteItemResponse quoteItemResponseForBEnd = QuoteHelper.fetchQuoteItemResponse(quoteResponse, QuoteItemNameType.B_END);

    //Build BEnd access.
    BendAccess bEndAccess = BendAccess.builder().build();
    bEndAccess.setActionFlag(quoteItemResponseForBEnd.getQuoteProduct().getQuoteProductConfig().getAction().getValue());
    bEnd.setAccess(bEndAccess);

    //Build BEnd location.
    BendLocation bEndLocation = BendLocation.builder().build();
    bEndLocation.setHandoverNode(quoteItemResponseForBEnd.getQuoteProduct().getQuoteProductPlace().getHandoverNode());
    bEnd.setBendLocation(bEndLocation);
    quoteItem.setBend(bEnd);
  }

  /**
   * Method to build logical and set in quote item.
   *
   * @param quoteResponse Quote response
   * @param quoteItem     Quote item.
   * @param serviceClass  Service class.
   */
  private static void buildLogical(QuoteResponse quoteResponse, QuoteItem quoteItem, String serviceClass) throws TalosNotFoundException {
    //Get Logical from Object factory.
    QuoteItem.LOGICAL logical = objectFactory.createQuoteItemLOGICAL();

    //Fetch Logical quote item response from quote response
    QuoteItemResponse quoteItemResponseForLogical = QuoteHelper.fetchQuoteItemResponse(quoteResponse, QuoteItemNameType.LOGICAL);
    QuoteProductConfigResponse quoteProductConfigResponse = quoteItemResponseForLogical.getQuoteProduct().getQuoteProductConfig();

    //Set IP Range
    if (quoteProductConfigResponse.getIpRange() != null) {
      QuoteItem.IP quoteItemIP = objectFactory.createQuoteItemIP();
      quoteItemIP.setIPRANGE(String.valueOf(quoteProductConfigResponse.getIpRange()));
      quoteItem.setIP(quoteItemIP);
    }

    logical.setACTIONFLAG(quoteProductConfigResponse.getAction().getValue());
    logical.setBANDWIDTH(QuoteHelper.formatBandwidth(quoteProductConfigResponse.getBandwidth()));
    logical.setPROFILE(quoteProductConfigResponse.getProfile().name());

    quoteItem.setPRODUCT(serviceClass);
    quoteItem.setLOGICAL(logical);
  }

  private static void buildLogicalDTO(QuoteResponse quoteResponse, QuoteItemDTO quoteItem, String serviceClass) throws TalosNotFoundException {
    //Get Logical from Object factory.
    Logical logical = Logical.builder().build();

    //Fetch Logical quote item response from quote response
    QuoteItemResponse quoteItemResponseForLogical = QuoteHelper.fetchQuoteItemResponse(quoteResponse, QuoteItemNameType.LOGICAL);
    QuoteProductConfigResponse quoteProductConfigResponse = quoteItemResponseForLogical.getQuoteProduct().getQuoteProductConfig();

    //Set IP Range
    if (quoteProductConfigResponse.getIpRange() != null) {
      quoteItem.setIpRange(String.valueOf(quoteProductConfigResponse.getIpRange()));
    }

    logical.setActionflag(quoteProductConfigResponse.getAction().getValue());
    logical.setBandwidth(QuoteHelper.formatBandwidth(quoteProductConfigResponse.getBandwidth()));
    logical.setProfile(quoteProductConfigResponse.getProfile().name());

    quoteItem.setProduct(serviceClass);
    quoteItem.setLogical(logical);
  }

  /**
   * Method to build AEnd access and set in quote item.
   *
   * @param quoteItemResponseForAEnd QuoteItemResponse
   *
   * @return AEnd access
   */
  private static QuoteItem.AEND buildAEndAccess(QuoteItemResponse quoteItemResponseForAEnd) {
    QuoteItem.AEND aEnd = objectFactory.createQuoteItemAEND();
    QuoteItem.AEND.ACCESS aEndAccess = objectFactory.createQuoteItemAENDACCESS();
    QuoteProductConfigResponse quoteProductConfigResponse = quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductConfig();

    aEndAccess.setACTIONFLAG(quoteProductConfigResponse.getAction().getValue());

    if (quoteProductConfigResponse.getBandwidth() != null) {
      aEndAccess.setBANDWIDTH(QuoteHelper.formatBandwidth(quoteProductConfigResponse.getBandwidth()));
    }

    if (quoteProductConfigResponse.getSla() != null) {
      aEndAccess.setSLA(quoteProductConfigResponse.getSla().name());
    }

    if (quoteProductConfigResponse.getTargetAccessSupplier() != null) {
      aEndAccess.setTARGETACCESSSUPPLIER(quoteProductConfigResponse.getTargetAccessSupplier().getValue());
    }

    if (quoteProductConfigResponse.getAccessSupplier() != null) {
      aEndAccess.setREQACCESSSUPPLIER(quoteProductConfigResponse.getAccessSupplier().getValue());
    }
    aEnd.setACCESS(aEndAccess);
    return aEnd;
  }

  private static Aend buildAEndAccessDTO(QuoteItemResponse quoteItemResponseForAEnd) {
    Aend aEnd = Aend.builder().build();
    AendAccess aEndAccess = AendAccess.builder().build();
    QuoteProductConfigResponse quoteProductConfigResponse = quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductConfig();

    aEndAccess.setActionFlag(quoteProductConfigResponse.getAction().getValue());

    if (quoteProductConfigResponse.getBandwidth() != null) {
      aEndAccess.setBandwidth(QuoteHelper.formatBandwidth(quoteProductConfigResponse.getBandwidth()));
    }

    if (quoteProductConfigResponse.getSla() != null) {
      aEndAccess.setSla(quoteProductConfigResponse.getSla().name());
    }

    if (quoteProductConfigResponse.getTargetAccessSupplier() != null) {
      aEndAccess.setTargetAccessSupplier(quoteProductConfigResponse.getTargetAccessSupplier().getValue());
    }

    if (quoteProductConfigResponse.getAccessSupplier() != null) {
      aEndAccess.setReqAccessSupplier(quoteProductConfigResponse.getAccessSupplier().getValue());
    }
    aEnd.setAccess(aEndAccess);
    return aEnd;
  }

  /**
   * Method to build AEnd location and set in quote item.
   *
   * @param quoteItemResponseForAEnd QuoteItemResponse
   *
   * @return AEnd location.
   */
  private static QuoteItem.AEND.LOCATION buildAEndLocation(QuoteItemResponse quoteItemResponseForAEnd) {
    QuoteItem.AEND.LOCATION aEndLocation = objectFactory.createQuoteItemAENDLOCATION();
    aEndLocation.setEIRCODE(quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductPlace().getEircode());

    if (quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductPlace().getFullAddress() != null) {
      aEndLocation.setADDRESS(quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductPlace().getFullAddress());
    }

    if (quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductPlace().getMultiEircode() != null) {
      aEndLocation.setMULTIEIRCODE(quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductPlace().getMultiEircode());
    }

    if (quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductConfig().getNetworkStatus() != null) {
      aEndLocation.setNETWORKSTATUS(quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductConfig().getNetworkStatus().getValue());
    }
    return aEndLocation;
  }

  private static AendLocation buildAEndLocationDTO(QuoteItemResponse quoteItemResponseForAEnd) {
    AendLocation aEndLocation = AendLocation.builder().build();
    aEndLocation.setEircode(quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductPlace().getEircode());

    if (quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductPlace().getFullAddress() != null) {
      aEndLocation.setAddress(quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductPlace().getFullAddress());
    }

    if (quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductPlace().getMultiEircode() != null) {
      aEndLocation.setMultiEircode(quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductPlace().getMultiEircode());
    }

    if (quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductConfig().getNetworkStatus() != null) {
      aEndLocation.setNetworkStatus(quoteItemResponseForAEnd.getQuoteProduct().getQuoteProductConfig().getNetworkStatus().getValue());
    }
    return aEndLocation;
  }

  /**
   * Method to map QbtdcNotificationHeader from CreateQuoteResponse and Notification Object
   * @param createQuoteResponse CreateQuoteResponse object received from QuoteManager
   * @param notification Notcom Notification object
   * @return QbtdcNotificationHeader xml Object
   */
  static QbtdcNotificationHeader createNotificationHeader(CreateQuoteResponse createQuoteResponse,
      Notifications notification) {
    QbtdcNotificationHeader notificationHeader = objectFactory.createQbtdcNotificationHeader();
    notificationHeader.setDATACONTRACTNAME(QBTDC_DATA_CONTRACT_NAME);
    notificationHeader.setORIGINATORCODE(QBTDC_ORIGINATOR_CODE);
    notificationHeader.setDATETIMESTAMP(createQuoteResponse.getQuoteDate().format(BT_DATE_TIME_FORMAT));
    notificationHeader.setTRANSACTIONID(BigInteger.valueOf(notification != null ? notification.getTransactionId() : 0));
    return notificationHeader;
  }

  /**
   * Method to map QbtdcNotificationData from CreateQuoteResponse, QuoteOrderMap and Notification object
   * @param createQuoteResponse CreateQuoteResponse object received from QuoteManager
   * @param quoteOrderMap QuoteOrderMap for mapping OrderId and OrderNumber
   * @param notification Notcom Notification object
   * @return QbtdcNotificationData xml Object
   */
  static QbtdcNotificationData createNotificationData(CreateQuoteResponse createQuoteResponse,
      QuoteOrderMapEntity quoteOrderMap, Notifications notification) {
    QbtdcNotificationData notificationData = objectFactory.createQbtdcNotificationData();
    notificationData.setNAME(quoteOrderMap.getOperatorName());
    notificationData.setCODE(quoteOrderMap.getOperatorCode());
    notificationData.setORDERNUMBER(quoteOrderMap.getOrderNumber());
    notificationData.setMESSAGETYPE(notification != null ? notification.getType() : "R");
    notificationData.setMESSAGEDATE(notification != null ? notification.getCreatedAt().format(BT_DATE_TIME_FORMAT)
        : createQuoteResponse.getQuoteDate().format(BT_DATE_TIME_FORMAT));
    notificationData.setAPPLICATIONDATE(createQuoteResponse.getQuoteDate().format(BT_DATE_WITHOUT_TIME_FORMAT));
    notificationData.setRECEIVEDDATE(createQuoteResponse.getQuoteDate().format(BT_DATE_TIME_FORMAT));
    return notificationData;
  }

  /**
   * Method to get recurring Frequency of the quote
    * @param quoteResponse QuoteResponse received from Quote Manager
   * @return String recurring frequency of the quote
   */
  static String getNonRecurringPrice(QuoteResponse quoteResponse) {
    String nonRecurringPrice = null;
    for (QuotePriceResponse quotePrice : quoteResponse.getQuotePrice()) {
      if (quotePrice.getPriceType() == PriceType.NON_RECURRING) {
        nonRecurringPrice = quotePrice.getQuotePrice();
        break;
      }
    }
    return nonRecurringPrice;
  }

  /**
   * Method to get recurring price of the quote
   * @param quoteResponse QuoteResponse received from Quote Manager
   * @return String recurring price of the quote
   */
  static String getRecurringPrice(QuoteResponse quoteResponse) {
    String recurringPrice = null;
    for (QuotePriceResponse quotePrice : quoteResponse.getQuotePrice()) {
      if (quotePrice.getPriceType() == PriceType.RECURRING) {
        recurringPrice = quotePrice.getQuotePrice();
        break;
      }
    }
    return recurringPrice;
  }

  /**
   * Method to get wagQuoteItemId of the quote
   *
   * @param quoteItemMapList QuoteItemMap List for the QuoteGroup
   * @param quoteId Talos quoteId of the quote
   *
   * @return Long wagQuoteId of the quote
   */
  static Long getWagQuoteItemId(List<QuoteItemMapEntity> quoteItemMapList, Long quoteId) {
    return quoteItemMapList
        .stream()
        .filter(quoteItemMap -> quoteItemMap.getQuoteId().equals(quoteId))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException(String.format("Quote id %d is not found in  QuoteItemMapEntity", quoteId)))
        .getWagQuoteId();
  }

  /**
   * Method to build QBTDC order response
   * @param wagOrderId Wag order id
   * @return QBTDC order response
   */
  private static QBTDCOrderResponse createQBTDCOrderResponse(Long wagOrderId) {
    QBTDCOrderResponse qbtdcOrderResponse = new QBTDCOrderResponse();
    qbtdcOrderResponse.setVERSION(QBTDC_CONTRACT_VERSION);
    qbtdcOrderResponse.setORDERID(wagOrderId);
    return qbtdcOrderResponse;
  }

  private static QBTDCOrderResponseDTO createQbtdcOrderResponseDTO(long wagOrderId) {
    return QBTDCOrderResponseDTO.builder().wagOrderId(wagOrderId).version(QBTDC_CONTRACT_VERSION).build();
  }

  static NotificationDataResponse createNotificationResponseDTO(CreateQuoteResponse createQuoteResponse,
                                                                QuoteOrderMapEntity quoteOrderMap, Notifications notification) {

    return NotificationDataResponse.builder().dataContractName(QBTDC_DATA_CONTRACT_NAME).originatorCode(QBTDC_ORIGINATOR_CODE)
            .dateTimeStamp(createQuoteResponse.getQuoteDate().format(BT_DATE_TIME_FORMAT))
            .transactionId(BigInteger.valueOf(notification != null ? notification.getTransactionId() : 0))
            .name(quoteOrderMap.getOperatorName())
            .code(quoteOrderMap.getOperatorCode())
            .ordernumber(quoteOrderMap.getOrderNumber())
            .messagetype(notification != null ? notification.getType() : "R")
            .messagedate(notification != null ? notification.getCreatedAt().format(BT_DATE_TIME_FORMAT)
                    : createQuoteResponse.getQuoteDate().format(BT_DATE_TIME_FORMAT))
            .applicationdate(createQuoteResponse.getQuoteDate().format(BT_DATE_WITHOUT_TIME_FORMAT))
            .receiveddate(createQuoteResponse.getQuoteDate().format(BT_DATE_TIME_FORMAT))
            .build();
  }
}
