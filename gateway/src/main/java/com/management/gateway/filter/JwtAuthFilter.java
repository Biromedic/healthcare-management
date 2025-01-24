package com.management.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange.getRequest().getHeaders());
        if (token == null) {
            return unauthorizedResponse(exchange, "Missing authorization token");
        }

        return validateToken(exchange, token)
                .flatMap(valid -> {
                    if (!valid) {
                        return unauthorizedResponse(exchange, "Invalid token");
                    }

                    // Get user info from exchange attributes
                    List<String> roles = exchange.getAttribute("roles");
                    String userId = Objects.requireNonNull(exchange.getAttribute("userId")).toString();

                    // Role-based access control
                    if (path.startsWith("/api/prescriptions/v1")) {
                        assert roles != null;
                        if (!roles.contains("ROLE_DOCTOR")) {
                            return unauthorizedResponse(exchange, "Doctor role required");
                        }
                    }

                    // Add headers for downstream services
                    assert roles != null;
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header("X-User-Id", userId)
                            .header("X-User-Roles", String.join(",", roles))
                            .build();
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                })
                .onErrorResume(e -> unauthorizedResponse(exchange, "Token validation failed"));
    }

    private Mono<Boolean> validateToken(ServerWebExchange exchange, String token) {
        return webClient.post()
                .uri(validateEndpoint)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(response -> {
                    exchange.getAttributes().put("userId", response.get("userId"));

                    List<String> roles = ((List<?>) response.get("roles"))
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.toList());
                    exchange.getAttributes().put("roles", roles);

                    return response.get("isValid") instanceof Boolean &&
                            (Boolean) response.get("isValid");
                });
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

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        String body = String.format("{\"error\": \"Unauthorized\", \"message\": \"%s\"}", message);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(body.getBytes()))
        );
    }
}