package com.asechallenge.inventorymanagement.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("products-api")
                .packagesToScan("com.asechallenge.inventorymanagement.controller") // only your controllers
                .build();
    }
}

