package com.btireland.talos.quote.facade.config;

import com.btireland.talos.core.common.rest.feign.CustomErrorDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign configuration applicable for Quote Manager Feign client.
 */
@Configuration
public class QuoteManagerFeignConfiguration {

    /**
     * Creates a custom error decoder that handles client error response.
     *
     * @param objectMapper the object mapper
     * @return the custom error decoder
     */
    @Bean
    public CustomErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new CustomErrorDecoder(objectMapper);
    }

}
