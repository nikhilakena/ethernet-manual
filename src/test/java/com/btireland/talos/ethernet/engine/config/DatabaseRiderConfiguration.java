package com.btireland.talos.ethernet.engine.config;

import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@TestConfiguration
@Profile("test")
public class DatabaseRiderConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        if (StringUtils.hasText(properties.getName())) {
            dataSource.setPoolName(properties.getName());
        }
        return dataSource;
    }

    /**
     * We have to create a datasource for database rider that is different from the Spring one.
     * Because the Spring database datasource has autocommit set to false (so that transactions work) and database rider needs autocommit=true.
     * With autocommit to false, the data inserted by @DataSet annotation is not visible to the spring code as it won’t be flushed to the database.
     * @param dataSourceProperties standard datasource properties from the configured Hikari pool
     * @return a new datasource with auto-commit set to true
     */
    @Bean
    public DataSource databaseRiderDatasource(DataSourceProperties dataSourceProperties) {
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        dataSource.setAutoCommit(true);
        dataSource.setMaximumPoolSize(200);
        return dataSource;
    }

}
