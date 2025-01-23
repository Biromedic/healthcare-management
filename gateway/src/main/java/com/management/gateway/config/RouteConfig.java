package com.management.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Value("${auth-service-url}")
    private String authServiceUrl;

    @Value("${medicine-service-url}")
    private String medicineServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Authentication Service Routes
                .route("auth_service_route", r -> r
                        .path("api/auth/v1/**")
                        .uri(authServiceUrl))
                .route("medicine_service_route", r -> r
                        .path("api/medicines/v1/**")
                        .uri(medicineServiceUrl))
                .build();
    }
} 