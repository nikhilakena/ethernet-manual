package com.btireland.talos.ethernet.engine.config;

import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompressorFactoryConfiguration {
    @Bean
    CompressorStreamFactory getCompressorConfiguration(){
        return new CompressorStreamFactory();
    }
}
