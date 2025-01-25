package com.management.prescriptionservice.service;

import com.management.prescriptionservice.dto.PrescriptionDTO;
import com.management.prescriptionservice.dto.PrescriptionRequest;

import java.time.LocalDate;
import java.util.List;

public interface PrescriptionService {
    PrescriptionDTO createPrescription(PrescriptionRequest request, String doctorUserId);
    PrescriptionDTO submitPrescription(Long prescriptionId, String pharmacyUserId);
    List<PrescriptionDTO> getIncompletePrescriptions(LocalDate date);
}