package com.management.gateway.config;

import com.management.gateway.filter.JwtCookieFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtCookieFilter jwtCookieFilter;

    @Value("${AUTH_SERVICE_URL}")
    private String authServiceUrl;

    @Value("${PHARMACY_SERVICE_URL}")
    private String pharmacyServiceUrl;

    @Value("${MEDICINE_SERVICE_URL}")
    private String medicineServiceUrl;

    @Value("${PRESCRIPTION_SERVICE_URL}")
    private String prescriptionServiceUrl;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_service", r -> r.path("api/auth/**")
                        .uri(authServiceUrl))
                .route("pharmacy_service", r -> r.path("/api/pharmacies/**")
                        .filters(f -> f.filter(jwtCookieFilter.apply(new JwtCookieFilter.Config())))
                        .uri(pharmacyServiceUrl))
                .route("medicine_service", r -> r.path("/api/medicines/**")
                        .uri(medicineServiceUrl))
                .route("prescription_service", r -> r.path("/api/prescriptions/**")
                        .filters(f -> f.filter(jwtCookieFilter.apply(new JwtCookieFilter.Config())))
                        .uri(prescriptionServiceUrl))
                .build();
    }
}
