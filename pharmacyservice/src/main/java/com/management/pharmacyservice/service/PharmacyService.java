package com.management.pharmacyservice.service;

import com.management.pharmacyservice.dto.*;

import java.util.List;

public interface PharmacyService {
    void processPrescription(PrescriptionProcessRequestDTO requestDTO);
}
