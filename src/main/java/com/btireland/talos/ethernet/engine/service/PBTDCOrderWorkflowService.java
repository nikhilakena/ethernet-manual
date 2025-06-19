package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.client.asset.ordermanager.AgentDTO;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.domain.Quote;
import com.btireland.talos.ethernet.engine.dto.EmailDTO;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.exception.NotificationClientException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceNotFoundException;
import com.btireland.talos.ethernet.engine.facade.PbtdcMapper;
import com.btireland.talos.ethernet.engine.facade.QBTDCOrderMapper;
import com.btireland.talos.ethernet.engine.mq.CerberusDataSyncMessageProducer;
import com.btireland.talos.ethernet.engine.util.BTNotificationTypes;
import com.btireland.talos.ethernet.engine.util.QuoteStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants.OrderValidationProcess.PROCESS_PBTDC_ORDER;

@Slf4j
@Service
@Transactional
public class PBTDCOrderWorkflowService {

    private static final String TALOS_COMPLETE = "talos complete";

    private RuntimeService runtimeService;

    private PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService;

    private NotComNotificationsService notComNotificationsService;

    private CerberusDataSyncMessageProducer dataSyncMessageProducer;

    private PbtdcMapper pbtdcMapper;

    private SealService sealService;

    private MailContentBuilderService mailContentBuilderService;

    private ApplicationConfiguration applicationConfiguration;

    private OrderManagerService orderManagerService;

    private QBTDCOrderMapper qbtdcOrderMapper;

    private InterventionService interventionService;

    public PBTDCOrderWorkflowService(RuntimeService runtimeService,
                                     PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService,
                                     NotComNotificationsService notComNotificationsService,
                                     CerberusDataSyncMessageProducer dataSyncMessageProducer, PbtdcMapper pbtdcMapper, SealService sealService,
                                     MailContentBuilderService mailContentBuilderService,
                                     ApplicationConfiguration applicationConfiguration,
                                     OrderManagerService orderManagerService, QBTDCOrderMapper qbtdcOrderMapper,
                                     InterventionService interventionService) {
        this.runtimeService = runtimeService;
        this.pbtdcOrdersPersistenceService = pbtdcOrdersPersistenceService;
        this.notComNotificationsService = notComNotificationsService;
        this.dataSyncMessageProducer = dataSyncMessageProducer;
        this.pbtdcMapper = pbtdcMapper;
        this.sealService = sealService;
        this.mailContentBuilderService = mailContentBuilderService;
        this.applicationConfiguration = applicationConfiguration;
        this.orderManagerService = orderManagerService;
        this.qbtdcOrderMapper = qbtdcOrderMapper;
        this.interventionService = interventionService;
    }

    public void processPbtdcOrders(long wagOrderId) {
        runtimeService.startProcessInstanceByKey(PROCESS_PBTDC_ORDER, String.valueOf(wagOrderId));
    }

    private void createNotification(Long orderId, BTNotificationTypes notificationType) throws JsonProcessingException, NotificationClientException {
        //Retrieve orders from local DB
        Pbtdc pbtdcOrder = pbtdcOrdersPersistenceService.findByOrderId(orderId);

        //Update with relevant info
        pbtdcOrder.setLastNotificationType(notificationType.name());

        //Invoke Notcom to generate 'U' notification
        notComNotificationsService.createNotification(notificationType.name(), pbtdcOrder);

    }

    public void processUndeliveredNotification(Long orderId) throws JsonProcessingException,
            NotificationClientException {

        createNotification(orderId, BTNotificationTypes.U);
    }

    public void processPOANotification(Long orderId) throws JsonProcessingException, NotificationClientException, OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException {
        //Retrieve orders from local DB
        Pbtdc order = pbtdcOrdersPersistenceService.findByOrderId(orderId);
        createNotification(orderId, BTNotificationTypes.POA);
        //Quote Status as 'Ordered'
        updateQuoteStatus(order);
    }

    public void sendAppointmentRequestNotification(Long orderId) throws JsonProcessingException,
            NotificationClientException {
        createNotification(orderId, BTNotificationTypes.APTR);

    }

