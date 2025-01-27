package com.management.medicineservice.service;

import com.management.medicineservice.DTO.MedicineDTO;
import org.springframework.data.domain.Page;


public interface MedicineService {
    void updateMedicineFromExcel();

    void cacheAllMedicines();

    Page<MedicineDTO> searchMedicines(String query, int page, int size);

    void updateMedicineList();

    MedicineDTO getMedicineById(String id);
}
