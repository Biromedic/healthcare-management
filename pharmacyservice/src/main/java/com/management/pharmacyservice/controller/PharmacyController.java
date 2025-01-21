package com.management.pharmacyservice.controller;

import com.management.pharmacyservice.dto.MedicineDTO;
import com.management.pharmacyservice.dto.PharmacyRequestDTO;
import com.management.pharmacyservice.dto.PharmacyResponseDTO;
import com.management.pharmacyservice.dto.PrescriptionProcessRequestDTO;
import com.management.pharmacyservice.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacies/v1")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PostMapping
    public ResponseEntity<PharmacyResponseDTO> createPharmacy(@RequestBody PharmacyRequestDTO requestDTO) {
        return ResponseEntity.ok(pharmacyService.createPharmacy(requestDTO));
    }

    @PostMapping("/process-prescription")
    public ResponseEntity<String> processPrescription(@RequestBody PrescriptionProcessRequestDTO prescriptionRequest) {
        pharmacyService.processPrescription(prescriptionRequest);
        return ResponseEntity.ok("Prescription processed successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacyResponseDTO> getPharmacyById(@PathVariable Long id) {
        return ResponseEntity.ok(pharmacyService.getPharmacyById(id));
    }

    @GetMapping
    public ResponseEntity<List<PharmacyResponseDTO>> getAllPharmacies() {
        return ResponseEntity.ok(pharmacyService.getAllPharmacies());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePharmacy(@PathVariable Long id) {
        pharmacyService.deletePharmacy(id);
        return ResponseEntity.ok("Pharmacy deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<MedicineDTO>> searchMedicines(@RequestParam String query) {
        List<MedicineDTO> medicines = pharmacyService.searchMedicines(query);
        return ResponseEntity.ok(medicines);
    }
}
