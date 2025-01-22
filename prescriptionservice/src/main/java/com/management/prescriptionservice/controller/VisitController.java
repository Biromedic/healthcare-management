package com.management.prescriptionservice.controller;

import com.management.prescriptionservice.dto.CreateVisitRequestDTO;
import com.management.prescriptionservice.dto.VisitResponseDTO;
import com.management.prescriptionservice.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits/v1")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<VisitResponseDTO> createVisit(@RequestBody CreateVisitRequestDTO request) {
        VisitResponseDTO response = visitService.createVisit(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VisitResponseDTO>> getAllVisits() {
        List<VisitResponseDTO> responses = visitService.getAllVisits();
        return ResponseEntity.ok(responses);
    }
}
