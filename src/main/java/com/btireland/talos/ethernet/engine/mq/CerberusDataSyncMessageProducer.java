package com.btireland.talos.ethernet.engine.mq;


import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class CerberusDataSyncMessageProducer {

    private ObjectMapper objectMapper;

    private CommonProducer commonProducer;

    @Value("${application.queue.cerberus.data-sync}")
    private String cerberusDataSyncQueue;

    public CerberusDataSyncMessageProducer(ObjectMapper objectMapper, CommonProducer commonProducer){
        this.objectMapper = objectMapper;
        this.commonProducer=commonProducer;
    }

    public String getOrderQueue() { return this.cerberusDataSyncQueue;}

    public void sendOrderData(OrdersDTO orderData){

        log.debug("Sending message to Queue : {"+getOrderQueue()+"}");

        try{
            log.debug("Cerberus Data-sync Request Message to be sent : "+objectMapper.writeValueAsString(orderData));
        } catch (JsonProcessingException e) {
            log.error("Error converting Order object to string ",e);
        }
        commonProducer.sendMessage(orderData,getOrderQueue());

    }



}

