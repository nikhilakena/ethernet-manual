package com.btireland.talos.ethernet.engine.config;

import com.btireland.talos.core.aop.aspect.ApiLogger;
import com.btireland.talos.ethernet.engine.aop.LoggingAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfiguration {

    @Bean
    @ConditionalOnProperty(name = "debug", matchIfMissing = false)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }

    @Bean
    public ApiLogger apiLogger() {
        return new ApiLogger();
    }
}
