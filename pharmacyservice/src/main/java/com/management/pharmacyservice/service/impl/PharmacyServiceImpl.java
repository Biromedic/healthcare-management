package com.management.pharmacyservice.service.impl;

import com.management.pharmacyservice.client.MedicineServiceClient;
import com.management.pharmacyservice.dto.*;
import com.management.pharmacyservice.model.Pharmacy;
import com.management.pharmacyservice.repository.PharmacyRepository;
import com.management.pharmacyservice.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final MedicineServiceClient medicineServiceClient;
    private final ModelMapper modelMapper;

    @Override
    public PharmacyResponseDTO createPharmacy(PharmacyRequestDTO requestDTO) {
        Pharmacy pharmacy = modelMapper.map(requestDTO, Pharmacy.class);
        Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);
        return modelMapper.map(savedPharmacy, PharmacyResponseDTO.class);
    }

    @Override
    public PharmacyResponseDTO getPharmacyById(Long id) {
        Pharmacy pharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacy not found with id: " + id));
        return modelMapper.map(pharmacy, PharmacyResponseDTO.class);
    }

    @Override
    public List<PharmacyResponseDTO> getAllPharmacies() {
        return pharmacyRepository.findAll()
                .stream()
                .map(pharmacy -> modelMapper.map(pharmacy, PharmacyResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deletePharmacy(Long id) {
        if (!pharmacyRepository.existsById(id)) {
            throw new RuntimeException("Pharmacy not found with id: " + id);
        }
        pharmacyRepository.deleteById(id);
    }

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

    @Override
    public PrescriptionStatusResponseDTO checkPrescriptionStatus(Long prescriptionId) {
        // Placeholder: API çağrısı yapılmalı
        return PrescriptionStatusResponseDTO.builder()
                .prescriptionId(prescriptionId)
                .status("COMPLETED")
                .pharmacyName("Example Pharmacy")
                .missingMedicines(List.of())
                .build();
    }

    @Override
    public List<PrescriptionStatusResponseDTO> getIncompletePrescriptions() {
        // Placeholder: Mock data döndürüyor
        return List.of(
                PrescriptionStatusResponseDTO.builder()
                        .prescriptionId(1L)
                        .status("INCOMPLETE")
                        .pharmacyName("Example Pharmacy")
                        .missingMedicines(List.of("Medicine A", "Medicine B"))
                        .build()
        );
    }

    @Override
    public List<MedicineDTO> searchMedicines(String query) {
        return medicineServiceClient.searchMedicines(query);
    }
}