package com.management.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class JwtAuthFilter implements GlobalFilter {

    @Value("${jwt.auth-header}")
    private String authHeader;

    @Value("${jwt.bearer-prefix}")
    private String bearerPrefix;

    @Value("${jwt.validate-endpoint}")
    private String validateEndpoint;

    private final WebClient webClient;

    public JwtAuthFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        System.out.println("Gateway processing path: " + path); // Debug log
        if (isPublicEndpoint(exchange.getRequest().getPath().toString())) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange.getRequest().getHeaders());
        if (token == null) {
            return unauthorizedResponse(exchange, "Missing authorization token");
        }

        return validateToken(token)
                .flatMap(valid -> {
                    if (valid) {
                        return chain.filter(exchange);
                    }
                    return unauthorizedResponse(exchange, "Invalid token");
                })
                .onErrorResume(e -> unauthorizedResponse(exchange, "Token validation failed"));
    }

    private boolean isPublicEndpoint(String path) {

        System.out.println("Checking path: " + path); // Add this line for debugging
        return path.startsWith("/api/auth/v1/signin") ||
                path.startsWith("/api/auth/v1/signup") ||
                path.startsWith("/api/auth/v1/validate") ||
                path.startsWith("/api/medicines/v1/search") ||
                path.startsWith("/actuator/health");
    }

    private String extractToken(HttpHeaders headers) {
        String authHeader = headers.getFirst(this.authHeader);
        if (authHeader != null && authHeader.startsWith(this.bearerPrefix)) {
            return authHeader.substring(this.bearerPrefix.length());
        }
        return null;
    }

    private Mono<Boolean> validateToken(String token) {
        return webClient.post()
                .uri(validateEndpoint)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(response -> {
                    Object isValid = response.get("isValid");
                    return isValid instanceof Boolean && (Boolean) isValid;
                });
    }


    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        String body = String.format("{\"error\": \"Unauthorized\", \"message\": \"%s\"}", message);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(body.getBytes()))
        );
    }
}