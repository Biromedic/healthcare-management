package com.management.prescriptionservice.service.impl;

import com.management.prescriptionservice.dto.VisitDTO;
import com.management.prescriptionservice.exception.ResourceNotFoundException;
import com.management.prescriptionservice.model.Visit;
import com.management.prescriptionservice.repository.VisitRepository;
import com.management.prescriptionservice.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final ModelMapper modelMapper;

    @Override
    public Visit createVisit(String doctorId, String patientTC) {
        Visit visit = new Visit();
        visit.setDoctorId(doctorId);
        visit.setPatientTC(patientTC);
        visit.setVisitDateTime(LocalDateTime.now());
        return visitRepository.save(visit);
    }

    @Override
    public VisitDTO getVisitDetails(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));
        return modelMapper.map(visit, VisitDTO.class);
    }
}