    public void sendStatusNotification(Long orderId) throws JsonProcessingException, NotificationClientException {
        createNotification(orderId, BTNotificationTypes.S);

    }


    public void sendDelayStartNotification(long orderId) throws JsonProcessingException, NotificationClientException {
        createNotification(orderId, BTNotificationTypes.DS);

    }

    public void sendDelayEndNotification(long orderId) throws JsonProcessingException, NotificationClientException {
        createNotification(orderId, BTNotificationTypes.DE);
    }


    public void sendConfirmationNotification(Long orderId) throws JsonProcessingException, NotificationClientException {
        createNotification(orderId, BTNotificationTypes.CF);
    }

    public void sendNotesNotification(Long orderId) throws JsonProcessingException, NotificationClientException {
        createNotification(orderId, BTNotificationTypes.N);
    }

    public void sendAcceptNotification(Long orderId) throws JsonProcessingException, NotificationClientException {
        createNotification(orderId, BTNotificationTypes.A);
    }

    public void sendCircuitIdNotification(long orderId) throws JsonProcessingException, NotificationClientException {
        createNotification(orderId, BTNotificationTypes.CI);
    }

    public void sendCompleteNotification(Long orderId) throws JsonProcessingException, NotificationClientException {
        createNotification(orderId, BTNotificationTypes.C);
    }

    public void sendRejectNotification(Long orderId) throws JsonProcessingException, NotificationClientException {
        createNotification(orderId, BTNotificationTypes.R);

    }

    public void submitPBTDCOrder(long orderId) {
        //Retrieve orders from local DB
        Pbtdc order = pbtdcOrdersPersistenceService.findByOrderId(orderId);

        //Populate process instance Order data
        order.setInternalTrackingOrderReference(generateInternalOrderTrackingRef(order.getWagOrderId(),
                order.getServiceType()));

        OrdersDTO ordersDTO = pbtdcMapper.toOrderDTO(order);

        if (ordersDTO.getPbtdc().getWholesalerAccess() != null && ordersDTO.getPbtdc().getWholesalerAccess().getSite() != null && ordersDTO.getPbtdc().getWholesalerAccess().getSite().getLocation() != null) {
            String handoverCode = ordersDTO.getPbtdc().getWholesalerAccess().getSite().getLocation().getId();
            String handoverDescription = applicationConfiguration.getHandoverMap().get(handoverCode);
            if (handoverDescription != null) {
                ordersDTO.getPbtdc().getWholesalerAccess().getSite().getLocation().setId(handoverDescription);
            }
        }

        Optional<AgentDTO> agentDTO = orderManagerService.getAgentByWagOrderId(orderId);
        if (agentDTO.isPresent()) {
            ordersDTO.setAgent(agentDTO.get().getName());
            ordersDTO.setAgentEmail(agentDTO.get().getEmail());
        } else {
            // This is usally not a fatal error; agent details are not expected to be present when an order is placed
            // via the WAG.
            log.info("Agent details not found for WAG Order " + orderId);
        }

        EmailDTO emailDTO = mailContentBuilderService.build(ordersDTO);
        sealService.createPbtdcOrderForSeal(emailDTO);

    }

    /* Code to generate Internal Supplier Order Reference Number*/
    private String generateInternalOrderTrackingRef(Long orderId, String orderType) {
        return "TALOS-" + orderId.toString() + "-" + orderType + "-" + 1;
    }

    public void updateOrderStatus(long orderId) {
        //Retrieve orders from local DB
        Pbtdc order = pbtdcOrdersPersistenceService.findByOrderId(orderId);
        order.setOrderStatus(TALOS_COMPLETE);

        OrdersDTO ordersDTO = pbtdcMapper.toOrderDTO(order);
        //Push process Order data for Order Manager for cerberus data sync
        dataSyncMessageProducer.sendOrderData(ordersDTO);

    }

    private void updateQuoteStatus(Pbtdc order) throws OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException {
        Quote quote = order.getQuote();
        quote.setStatus(QuoteStatus.ORDERED.getValue());
        orderManagerService.updateQuote(qbtdcOrderMapper.toQuoteDTO(quote));
    }
}
