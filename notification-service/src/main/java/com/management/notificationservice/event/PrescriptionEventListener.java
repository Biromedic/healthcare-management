package com.management.notificationservice.event;

import com.management.notificationservice.dto.PrescriptionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionEventListener {

    @RabbitListener(queues = "${rabbitmq.prescription.queue}")
    public void handlePrescriptionEvent(PrescriptionEvent event) {
        log.info("Received Prescription Event: {}", event);
    }
}
