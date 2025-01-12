package com.management.prescriptionservice.repository;

import com.management.prescriptionservice.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
