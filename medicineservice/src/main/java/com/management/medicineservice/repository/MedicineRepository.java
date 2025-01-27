package com.management.medicineservice.repository;

import com.management.medicineservice.model.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends MongoRepository<Medicine, String> {
    Page<Medicine> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

