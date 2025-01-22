package com.management.prescriptionservice.repository;

import com.management.prescriptionservice.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @NonNull
    Page<Prescription> findAll(@NonNull Pageable pageable);

    @Query("SELECT p FROM Prescription p WHERE p.isComplete = false AND p.missingDetails IS NOT NULL")
    List<Prescription> findIncompletePrescriptions();

}

