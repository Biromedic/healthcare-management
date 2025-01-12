package com.management.prescriptionservice.service.impl;

import com.management.prescriptionservice.dto.PrescriptionRequestDTO;
import com.management.prescriptionservice.dto.PrescriptionResponseDTO;
import com.management.prescriptionservice.exception.PrescriptionNotFoundException;
import com.management.prescriptionservice.model.Prescription;
import com.management.prescriptionservice.repository.PrescriptionRepository;
import com.management.prescriptionservice.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    @Override
    public PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO requestDTO) {
        Prescription prescription = Prescription.builder()
                .patientName(requestDTO.getPatientName())
                .doctorName(requestDTO.getDoctorName())
                .medicines(requestDTO.getMedicines())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return convertToDTO(savedPrescription);
    }

    @Override
    public PrescriptionResponseDTO getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with id: " + id));
        return convertToDTO(prescription);
    }

    @Override
    public List<PrescriptionResponseDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePrescription(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new PrescriptionNotFoundException("Prescription not found with id: " + id);
        }
        prescriptionRepository.deleteById(id);
    }

    private PrescriptionResponseDTO convertToDTO(Prescription prescription) {
        return PrescriptionResponseDTO.builder()
                .id(prescription.getId())
                .patientName(prescription.getPatientName())
                .doctorName(prescription.getDoctorName())
                .medicines(prescription.getMedicines())
                .status(prescription.getStatus())
                .createdAt(prescription.getCreatedAt())
                .updatedAt(prescription.getUpdatedAt())
                .build();
    }
}
