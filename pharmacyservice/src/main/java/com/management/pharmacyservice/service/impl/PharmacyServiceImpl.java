package com.management.pharmacyservice.service.impl;

import com.management.pharmacyservice.client.MedicineServiceClient;
import com.management.pharmacyservice.dto.*;
import com.management.pharmacyservice.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final MedicineServiceClient medicineServiceClient;

    @Override
    public void processPrescription(PrescriptionProcessRequestDTO requestDTO) {
        List<MedicineDTO> availableMedicines = medicineServiceClient.searchMedicines(requestDTO.getPharmacyName());
        List<String> missingMedicines = requestDTO.getMedicinesProvided().stream()
                .filter(medicine -> availableMedicines.stream().noneMatch(m -> m.getName().equalsIgnoreCase(medicine)))
                .toList();

        if (missingMedicines.isEmpty()) {
            System.out.println("Prescription processed successfully.");
        } else {
            System.out.println("Missing medicines: " + missingMedicines);
        }
    }
}