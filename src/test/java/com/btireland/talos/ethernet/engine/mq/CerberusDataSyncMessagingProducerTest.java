package com.btireland.talos.ethernet.engine.mq;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.util.PBTDCOrderDTOFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;

@UnitTest
@ExtendWith(MockitoExtension.class)
class CerberusDataSyncMessagingProducerTest {

    @Mock
    private CommonProducer commonProducer;
    @Mock
    private ObjectMapper objectMapper;


    @InjectMocks
    private CerberusDataSyncMessageProducer dataSyncMessageProducer;

    @BeforeEach
    void setUp(){
        dataSyncMessageProducer= new CerberusDataSyncMessageProducer(objectMapper,commonProducer);
    }

    @Test
    void sendMessage() {
        OrdersDTO ordersDTO= PBTDCOrderDTOFactory.defaultOrdersDTO();
        dataSyncMessageProducer.sendOrderData(ordersDTO);
        Mockito.verify(commonProducer, times(1)).sendMessage(ordersDTO,dataSyncMessageProducer.getOrderQueue());

    }
}