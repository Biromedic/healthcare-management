package com.management.prescriptionservice.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PrescriptionServiceWebClient {

    private final WebClient webClient;

    public PrescriptionServiceWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public List<String> searchMedicines(String query) {
        return webClient.get()
                .uri("/api/medicines/v1/search?query={query}", query)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }
}
