package com.management.prescriptionservice.controller;

import com.management.prescriptionservice.dto.PrescriptionDTO;
import com.management.prescriptionservice.dto.PrescriptionRequest;
import com.management.prescriptionservice.dto.VisitDTO;
import com.management.prescriptionservice.service.PrescriptionService;
import com.management.prescriptionservice.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions/v1")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final VisitService visitService;

    @PostMapping
    public PrescriptionDTO createPrescription(
            @RequestBody PrescriptionRequest request,
            @RequestHeader("X-User-Id") String doctorUserId) {
        return prescriptionService.createPrescription(request, doctorUserId);
    }

    @PutMapping("/{id}/submit")
    public PrescriptionDTO submitPrescription(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String pharmacyUserId) {
        return prescriptionService.submitPrescription(id, pharmacyUserId);
    }

    @GetMapping("/incomplete")
    public List<PrescriptionDTO> getIncompletePrescriptions(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return prescriptionService.getIncompletePrescriptions(localDate);
    }

    @GetMapping("/visits/{id}")
    public VisitDTO getVisitDetails(@PathVariable Long id) {
        return visitService.getVisitDetails(id);
    }
}