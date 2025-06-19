package com.btireland.talos.ethernet.engine.config;

import com.btireland.talos.ethernet.engine.dto.NotificationDTO;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;


@Configuration
public class MessagingConfiguration {

    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        var converter = new MappingJackson2MessageConverter();

        HashMap<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("Notifications", NotificationDTO.class);
        typeIdMappings.put("OrdersDTO", OrdersDTO.class);
        converter.setTypeIdMappings(typeIdMappings);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    @Profile("local")
    @ConditionalOnProperty(name = "spring.artemis.mode", havingValue = "EMBEDDED", matchIfMissing = false)
    public ArtemisConfigurationCustomizer configureArtemisEmbeddedServer(ArtemisProperties artemisProperties) {
        return configuration -> {
            try {
                configuration.addAcceptorConfiguration("netty", artemisProperties.getBrokerUrl());
            } catch (Exception e) {
                throw new RuntimeException("Failed to add netty transport acceptor to artemis embedded server", e);
            }
        };
    }
}
