package com.btireland.talos.ethernet.engine.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.jms.IllegalStateException;

import java.util.UUID;

import static org.apache.activemq.artemis.api.core.Message.HDR_DUPLICATE_DETECTION_ID;

@Component
@Slf4j
public class CommonProducer {

    private JmsTemplate jmsTemplate;
    public CommonProducer(JmsTemplate jmsTemplate){
        this.jmsTemplate=jmsTemplate;
    }


    @Retryable(value = { UncategorizedJmsException.class, IllegalStateException.class },
            maxAttemptsExpression= "${application.artemis-ha.max-transmit-retries}",
            backoff = @Backoff(delayExpression= "${application.artemis-ha.retransmit-timeout}"))
    public void sendMessage(Object message,String queueName)
    {
        log.debug("Attempt to transmit message to queue " + queueName);
        this.jmsTemplate.convertAndSend(queueName, message, m -> {
            m.setStringProperty(String.valueOf(HDR_DUPLICATE_DETECTION_ID), UUID.randomUUID().toString());
            return m;
        });
        log.debug("Transmit successful for queue " + queueName);
    }
}
