package com.management.medicineservice.controller;

import com.management.medicineservice.DTO.MedicineDTO;
import com.management.medicineservice.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines/v1")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping("/search")
    public ResponseEntity<List<MedicineDTO>> searchMedicines(@RequestParam String query) {
        return ResponseEntity.ok(medicineService.searchMedicines(query));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadMedicines() {
        medicineService.updateMedicineFromExcel();
        return ResponseEntity.ok("Medicines updated from Excel and cached in Redis.");
    }

    @PostMapping("/cache")
    public ResponseEntity<String> cacheMedicines() {
        medicineService.cacheAllMedicines();
        return ResponseEntity.ok("All medicines cached in Redis.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDTO> getMedicineById(@PathVariable String id) {
        return ResponseEntity.ok(medicineService.getMedicineById(id));
    }
}