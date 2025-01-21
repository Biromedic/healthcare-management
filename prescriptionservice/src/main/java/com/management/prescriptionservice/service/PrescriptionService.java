package com.management.prescriptionservice.service;

import com.management.prescriptionservice.dto.CreatePrescriptionRequestDTO;
import com.management.prescriptionservice.dto.PrescriptionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrescriptionService {

    PrescriptionResponseDTO createPrescription(CreatePrescriptionRequestDTO request);
    Page<PrescriptionResponseDTO> getAllPrescriptions(Pageable pageable);
}

