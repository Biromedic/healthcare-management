package com.management.prescriptionservice.service.impl;

import com.management.prescriptionservice.dto.TcValidationResponse;
import com.management.prescriptionservice.service.TCValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class TcValidationServiceImpl implements TCValidationService {

    private final WebClient.Builder webClientBuilder;

    @Value("${tc.validation.url}")
    private String tcValidationServiceUrl;

    @Override
    public boolean validateTcNumber(String tcNumber) {
        try {
            TcValidationResponse response = webClientBuilder.build()
                    .get()
                    .uri(tcValidationServiceUrl + "?tcNumber=" + tcNumber)
                    .retrieve()
                    .bodyToMono(TcValidationResponse.class)
                    .block();

            return response != null && "SUCCESS".equals(response.getStatus());
        } catch (Exception e) {
            log.error("TC validation failed: {}", e.getMessage());
            return false;
        }
    }
}
