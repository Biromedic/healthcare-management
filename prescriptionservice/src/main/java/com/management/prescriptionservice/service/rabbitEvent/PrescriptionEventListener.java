package com.management.prescriptionservice.service.rabbitEvent;

import com.management.prescriptionservice.event.PrescriptionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PrescriptionEventListener {
    @RabbitListener(queues = "${rabbitmq.prescription.queue}")
    public void handlePrescriptionEvent(PrescriptionEvent event) {
        log.info("Received prescription event: {}", event);
        System.out.println("Notification Sent to Patient: " + event.getMessage());
    }
}
