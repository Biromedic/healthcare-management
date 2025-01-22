package com.management.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingGatewayFilterFactory extends AbstractGatewayFilterFactory<LoggingGatewayFilterFactory.Config> {

    public LoggingGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("Request received for path: {}", exchange.getRequest().getPath());

            return chain.filter(exchange).doOnError(throwable -> {
                log.error("Error occurred while processing request: {}", throwable.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            }).then(Mono.fromRunnable(() -> log.info("Response status code: {}", exchange.getResponse().getStatusCode())));
        };
    }

    public static class Config {
    }
}
