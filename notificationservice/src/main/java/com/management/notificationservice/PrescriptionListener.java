package com.management.notificationservice;

import com.management.prescriptionservice.model.Prescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PrescriptionListener {

    @RabbitListener(queues = "prescription.queue")
    public void handleIncompletePrescription(Prescription prescription) {
        log.info("⚠️ Incomplete Prescription Alert ⚠️");
        log.info("Prescription ID: {}", prescription.getId());
        log.info("Patient TC: {}", prescription.getPatientTC());
        log.info("Missing Medicines Count: {}", prescription.getMedicines().size());
        log.info("Pharmacy User ID to Notify: {}", prescription.getPharmacyUserId());
        log.info("-----------------------------------------");
    }
}