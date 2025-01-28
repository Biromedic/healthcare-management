package com.management.notificationservice;

import com.management.notificationservice.dto.IncompletePrescriptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PrescriptionListener {

    @RabbitListener(queues = "prescription.queue")
    @Scheduled(cron = "0 0 1 * * ?")
    public void handleIncompletePrescription(IncompletePrescriptionMessage prescription) {
        log.info("⚠️ Incomplete Prescription Alert ⚠️");
        log.info("Prescription ID: {}", prescription.getPrescriptionId());
        log.info("Patient TC: {}", prescription.getPatientTC());
        log.info("Missing Medicines Count: {}", prescription.getMissingMedicinesCount());
        log.info("Pharmacy User ID to Notify: {}", prescription.getPharmacyUserId());
        log.info("-----------------------------------------");
    }
}