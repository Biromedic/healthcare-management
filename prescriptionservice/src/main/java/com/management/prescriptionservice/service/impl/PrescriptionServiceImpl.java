package com.management.prescriptionservice.service.impl;

import com.management.prescriptionservice.dto.CreatePrescriptionRequestDTO;
import com.management.prescriptionservice.dto.PrescriptionResponseDTO;
import com.management.prescriptionservice.event.PrescriptionEvent;
import com.management.prescriptionservice.event.PrescriptionEventPublisher;
import com.management.prescriptionservice.model.Prescription;
import com.management.prescriptionservice.repository.PrescriptionRepository;
import com.management.prescriptionservice.service.PrescriptionService;
import com.management.prescriptionservice.service.TCValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ModelMapper modelMapper;
    private final PrescriptionEventPublisher eventPublisher;
    private final TCValidationService tcValidationService;

    @Override
    @Transactional
    public PrescriptionResponseDTO createPrescription(CreatePrescriptionRequestDTO request) {

        boolean isTcValid = tcValidationService.validateTcNumber(request.getPatientId().toString());
        if (!isTcValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid TC number provided.");
        }

        Prescription prescription = Prescription.builder()
                .doctorId(request.getDoctorId())
                .patientId(request.getPatientId())
                .medicationDetails(request.getMedicationDetails())
                .issuedAt(LocalDateTime.now())
                .build();
        
        prescription = prescriptionRepository.save(prescription);
        
        // Create and publish the event
        PrescriptionEvent event = PrescriptionEvent.builder()
                .prescriptionId(prescription.getId().toString())
                .patientName("Patient ID: " + prescription.getPatientId())
                .doctorName("Doctor ID: " + prescription.getDoctorId())
                .createdAt(prescription.getIssuedAt())
                .status("CREATED")
                .message("New prescription created with medication: " + prescription.getMedicationDetails())
                .build();
        
        eventPublisher.publishPrescriptionEvent(event);
        
        return modelMapper.map(prescription, PrescriptionResponseDTO.class);
    }

    @Override
    public Page<PrescriptionResponseDTO> getAllPrescriptions(Pageable pageable) {
        return prescriptionRepository.findAll(pageable)
                .map(prescription -> modelMapper.map(prescription, PrescriptionResponseDTO.class));
    }
}
