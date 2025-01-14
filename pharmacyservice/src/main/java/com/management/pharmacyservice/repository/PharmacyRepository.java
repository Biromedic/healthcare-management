package com.management.pharmacyservice.repository;

import com.management.pharmacyservice.model.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
