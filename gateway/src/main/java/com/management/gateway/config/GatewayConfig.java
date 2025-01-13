package com.management.gateway.config;

import com.management.gateway.filter.JwtCookieFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class GatewayConfig {

    private final JwtCookieFilter jwtCookieFilter;

    @Value("${AUTH_SERVICE_URL}")
    private String authServiceUrl;

    @Value("${PHARMACY_SERVICE_URL}")
    private String pharmacyServiceUrl;

    public GatewayConfig(JwtCookieFilter jwtCookieFilter) {
        this.jwtCookieFilter = jwtCookieFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_service", r -> r.path("/auth/**")
                        .uri(authServiceUrl))
                .route("pharmacy_service", r -> r.path("/api/pharmacies/v1/**")
                        .filters(f -> f.filter(jwtCookieFilter.apply(new JwtCookieFilter.Config())))
                        .uri(pharmacyServiceUrl))
                .build();
    }
}
