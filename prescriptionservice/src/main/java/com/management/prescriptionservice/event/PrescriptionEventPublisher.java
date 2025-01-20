package com.management.prescriptionservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.prescription.exchange}")
    private String prescriptionExchange;

    @Value("${rabbitmq.prescription.routing-key}")
    private String prescriptionRoutingKey;

    public void publishPrescriptionEvent(PrescriptionEvent event) {
        try {
            log.info("Publishing prescription event: {}", event);
            rabbitTemplate.convertAndSend(prescriptionExchange, prescriptionRoutingKey, event);
            log.info("Prescription event published successfully");
        } catch (Exception e) {
            log.error("Error publishing prescription event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to publish prescription event", e);
        }
    }
} 