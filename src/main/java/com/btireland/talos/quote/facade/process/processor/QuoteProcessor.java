package com.btireland.talos.quote.facade.process.processor;

import com.btireland.talos.core.common.rest.exception.checked.TalosBadRequestException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.OrderManagerClient;
import com.btireland.talos.ethernet.engine.dto.QBTDCOrderResponseDTO;
import com.btireland.talos.ethernet.engine.exception.PersistanceException;
import com.btireland.talos.ethernet.engine.repository.QuoteOrderMapRepostiory;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrder;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrderResponse;
import com.btireland.talos.quote.facade.base.constant.QuoteManagerConstants;
import com.btireland.talos.quote.facade.base.enumeration.internal.NotificationType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteStateType;
import com.btireland.talos.quote.facade.connector.rest.quotemanager.QuoteManagerClient;
import com.btireland.talos.quote.facade.domain.entity.QuoteEmailEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.ordermanager.Quote;
import com.btireland.talos.quote.facade.dto.ordermanager.QuoteOrder;
import com.btireland.talos.quote.facade.dto.quotemanager.request.CreateQuoteRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.response.CreateQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.RejectionDetailsResponse;
import com.btireland.talos.quote.facade.process.helper.QuoteHelper;
import com.btireland.talos.quote.facade.process.mapper.ordermanager.OrderManagerRequestMapper;
import com.btireland.talos.quote.facade.process.mapper.quotemanager.QuoteManagerRequestMapper;
import com.btireland.talos.quote.facade.process.mapper.quotemanager.QuoteManagerResponseMapper;
import com.btireland.talos.quote.facade.workflow.QuoteProcessConstants;
import org.apache.commons.lang3.BooleanUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class QuoteProcessor {

  public static final Logger LOGGER = LoggerFactory.getLogger(QuoteProcessor.class);

  private final QuoteHelper quoteHelper;
  private final OrderManagerClient orderManagerClient;
  private final QuoteManagerClient quoteManagerClient;
  private final NotComNotificationsProcessor notComNotificationsProcessor;
  private final RuntimeService runtimeService;

  public QuoteProcessor(QuoteHelper quoteHelper, OrderManagerClient orderManagerClient,
                        QuoteManagerClient quoteManagerClient, NotComNotificationsProcessor notComNotificationsProcessor,
                        RuntimeService runtimeService) {
    this.quoteHelper = quoteHelper;
    this.orderManagerClient = orderManagerClient;
    this.quoteManagerClient = quoteManagerClient;
    this.notComNotificationsProcessor = notComNotificationsProcessor;
    this.runtimeService = runtimeService;
  }

  /**
   * Method to create quote by calling quote manager API
   *
   * @param qbtdcOrderRequest QBTDCOrder xml object
   * @param oao               oao of the order
   * @return QBTDCOrderResponse xml object
   */
  @Transactional
  public QBTDCOrderResponse   createQuote(QBTDCOrder qbtdcOrderRequest, String oao)
      throws PersistanceException {
    QBTDCOrderResponse qbtdcOrderResponse = null;
    try {
      //Saving operator code, operator name and sync flag in quote order map
      QuoteOrderMapEntity quoteOrderMap = generateQuoteOrderMapEntity(qbtdcOrderRequest, oao);

      //Map xml request to Quote Manager request
      CreateQuoteRequest createQuoteRequest = QuoteManagerRequestMapper
          .createQuoteManagerRequest(qbtdcOrderRequest, quoteOrderMap.getQuoteGroupId(), oao);

      //Calling Quote Manager to create quotes and get pricing info
      CreateQuoteResponse createQuoteResponse = createQuote(createQuoteRequest);

      //Calling order manager to save the quote order data in wag database
      QuoteOrder orderManagerResponse = null;
      Notifications notification = null;

      LOGGER.error("create quote response from quote manager {}", createQuoteResponse);

      if(!(CollectionUtils.isEmpty(createQuoteResponse.getQuotes()))) {
        orderManagerResponse = saveQuoteInWagDatabase(
            OrderManagerRequestMapper.createOrderManagerRequest(createQuoteResponse, quoteOrderMap));

        LOGGER.error("order manager response {}", orderManagerResponse);

        //Map order number in quote order map
        quoteOrderMap.setOrderNumber(orderManagerResponse.getOrders().getOrderNumber());

        //generate quote item map
        quoteOrderMap.setQuoteItemMapList(generateQuoteItemMapList(createQuoteResponse.getQuotes(), orderManagerResponse.getQuoteList(), quoteOrderMap));

        // Calling Notcom notification API
        notification = notComNotificationsProcessor.createNotification(createQuoteResponse,orderManagerResponse.getOrders().getOrderId(), quoteOrderMap);
      }

      //Generating Response for NOG based on Notification Type received from quote manager
      qbtdcOrderResponse = null;
      if (createQuoteResponse.getNotificationType() == NotificationType.COMPLETE) {
        quoteOrderMap.setStatus(QuoteOrderMapStatus.COMPLETE);
        qbtdcOrderResponse = QuoteManagerResponseMapper
            .createQBTDCCompletionResponse(createQuoteResponse, quoteOrderMap, notification, orderManagerResponse.getOrders().getOrderId());
      } else if (createQuoteResponse.getNotificationType() == NotificationType.REJECT) {
        quoteOrderMap.setStatus(QuoteOrderMapStatus.REJECT);
        qbtdcOrderResponse = QuoteManagerResponseMapper
            .createQBTDCOrderRejectResponse(createQuoteResponse, quoteOrderMap, notification,
                orderManagerResponse  != null ? orderManagerResponse.getOrders().getOrderId() : 0);
      } else if (createQuoteResponse.getNotificationType() == NotificationType.WSA) {
        quoteOrderMap.setStatus((QuoteOrderMapStatus.WSA));
        qbtdcOrderResponse = QuoteManagerResponseMapper
            .createQBTDCOrderWSAResponse(createQuoteResponse, quoteOrderMap, notification,
                orderManagerResponse.getOrders().getOrderId());

        //Start Camunda Process for Offline Pricing from Business Console
        startOfflineQuotingProcess(createQuoteResponse);
      }
      //update quoteOrderMap
      quoteHelper.saveQuoteOrderMap(quoteOrderMap);
    } catch (Exception e) {
      LOGGER.error("Error while creating QBTDC Order response from persistence ", e);
      throw new PersistanceException("WS10: Unexpected Failure.");
    }

    LOGGER.error("qbtdc order response {}", qbtdcOrderResponse);

    return qbtdcOrderResponse;
  }

  @Transactional
  public QBTDCOrderResponseDTO createQBTDCQuote(QuoteOrderMapEntity quoteOrderMap, CreateQuoteRequest createQuoteRequest) throws PersistanceException {
    QBTDCOrderResponseDTO qbtdcOrderResponse = null;
    try {

      quoteOrderMap = quoteHelper.saveQuoteOrderMap(quoteOrderMap);
      createQuoteRequest.setQuoteGroupId(quoteOrderMap.getQuoteGroupId());
      //Calling Quote Manager to create quotes and get pricing info
      CreateQuoteResponse createQuoteResponse = createQuote(createQuoteRequest);

      //Calling order manager to save the quote order data in wag database
      QuoteOrder orderManagerResponse = null;
      Notifications notification = null;

      LOGGER.error("create quote response from quote manager {}", createQuoteResponse);

      if(!(CollectionUtils.isEmpty(createQuoteResponse.getQuotes()))) {
        orderManagerResponse = saveQuoteInWagDatabase(
                OrderManagerRequestMapper.createOrderManagerRequest(createQuoteResponse, quoteOrderMap));

        LOGGER.error("order manager response {}", orderManagerResponse);

        //Map order number in quote order map
        quoteOrderMap.setOrderNumber(orderManagerResponse.getOrders().getOrderNumber());

        //generate quote item map
        quoteOrderMap.setQuoteItemMapList(generateQuoteItemMapList(createQuoteResponse.getQuotes(), orderManagerResponse.getQuoteList(), quoteOrderMap));

        // Calling Notcom notification API
        notification = notComNotificationsProcessor.createNotification(createQuoteResponse,orderManagerResponse.getOrders().getOrderId(), quoteOrderMap);
      }

      //Generating Response for NOG based on Notification Type received from quote manager
      qbtdcOrderResponse = null;
      if (createQuoteResponse.getNotificationType() == NotificationType.COMPLETE) {
        quoteOrderMap.setStatus(QuoteOrderMapStatus.COMPLETE);
        qbtdcOrderResponse = QuoteManagerResponseMapper.createQBTDCCompletionResponseDTO(createQuoteResponse,quoteOrderMap,notification,orderManagerResponse.getOrders().getOrderId());
      } else if (createQuoteResponse.getNotificationType() == NotificationType.REJECT) {
        quoteOrderMap.setStatus(QuoteOrderMapStatus.REJECT);
        qbtdcOrderResponse = QuoteManagerResponseMapper.createQBTDCOrderRejectResponseDTO(createQuoteResponse, quoteOrderMap, notification, orderManagerResponse  != null ? orderManagerResponse.getOrders().getOrderId() : 0);
      } else if (createQuoteResponse.getNotificationType() == NotificationType.WSA) {
        quoteOrderMap.setStatus((QuoteOrderMapStatus.WSA));
        qbtdcOrderResponse = QuoteManagerResponseMapper.createQBTDCOrderWSAResponseDTO(createQuoteResponse,quoteOrderMap,notification,orderManagerResponse.getOrders().getOrderId());

        //Start Camunda Process for Offline Pricing from Business Console
        startOfflineQuotingProcess(createQuoteResponse);
      }
      //update quoteOrderMap
      quoteHelper.saveQBTDCQuoteOrderMap(quoteOrderMap);
    } catch (Exception e) {
      LOGGER.error("Error while creating QBTDC Order response from persistence ", e);
      throw new PersistanceException("WS10: Unexpected Failure.");
    }

    LOGGER.error("qbtdc order response {}", qbtdcOrderResponse);

    return qbtdcOrderResponse;
  }

  private CreateQuoteResponse createQuote(CreateQuoteRequest createQuoteRequest){
    CreateQuoteResponse createQuoteResponse;
    try {
      createQuoteResponse = quoteManagerClient.createQuote(createQuoteRequest);
    } catch (TalosBadRequestException | TalosNotFoundException ex) {
      // Deal with the technical validation failure
      createQuoteResponse = new CreateQuoteResponse(createQuoteRequest.getQuoteGroupId(),
          createQuoteRequest.getQuoteDate(), NotificationType.REJECT, new RejectionDetailsResponse(
          QuoteManagerConstants.GENERIC_ERROR_CODE, QuoteManagerConstants.GENERIC_ERROR_REASON, LocalDateTime.now()));

    }
    return createQuoteResponse;
  }

  /**
   * Method to generate QuoteOrderMapEntity
   *
   * @param qbtdcOrderRequest QBTDCOrder xml object
   * @return Saved QuoteOrderMapEntity
   */
  private QuoteOrderMapEntity generateQuoteOrderMapEntity(QBTDCOrder qbtdcOrderRequest, String oao) {
    String supplier = qbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getOBO() != null
            ? qbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getOBO() : oao;
    QuoteOrderMapEntity quoteOrderMap =
            new QuoteOrderMapEntity(qbtdcOrderRequest.getORDER().getORDERDATA().getOPERATORDETAILS().getCODE(),
                                    qbtdcOrderRequest.getORDER().getORDERDATA().getOPERATORDETAILS().getNAME(),
                                    qbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getORDERNUMBER(),
                                    BooleanUtils.toBooleanObject(qbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getSYNCREQUIRED()), supplier);
    if (qbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getEMAILRECIPIENTS() != null) {
      quoteOrderMap.setQuoteEmails(buildQuoteEmails(qbtdcOrderRequest, quoteOrderMap));
    }
    return quoteHelper.saveQuoteOrderMap(quoteOrderMap);
  }

  /**
   * Method to generate QuoteItemMapList from QuoteResponse List and QuoteDTO List
   *
   * @param quoteResponses QuoteResponse List received from QuoteManager
   * @param wagResponses      QuoteDTO List received from Order Manager
   * @param quoteOrderMap     QuoteOrderMap
   * @return List of QuoteItemMapEntity
   */
  private List<QuoteItemMapEntity> generateQuoteItemMapList(List<QuoteResponse> quoteResponses,
                                                            List<Quote> wagResponses, QuoteOrderMapEntity quoteOrderMap) {

    return IntStream.range(0, quoteResponses.size())
        .mapToObj(i -> new QuoteItemMapEntity(quoteResponses.get(i).getQuoteId(), wagResponses.get(i).getId(), quoteOrderMap))
        .collect(Collectors.toList());
  }

  /**
   * Method to save Quote order in Wag Database
   *
   * @param quoteOrderRequest QBTDCOrderDTO (order data) to be saved
   * @return Saved QBTDCOrderDTO
   */
  private QuoteOrder saveQuoteInWagDatabase(QuoteOrder quoteOrderRequest) {
    return orderManagerClient.createQBTDCOrder(quoteOrderRequest);
  }

  /**
   * Method to start Camunda Process for Offline Quoting
   * @param createQuoteResponse
   */
  private void startOfflineQuotingProcess(CreateQuoteResponse createQuoteResponse) {
    List<QuoteStateType> errorOrRejectedState = Arrays.asList(QuoteStateType.REJECTED, QuoteStateType.ERROR);

    List<Long> quoteIds = createQuoteResponse.getQuotes()
        .stream()
        .filter(quoteResponse -> errorOrRejectedState.contains(quoteResponse.getQuoteState()))
        .map(quoteResponse -> quoteResponse.getQuoteId())
        .collect(Collectors.toList());

    Map<String,Object> variables= new HashMap<>();
    variables.put(QuoteProcessConstants.VAR_NAME_QUOTE_ID_LIST, quoteIds);

    runtimeService.startProcessInstanceByKey(QuoteProcessConstants.PROC_DEF_KEY, String.valueOf(createQuoteResponse.getQuoteGroupId()), variables);
  }

  private List<QuoteEmailEntity> buildQuoteEmails(QBTDCOrder qbtdcOrderRequest,QuoteOrderMapEntity quoteOrderMap) {
    return qbtdcOrderRequest.getORDER().getORDERDATA().getORDERDETAILS().getEMAILRECIPIENTS().getEMAIL()
            .stream()
            .map(mail->new QuoteEmailEntity(quoteOrderMap,mail))
            .collect(Collectors.toList());

  }

}
