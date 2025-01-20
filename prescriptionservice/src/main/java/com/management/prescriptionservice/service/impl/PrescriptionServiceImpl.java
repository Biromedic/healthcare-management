package com.management.prescriptionservice.service.impl;

import com.management.prescriptionservice.dto.CreatePrescriptionRequestDTO;
import com.management.prescriptionservice.dto.PrescriptionResponseDTO;
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
        return modelMapper.map(prescription, PrescriptionResponseDTO.class);
    }

    @Override
    public List<PrescriptionResponseDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll()
                .stream().map(prescription -> modelMapper.map(prescription, PrescriptionResponseDTO.class))
                .collect(Collectors.toList());
    }
}
