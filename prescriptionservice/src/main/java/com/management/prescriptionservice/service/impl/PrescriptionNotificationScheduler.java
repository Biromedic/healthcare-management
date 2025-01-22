package com.management.prescriptionservice.service.impl;

import com.management.prescriptionservice.event.PrescriptionEvent;
import com.management.prescriptionservice.event.PrescriptionEventPublisher;
import com.management.prescriptionservice.model.Prescription;
import com.management.prescriptionservice.repository.PrescriptionRepository;
import com.management.prescriptionservice.service.PrescriptionNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionNotificationScheduler implements PrescriptionNotificationService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionEventPublisher eventPublisher;


    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void notifyIncompletePrescriptions() {
        List<Prescription> incompletePrescriptions = prescriptionRepository.findIncompletePrescriptions();

        if (incompletePrescriptions.isEmpty()) {
            log.info("No incomplete prescriptions found for notification.");
            return;
        }

        incompletePrescriptions.forEach(prescription -> {
            PrescriptionEvent event = PrescriptionEvent.builder()
                    .prescriptionId(prescription.getId().toString())
                    .missingDetails(prescription.getMissingDetails())
                    .build();
            eventPublisher.publishPrescriptionEvent(event);
            log.info("Event published for Prescription ID: {}", prescription.getId());
        });
    }
}
