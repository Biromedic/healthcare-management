package com.management.prescriptionservice.repository;

import com.management.prescriptionservice.model.Prescription;
import com.management.prescriptionservice.model.enums.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByStatusAndCreatedAtBetween(
            PrescriptionStatus status,
            LocalDateTime start,
            LocalDateTime end
    );
}