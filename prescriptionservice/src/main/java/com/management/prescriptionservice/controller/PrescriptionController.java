package com.management.prescriptionservice.controller;

import com.management.prescriptionservice.dto.CreatePrescriptionRequestDTO;
import com.management.prescriptionservice.dto.PrescriptionResponseDTO;
import com.management.prescriptionservice.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions/v1")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(@RequestBody CreatePrescriptionRequestDTO request) {
        PrescriptionResponseDTO response = prescriptionService.createPrescription(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PrescriptionResponseDTO>> getAllPrescriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PrescriptionResponseDTO> responses = prescriptionService.getAllPrescriptions(pageable);
        return ResponseEntity.ok(responses);
    }
}
