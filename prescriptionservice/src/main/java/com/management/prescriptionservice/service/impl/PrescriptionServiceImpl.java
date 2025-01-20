package com.management.prescriptionservice.service.impl;

import com.management.prescriptionservice.dto.CreatePrescriptionRequestDTO;
import com.management.prescriptionservice.dto.PrescriptionResponseDTO;
import com.management.prescriptionservice.event.PrescriptionEvent;
import com.management.prescriptionservice.event.PrescriptionEventPublisher;
import com.management.prescriptionservice.model.Prescription;
import com.management.prescriptionservice.repository.PrescriptionRepository;
import com.management.prescriptionservice.service.PrescriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ModelMapper modelMapper;
    private final PrescriptionEventPublisher eventPublisher;

    @Override
    @Transactional
    public PrescriptionResponseDTO createPrescription(CreatePrescriptionRequestDTO request) {
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
                .patientName("Patient ID: " + prescription.getPatientId()) // You might want to fetch actual names from a user service
                .doctorName("Doctor ID: " + prescription.getDoctorId())
                .createdAt(prescription.getIssuedAt())
                .status("CREATED")
                .message("New prescription created with medication: " + prescription.getMedicationDetails())
                .build();
        
        eventPublisher.publishPrescriptionEvent(event);
        
        return modelMapper.map(prescription, PrescriptionResponseDTO.class);
    }

    @Override
    public List<PrescriptionResponseDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll()
                .stream().map(prescription -> modelMapper.map(prescription, PrescriptionResponseDTO.class))
                .collect(Collectors.toList());
    }
}
