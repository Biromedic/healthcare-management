package com.management.prescriptionservice.service;

import com.management.prescriptionservice.dto.CreateVisitRequestDTO;
import com.management.prescriptionservice.dto.VisitResponseDTO;

import java.util.List;

public interface VisitService {
    VisitResponseDTO createVisit(CreateVisitRequestDTO request);
    List<VisitResponseDTO> getAllVisits();
}
