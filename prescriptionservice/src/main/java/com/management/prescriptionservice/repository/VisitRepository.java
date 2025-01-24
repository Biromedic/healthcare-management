package com.management.prescriptionservice.repository;

import com.management.prescriptionservice.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
