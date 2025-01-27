package com.management.medicineservice.service;

import com.management.medicineservice.DTO.MedicineDTO;

import java.util.List;

public interface MedicineService {
    void updateMedicineFromExcel();

    void cacheAllMedicines();

    List<MedicineDTO> searchMedicines(String query);

    void updateMedicineList();

    MedicineDTO getMedicineById(String id);
}
