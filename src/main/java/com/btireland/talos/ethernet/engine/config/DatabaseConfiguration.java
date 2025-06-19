package com.btireland.talos.ethernet.engine.config;

import com.btireland.talos.ethernet.engine.repository.JpaRsqlRepositoryImpl;
import io.github.perplexhub.rsql.RSQLConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories(basePackages = {"com.btireland.talos.ethernet.engine.repository", "com.btireland.talos" +
        ".quote.facade.domain.dao" }, repositoryBaseClass =
        JpaRsqlRepositoryImpl.class)
@EntityScan({"com.btireland.talos.ethernet.engine.domain", "com.btireland.talos" +
        ".quote.facade.domain.entity" })
@EnableTransactionManagement
@Import(RSQLConfig.class)
public class DatabaseConfiguration {

}
