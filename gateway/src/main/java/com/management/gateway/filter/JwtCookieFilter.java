package com.management.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class JwtCookieFilter extends AbstractGatewayFilterFactory<JwtCookieFilter.Config> {

    private final WebClient webClient;

    @Value("${AUTH_SERVICE_URL}")
    private String authServiceUrl;

    public JwtCookieFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpCookie jwtCookie = exchange.getRequest().getCookies().getFirst("jwtCookie");
            if (jwtCookie == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClient.post()
                    .uri("/api/auth/v1/validate")
                    .header("Content-Type", "application/json")
                    .bodyValue(Map.of("token", jwtCookie.getValue()))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            Mono.error(new RuntimeException("JWT validation failed")))
                    .toBodilessEntity()
                    .flatMap(response -> chain.filter(exchange));
        };
    }

    public static class Config {
    }
}

