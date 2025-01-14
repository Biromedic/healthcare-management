package com.management.pharmacyservice.service;

import com.management.pharmacyservice.dto.*;

import java.util.List;

public interface PharmacyService {
    PharmacyResponseDTO createPharmacy(PharmacyRequestDTO requestDTO);

    PharmacyResponseDTO getPharmacyById(Long id);

    List<PharmacyResponseDTO> getAllPharmacies();

    void deletePharmacy(Long id);

    void processPrescription(PrescriptionProcessRequestDTO requestDTO);

    PrescriptionStatusResponseDTO checkPrescriptionStatus(Long prescriptionId);

    List<PrescriptionStatusResponseDTO> getIncompletePrescriptions();

    List<MedicineDTO> searchMedicines(String query);
}
