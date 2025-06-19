package com.btireland.talos.ethernet.engine.service;


import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QuoteDTO;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.dto.EmailDTO;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.exception.NotificationClientException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceNotFoundException;
import com.btireland.talos.ethernet.engine.facade.PbtdcMapper;
import com.btireland.talos.ethernet.engine.facade.QBTDCOrderMapper;
import com.btireland.talos.ethernet.engine.mq.CerberusDataSyncMessageProducer;
import com.btireland.talos.ethernet.engine.util.AgentsDTOFactory;
import com.btireland.talos.ethernet.engine.util.BTNotificationTypes;
import com.btireland.talos.ethernet.engine.util.EmailDTOFactory;
import com.btireland.talos.ethernet.engine.util.OrderFactory;
import com.btireland.talos.ethernet.engine.util.OrdersDTOFactory;
import com.btireland.talos.ethernet.engine.util.PBTDCOrderDTOFactory;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@UnitTest
class PBTCOrderWorkflowServiceTest {

    @Mock
    PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService;
    @Mock
    MessageCorrelationBuilder messageCorrelationBuilder;
    @Mock
    private RuntimeService runtimeService;
    @Mock
    private NotComNotificationsService notComNotificationsService;

    private PBTDCOrderWorkflowService pbtdcOrderWorkflowService;

    private PbtdcMapper pbtdcMapper;

    private CerberusDataSyncMessageProducer dataSyncMessageProducer;

    private SealService sealService;

    private MailContentBuilderService mailContentBuilderService;

    private ApplicationConfiguration applicationConfiguration;

    private OrderManagerService orderManagerService;

    private QBTDCOrderMapper qbtdcOrderMapper;

    private InterventionService interventionService;

    @BeforeEach
    public void setUp() {
        runtimeService = Mockito.mock(RuntimeService.class);
        notComNotificationsService = Mockito.mock(NotComNotificationsService.class);
        messageCorrelationBuilder = Mockito.mock(MessageCorrelationBuilder.class);
        pbtdcOrdersPersistenceService = Mockito.mock(PbtdcOrdersPersistenceService.class);
        pbtdcMapper = Mockito.mock(PbtdcMapper.class);
        dataSyncMessageProducer = Mockito.mock(CerberusDataSyncMessageProducer.class);
        sealService = Mockito.mock(SealService.class);
        mailContentBuilderService = Mockito.mock(MailContentBuilderService.class);
        applicationConfiguration = Mockito.mock(ApplicationConfiguration.class);
        orderManagerService = Mockito.mock(OrderManagerService.class);
        qbtdcOrderMapper = Mockito.mock(QBTDCOrderMapper.class);
        interventionService = Mockito.mock(InterventionService.class);
        pbtdcOrderWorkflowService = new PBTDCOrderWorkflowService(runtimeService, pbtdcOrdersPersistenceService,
                notComNotificationsService, dataSyncMessageProducer, pbtdcMapper, sealService,
                mailContentBuilderService, applicationConfiguration, orderManagerService, qbtdcOrderMapper,
                interventionService);
    }

    @Test
    @DisplayName("Notification is sent to notcom for persistance")
    void processUndeliveredNotification() throws JsonProcessingException, NotificationClientException {
        Pbtdc orders = OrderFactory.defaultPbtdcOrders();
        when(pbtdcOrdersPersistenceService.findByOrderId(anyLong())).thenReturn(orders);
        pbtdcOrderWorkflowService.processUndeliveredNotification(123L);
        orders.setLastNotificationType("U");
        verify(notComNotificationsService).createNotification("U", orders);

    }

