package com.chubbTest.customer.infrastructure.config.database;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.chubbTest.customer.infrastructure.database.repository.jpa"
)
@EntityScan(
    basePackages = "com.chubbTest.customer.infrastructure.database.model"
)
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {
}