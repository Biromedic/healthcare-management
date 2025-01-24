package com.management.prescriptionservice.service;

import com.management.prescriptionservice.dto.VisitDTO;
import com.management.prescriptionservice.model.Visit;

public interface VisitService {
    Visit createVisit(String doctorId, String patientTC);
    VisitDTO getVisitDetails(Long visitId);
}
