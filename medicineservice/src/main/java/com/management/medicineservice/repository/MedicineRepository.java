package com.management.medicineservice.repository;

import com.management.medicineservice.model.Medicine;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicineRepository extends MongoRepository<Medicine, Long> {
}

