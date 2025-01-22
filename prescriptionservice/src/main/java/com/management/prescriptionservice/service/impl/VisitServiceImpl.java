package com.management.prescriptionservice.service.impl;

import com.management.prescriptionservice.dto.CreateVisitRequestDTO;
import com.management.prescriptionservice.dto.VisitResponseDTO;
import com.management.prescriptionservice.model.Prescription;
import com.management.prescriptionservice.model.Visit;
import com.management.prescriptionservice.repository.PrescriptionRepository;
import com.management.prescriptionservice.repository.VisitRepository;
import com.management.prescriptionservice.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final ModelMapper modelMapper;

    @Override
    public VisitResponseDTO createVisit(CreateVisitRequestDTO request) {
        Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid prescription ID"));

        Visit visit = Visit.builder()
                .doctorId(request.getDoctorId())
                .patientId(request.getPatientId())
                .visitDate(request.getVisitDate())
                .prescription(prescription)
                .build();

        visit = visitRepository.save(visit);
        return modelMapper.map(visit, VisitResponseDTO.class);
    }

    @Override
    public List<VisitResponseDTO> getAllVisits() {
        return visitRepository.findAll()
                .stream()
                .map(visit -> modelMapper.map(visit, VisitResponseDTO.class))
                .collect(Collectors.toList());
    }
}
