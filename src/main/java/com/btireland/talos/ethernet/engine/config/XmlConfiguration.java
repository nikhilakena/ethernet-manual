package com.btireland.talos.ethernet.engine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class XmlConfiguration {

    @Bean
    public Marshaller jaxb2marshaller() {
        var marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.btireland.talos.ethernet.engine.soap");
        return marshaller;
    }
}
