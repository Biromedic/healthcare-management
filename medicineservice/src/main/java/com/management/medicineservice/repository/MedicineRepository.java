package com.management.medicineservice.repository;

import com.management.medicineservice.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByNameContainingIgnoreCase(String name);
}