    @Test
    @DisplayName("Process POA notification")
    void processPOANotification() throws OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException, JsonProcessingException, NotificationClientException {
        Long orderId = 123L;
        Pbtdc order = OrderFactory.enrichedPbtdcOrdersWithQuoteItem();
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(orderId)).thenReturn(order);
        Mockito.when(qbtdcOrderMapper.toQuoteDTO(order.getQuote())).thenReturn(QBTDCOrderDTOFactory.defaultQuoteDTO());
        pbtdcOrderWorkflowService.processPOANotification(orderId);
        Mockito.verify(notComNotificationsService, times(1)).createNotification(BTNotificationTypes.POA.name(), order);
        Mockito.verify(orderManagerService).updateQuote(Mockito.any(QuoteDTO.class));
    }

    @Test
    @DisplayName("DS Notification is processed")
    void processDSNotification() throws JsonProcessingException, NotificationClientException {
        Pbtdc orders = OrderFactory.defaultPbtdcOrders();
        when(pbtdcOrdersPersistenceService.findByOrderId(anyLong())).thenReturn(orders);
        pbtdcOrderWorkflowService.sendDelayStartNotification(123L);
        orders.setLastNotificationType("DS");
        verify(notComNotificationsService).createNotification("DS", orders);
    }

    @Test
    @DisplayName("DE Notification is processed")
    void processDENotification() throws JsonProcessingException, NotificationClientException {
        Pbtdc orders = OrderFactory.defaultPbtdcOrders();
        when(pbtdcOrdersPersistenceService.findByOrderId(anyLong())).thenReturn(orders);
        pbtdcOrderWorkflowService.sendDelayEndNotification(123L);
        orders.setLastNotificationType("DE");
        verify(notComNotificationsService).createNotification("DE", orders);
    }


    @Test
    @DisplayName("Process APTR notification")
    void sendAppointmentRequestNotification() throws Exception {
        Long orderId = 123L;
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(orderId)).thenReturn(order);
        pbtdcOrderWorkflowService.sendAppointmentRequestNotification(orderId);
        Mockito.verify(notComNotificationsService, times(1)).createNotification(BTNotificationTypes.APTR.name(), order);
    }

    @Test
    @DisplayName("Process CF notification")
    void sendConfirmationNotification() throws Exception {
        Long orderId = 123L;
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(orderId)).thenReturn(order);
        pbtdcOrderWorkflowService.sendConfirmationNotification(orderId);
        Mockito.verify(notComNotificationsService, times(1)).createNotification(BTNotificationTypes.CF.name(), order);
    }

    @Test
    @DisplayName("Process N notification")
    void sendNotesNotification() throws Exception {
        Long orderId = 123L;
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(orderId)).thenReturn(order);
        pbtdcOrderWorkflowService.sendNotesNotification(orderId);
        Mockito.verify(notComNotificationsService, times(1)).createNotification(BTNotificationTypes.N.name(), order);
    }

    @Test
    @DisplayName("Update order status")
    void updateOrderStatus() throws Exception {
        Long orderId = 123L;
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        OrdersDTO ordersDTO = PBTDCOrderDTOFactory.defaultOrdersDTO();
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(orderId)).thenReturn(order);
        Mockito.when(pbtdcMapper.toOrderDTO(order)).thenReturn(ordersDTO);
        pbtdcOrderWorkflowService.updateOrderStatus(orderId);
        Mockito.verify(dataSyncMessageProducer, times(1)).sendOrderData(ordersDTO);
    }

    @Test
    @DisplayName("Send Accept notification")
    void sendAcceptNotification() throws Exception {
        Long orderId = 123L;
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(orderId)).thenReturn(order);
        pbtdcOrderWorkflowService.sendAcceptNotification(orderId);
        Mockito.verify(notComNotificationsService, times(1)).createNotification(BTNotificationTypes.A.name(), order);
    }

    @Test
    @DisplayName("Send CI notification")
    void sendCircuitIdNotification() throws Exception {
        Long orderId = 123L;
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(orderId)).thenReturn(order);
        pbtdcOrderWorkflowService.sendCircuitIdNotification(orderId);
        Mockito.verify(notComNotificationsService, times(1)).createNotification(BTNotificationTypes.CI.name(), order);
    }

    @Test
    @DisplayName("Send Complete notification")
    void sendCompleteNotification() throws Exception {
        Long orderId = 123L;
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(orderId)).thenReturn(order);
        pbtdcOrderWorkflowService.sendCompleteNotification(orderId);
        Mockito.verify(notComNotificationsService, times(1)).createNotification(BTNotificationTypes.C.name(), order);
    }

    @Test
    @DisplayName("Submit PBTDC Order")
    void submitPBTDCOrder()  {
        Long orderId = 123L;
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        OrdersDTO ordersDTO = OrdersDTOFactory.ordersDTOWithHandoverCode();
        EmailDTO emailDTO = EmailDTOFactory.defaultEmailDTO();
        Map<String, String> handoverMap = new HashMap<>();
        handoverMap.put("BT_CITYWEST", "BT Citywest - Unit 4029 Citywest");
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(orderId)).thenReturn(order);
        Mockito.when(pbtdcMapper.toOrderDTO(order)).thenReturn(ordersDTO);
        Mockito.when(orderManagerService.getAgentByWagOrderId(ordersDTO.getOrderId())).thenReturn(AgentsDTOFactory.defaultAgentDTO());
        Mockito.when(mailContentBuilderService.build(ordersDTO)).thenReturn(emailDTO);
        Mockito.when(applicationConfiguration.getHandoverMap()).thenReturn(handoverMap);
        pbtdcOrderWorkflowService.submitPBTDCOrder(orderId);
        Mockito.verify(pbtdcMapper, times(1)).toOrderDTO(order);
        Mockito.verify(mailContentBuilderService, times(1)).build(ordersDTO);
        Mockito.verify(sealService, times(1)).createPbtdcOrderForSeal(emailDTO);
        Assertions.assertEquals(ordersDTO.getPbtdc().getWholesalerAccess().getSite().getLocation().getId(),
                handoverMap.get("BT_CITYWEST"));
    }
}
