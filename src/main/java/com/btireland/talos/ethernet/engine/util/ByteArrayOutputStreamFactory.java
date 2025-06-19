package com.btireland.talos.ethernet.engine.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class ByteArrayOutputStreamFactory {
    @Bean
    public ByteArrayOutputStream construct(){
        return new ByteArrayOutputStream();
    }
}
