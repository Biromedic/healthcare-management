package com.management.pharmacyservice.controller;

import com.management.pharmacyservice.dto.PrescriptionProcessRequestDTO;
import com.management.pharmacyservice.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/pharmacies/v1")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PostMapping("/process-prescription")
    public ResponseEntity<String> processPrescription(@RequestBody PrescriptionProcessRequestDTO prescriptionRequest) {
        pharmacyService.processPrescription(prescriptionRequest);
        return ResponseEntity.ok("Prescription processed successfully.");
    }
}
