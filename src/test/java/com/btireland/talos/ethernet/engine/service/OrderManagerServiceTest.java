package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.rest.exception.BadRequestException;
import com.btireland.talos.core.common.rest.exception.NotFoundException;
import com.btireland.talos.core.common.rest.exceptionmessage.ExceptionResponse;
import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.*;
import com.btireland.talos.ethernet.engine.dto.PBTDCGlanIdDTO;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceNotFoundException;
import com.btireland.talos.ethernet.engine.util.AgentsDTOFactory;
import com.btireland.talos.ethernet.engine.util.PBTDCGlanIdDTOFactory;
import com.btireland.talos.ethernet.engine.util.PBTDCOrderDTOFactory;
import com.btireland.talos.ethernet.engine.util.QBTDCOrderDTOFactory;
import feign.Request;
import feign.RequestTemplate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.times;

@UnitTest
@ExtendWith(MockitoExtension.class)
class OrderManagerServiceTest {

    @InjectMocks
    OrderManagerService orderManagerService;
    @Mock
    private OrderManagerClient orderManagerClient;

    @Test
    @DisplayName("Agent details are received when calling getAgentByWagOrderId method")
    void getAgentByWagOrderId() {
        AgentDTO expected = AgentsDTOFactory.defaultAgentDTO().get();
        Mockito.when(orderManagerClient.getAgentByWagOrderId(1L)).thenReturn(expected);
        AgentDTO actual = orderManagerService.getAgentByWagOrderId(1L).get();
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Empty AgentDTO returned when calling getAgentByWagOrderId method")
    void getAgentByWagOrderIdThrowsNotFoundException() throws Exception {

        Map<String, Collection<String>> headers = new HashMap<>();
        Mockito.when(orderManagerClient.getAgentByWagOrderId(1L))
                .thenThrow(new NotFoundException(
                        feign.Request.create(Request.HttpMethod.POST, "/api/v1/wag/agent/{orderId}", headers, null, null, null),
                        null,
                        ExceptionResponse.builder().build()));
        Optional<AgentDTO> actual = orderManagerService.getAgentByWagOrderId(1L);
        Assertions.assertThat(actual).isEqualTo(Optional.empty());

    }

    @Test
    @DisplayName("getQuoteItem returns expected value")
    void getQuoteItemTest() {
        QuoteDetailsDTO expected = QuoteDetailsDTO.builder().build();
        Mockito.when(orderManagerClient.fetchQuote(1L)).thenReturn(expected);
        QuoteDetailsDTO actual = orderManagerService.getQuoteItem(1L).get();
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("getQuoteItem for an unknown quote item returns an empty value")
    void getQuoteItemFailTest() {
        Mockito.when(orderManagerClient.fetchQuote(1L))
                .thenThrow(new NotFoundException(
                        Request.create(Request.HttpMethod.GET, "url", Map.of(), null, new RequestTemplate()),
                        null,
                        ExceptionResponse.builder().build()
                ));
        Optional<QuoteDetailsDTO> actual = orderManagerService.getQuoteItem(1L);
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("Saved PBTDC Order is received when calling createPBTDCOrder method")
    void createPBTDCOrderHappyPath() throws OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException, BadRequestException, IOException {
        PBTDCOrderDTO pbtdcOrderRequest = PBTDCOrderDTOFactory.defaultPBTDCOrderDTO();
        PBTDCOrderDTO expected = PBTDCOrderDTOFactory.savedPBTDCResponse();
        Mockito.when(orderManagerClient.createPBTDCOrder(pbtdcOrderRequest)).thenReturn(expected);
        PBTDCOrderDTO actual = orderManagerService.createPBTDCOrder(pbtdcOrderRequest);
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("BadRequestException is received when calling createPBTDCOrder method")
    void createPBTDCOrderBadRequestException() throws Exception {
        PBTDCOrderDTO pbtdcOrderRequest = PBTDCOrderDTOFactory.defaultPBTDCOrderDTO();
        Map<String, Collection<String>> headers = new HashMap<>();
        Mockito.when(orderManagerClient.createPBTDCOrder(pbtdcOrderRequest)).thenThrow(new BadRequestException(feign.Request.create(Request.HttpMethod.POST,
                        "/api/v1/wag/pbtdc-orders", headers, null, null, null), null,
                        ExceptionResponse.builder().build()));
        Assertions.assertThatThrownBy(() -> orderManagerService.createPBTDCOrder(pbtdcOrderRequest))
                .isInstanceOf(OrderManagerServiceBadRequestException.class);
        Mockito.verify(orderManagerClient, times(1)).createPBTDCOrder(Mockito.any(PBTDCOrderDTO.class));
    }

    @Test
    @DisplayName("ResourceNotFoundException is received when calling createPBTDCOrder method")
    void createPBTDCOrderResourceNotFoundException() throws BadRequestException {
        PBTDCOrderDTO pbtdcOrderRequest = PBTDCOrderDTOFactory.defaultPBTDCOrderDTO();
        Map<String, Collection<String>> headers = new HashMap<>();
        Mockito.when(orderManagerClient.createPBTDCOrder(pbtdcOrderRequest)).thenThrow(
                new com.btireland.talos.core.common.rest.exception.NotFoundException(feign.Request.create(Request.HttpMethod.POST,
                        "/api/v1/wag/pbtdc-orders", headers, null, null, null), null,
                        ExceptionResponse.builder().build()));
        Assertions.assertThatThrownBy(() -> orderManagerService.createPBTDCOrder(pbtdcOrderRequest))
                .isInstanceOf(OrderManagerServiceNotFoundException.class);
        Mockito.verify(orderManagerClient, times(1)).createPBTDCOrder(Mockito.any(PBTDCOrderDTO.class));
    }

    @Test
    @DisplayName("FeignException.BadRequest is received when calling createPBTDCOrder method")
    void createPBTDCOrderFeignExceptionBadRequest() throws BadRequestException {
        PBTDCOrderDTO pbtdcOrderRequest = PBTDCOrderDTOFactory.defaultPBTDCOrderDTO();
        Mockito.when(orderManagerClient.createPBTDCOrder(pbtdcOrderRequest)).thenThrow(
                new BadRequestException(feign.Request.create(Request.HttpMethod.POST,
                "/api/v1/wag/pbtdc-orders", new HashMap<>(), null, null, null), null,
                ExceptionResponse.builder().build()));
        Assertions.assertThatThrownBy(() -> orderManagerService.createPBTDCOrder(pbtdcOrderRequest))
                .isInstanceOf(OrderManagerServiceBadRequestException.class);
        Mockito.verify(orderManagerClient, times(1)).createPBTDCOrder(Mockito.any(PBTDCOrderDTO.class));
    }

    @Test
    @DisplayName("Order Manager update quote is invoked")
    void updateQuote() throws BadRequestException, OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException {
        QuoteDTO actual = orderManagerService.updateQuote(QBTDCOrderDTOFactory.defaultQuoteDTO());
        Mockito.verify(orderManagerClient).updateQuote(1l, QBTDCOrderDTOFactory.defaultQuoteDTO());
    }

    @Test
    @DisplayName("Order Manager update gladId is invoked")
    void updatePBTDCOrderGlanId() throws OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException, BadRequestException {
        orderManagerService.updatePBTDCOrderGlanId(123L, PBTDCGlanIdDTOFactory.defaultPBTDCGlanIdDTO());
        Mockito.verify(orderManagerClient).updatePBTDCOrderGlanId(123l, PBTDCGlanIdDTOFactory.defaultPBTDCGlanIdDTO());
    }

    @Test
    @DisplayName("FeignException.BadRequest is received when calling updatePBTDCOrderGlanId method")
    void updatePBTDCOrderGlanIdFeignExceptionBadRequest() throws BadRequestException {
        List<PBTDCGlanIdDTO> pbtdcGlanIdDTOList = PBTDCGlanIdDTOFactory.defaultPBTDCGlanIdDTO();
        Mockito.when(orderManagerClient.updatePBTDCOrderGlanId(123l, pbtdcGlanIdDTOList)).thenThrow(
                new BadRequestException(feign.Request.create(Request.HttpMethod.POST,
                "/api/v1/wag/pbtdc-orders/{id}/glanid", new HashMap<>(), null, null, null), null,
                ExceptionResponse.builder().build()));
        Assertions.assertThatThrownBy(() -> orderManagerService.updatePBTDCOrderGlanId(123L, pbtdcGlanIdDTOList))
                .isInstanceOf(OrderManagerServiceBadRequestException.class);
        Mockito.verify(orderManagerClient, times(1)).updatePBTDCOrderGlanId(Mockito.anyLong(), Mockito.anyList());
    }

}
