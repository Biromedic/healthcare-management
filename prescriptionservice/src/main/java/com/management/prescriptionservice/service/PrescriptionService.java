package com.management.prescriptionservice.service;

import com.management.prescriptionservice.dto.PrescriptionRequestDTO;
import com.management.prescriptionservice.dto.PrescriptionResponseDTO;

import java.util.List;

public interface PrescriptionService {
    PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO requestDTO);

    PrescriptionResponseDTO getPrescriptionById(Long id);

    List<PrescriptionResponseDTO> getAllPrescriptions();

    void deletePrescription(Long id);
}
