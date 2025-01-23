package com.management.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtValidationGatewayFilterFactory.Config> {

    private final WebClient webClient;
    @Value("${jwt.validate-endpoint}")
    private String validateEndpoint;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            String path = exchange.getRequest().getURI().getPath();

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorizedResponse(exchange);
            }

            String token = authHeader.substring(7);

            if (isAdminEndpoint(path)) {
                return webClient.post()
                        .uri(validateEndpoint)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .then(chain.filter(exchange))
                        .onErrorResume(e -> unauthorizedResponse(exchange));
            } else {
                return chain.filter(exchange);
            }
        };
    }

    private boolean isAdminEndpoint(String path) {
        return path.startsWith("/api/medicines/v1/upload")
                || path.startsWith("/api/medicines/v1/cache");
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config { }
}