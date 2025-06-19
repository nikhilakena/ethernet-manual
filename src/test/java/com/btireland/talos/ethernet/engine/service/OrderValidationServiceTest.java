package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCOrderDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QuoteDetailsDTO;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.exception.UserUnauthorizedException;
import com.btireland.talos.ethernet.engine.exception.ValidationException;
import com.btireland.talos.ethernet.engine.facade.QBTDCOrderMapper;
import com.btireland.talos.ethernet.engine.util.OrderFactory;
import com.btireland.talos.ethernet.engine.util.PBTDCOrderDTOFactory;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@UnitTest
@ExtendWith(MockitoExtension.class)
class OrderValidationServiceTest {

    private static final String TALOS_COMPLETE = "talos complete";
    @Mock
    PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService;
    @InjectMocks
    OrderValidationService orderValidationService;
    @Captor
    ArgumentCaptor<Pbtdc> updatedOrderCaptor;
    @Mock
    private OrderManagerService orderManagerService;
    @Mock
    private QBTDCOrderMapper qbtdcOrderMapper;

    @Test
    @DisplayName("Order is valid and quote details are updated")
    void checkQuoteForOrder() throws Exception {
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        Pbtdc expected = OrderFactory.enrichedPbtdcOrdersWithQuoteItem();
        QuoteDetailsDTO quoteDetailsDTO = QBTDCOrderDTOFactory.defaultQuoteDetailsDTO();
        Mockito.when(orderManagerService.getQuoteItem(1L)).thenReturn(Optional.of(quoteDetailsDTO));
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(order.getWagOrderId())).thenReturn(order);
        Mockito.when(qbtdcOrderMapper.toQuote(quoteDetailsDTO.getQuote(), quoteDetailsDTO.getQbtdcs(), quoteDetailsDTO.getOrders())).thenReturn(expected.getQuote());
        orderValidationService.validateAndPersistQuote(order.getWagOrderId());
        Mockito.verify(pbtdcOrdersPersistenceService).update(updatedOrderCaptor.capture());
        Pbtdc updatedOrder = updatedOrderCaptor.getValue();
        Assertions.assertThat(updatedOrder).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Order and quote oao are different and rejectiondetails is updated")
    void checkQuoteForOrder_customerUnauthorized() throws Exception {
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        QuoteDetailsDTO quoteDetailsDTO = QBTDCOrderDTOFactory.defaultQuoteDetailsDTO();
        quoteDetailsDTO.getQuote().setOao("sky2");
        Mockito.when(orderManagerService.getQuoteItem(1L)).thenReturn(Optional.of(quoteDetailsDTO));
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(order.getWagOrderId())).thenReturn(order);
        Assertions.assertThatThrownBy(() -> orderValidationService.validateAndPersistQuote(order.getWagOrderId()))
                .isInstanceOf(UserUnauthorizedException.class);
        Mockito.verify(pbtdcOrdersPersistenceService, Mockito.times(1)).update(any(Pbtdc.class));
        Assertions.assertThat(order.getRejectionDetails()).usingRecursiveComparison().ignoringFields("orders").isEqualTo(OrderFactory.rejectionDetailsForInvalidQuote());

    }

    @Test
    @DisplayName("Order is duplicate exception is thrown and rejectiondetails is updated")
    void checkDuplicateOrder() throws Exception {
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        order.setRefQuoteItemId(1L);
        QuoteDetailsDTO quoteDetailsDTO = QBTDCOrderDTOFactory.defaultQuoteDetailsDTO();
        Mockito.when(pbtdcOrdersPersistenceService.findActiveOrdersByRefQuoteItemId(1L, TALOS_COMPLETE)).thenReturn(List.of(order, order));
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(order.getWagOrderId())).thenReturn(order);
        Assertions.assertThatThrownBy(() -> orderValidationService.checkDuplicateOrder(order.getWagOrderId()))
                .isInstanceOf(ValidationException.class);
        Assertions.assertThat(order.getRejectionDetails()).usingRecursiveComparison().ignoringFields("orders").isEqualTo(OrderFactory.rejectionDetailsForDuplicateOrder());
        Mockito.verify(pbtdcOrdersPersistenceService, Mockito.times(1)).update(any(Pbtdc.class));

    }


    @Test
    @DisplayName("Order is not duplicate")
    void checkDuplicateOrder_valid() throws Exception {
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        QuoteDetailsDTO quoteDetailsDTO = QBTDCOrderDTOFactory.defaultQuoteDetailsDTO();
        Mockito.when(pbtdcOrdersPersistenceService.findActiveOrdersByRefQuoteItemId(1L, TALOS_COMPLETE)).thenReturn(List.of(order));
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(order.getWagOrderId())).thenReturn(order);
        orderValidationService.checkDuplicateOrder(order.getWagOrderId());
        Assertions.assertThatNoException();
    }

    @Test
    @DisplayName("Quote profile is correctly saved")
    void checkProfileValue() throws Exception {
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        Pbtdc expected = OrderFactory.enrichedPbtdcOrdersWithQuoteItem();
        QuoteDetailsDTO quoteDetailsDTO = QBTDCOrderDTOFactory.defaultQuoteDetailsDTO();
        Mockito.when(orderManagerService.getQuoteItem(1L)).thenReturn(Optional.of(quoteDetailsDTO));
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(order.getWagOrderId())).thenReturn(order);
        Mockito.when(qbtdcOrderMapper.toQuote(quoteDetailsDTO.getQuote(), quoteDetailsDTO.getQbtdcs(), quoteDetailsDTO.getOrders())).thenReturn(expected.getQuote());
        orderValidationService.validateAndPersistQuote(order.getWagOrderId());
        Mockito.verify(pbtdcOrdersPersistenceService).update(updatedOrderCaptor.capture());
        Pbtdc updatedOrder = updatedOrderCaptor.getValue();
        Assertions.assertThat(updatedOrder.getLogicalLink().getProfile()).isEqualTo(expected.getLogicalLink().getProfile());
        Assertions.assertThat(updatedOrder.getLogicalLink().getServiceClass()).isNull();

    }

    @Test
    @DisplayName("Quote item is not found")
    void quoteItemNotFound() throws Exception {
        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        Mockito.when(orderManagerService.getQuoteItem(1L)).thenReturn(Optional.empty());
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(order.getWagOrderId())).thenReturn(order);
        Assertions.assertThatThrownBy(() -> orderValidationService.validateAndPersistQuote(order.getWagOrderId()))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Quote 1 is invalid");
    }

    @Test
    @DisplayName("BT Group Ref is not provided when connection_type is Diverse")
    void btGroupRefInvalid() throws Exception {

        Pbtdc order = OrderFactory.defaultPbtdcOrders();
        order.setConnectionType("ETHERWAY_DIVERSE");
        PBTDCOrderDTO pbtdcOrderDTO = PBTDCOrderDTOFactory.defaultPBTDCOrderDTO();
        pbtdcOrderDTO.getPbtdcs().setConnectionType("ETHERWAY_DIVERSE");
        Mockito.when(orderManagerService.findPBTDCOrderById(order.getWagOrderId())).thenReturn(Optional.of(pbtdcOrderDTO));
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(order.getWagOrderId())).thenReturn(order);
        Assertions.assertThatThrownBy(() -> orderValidationService.validateAndPersistQuote(order.getWagOrderId()))
                .isInstanceOf(ValidationException.class);
        Mockito.verify(pbtdcOrdersPersistenceService, Mockito.times(1)).update(any(Pbtdc.class));
        Assertions.assertThat(order.getRejectionDetails()).usingRecursiveComparison().ignoringFields("orders").isEqualTo(OrderFactory.rejectionDetailsForInvalidBtGroupRef());
    }
}
