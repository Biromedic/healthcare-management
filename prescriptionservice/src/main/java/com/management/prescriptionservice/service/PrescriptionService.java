package com.management.prescriptionservice.service;

import com.management.prescriptionservice.dto.CreatePrescriptionRequestDTO;
import com.management.prescriptionservice.dto.PrescriptionResponseDTO;

import java.util.List;

public interface PrescriptionService {

    PrescriptionResponseDTO createPrescription(CreatePrescriptionRequestDTO request);
    List<PrescriptionResponseDTO> getAllPrescriptions();
}
