package com.management.medicineservice.controller;

import com.management.medicineservice.DTO.MedicineDTO;
import com.management.medicineservice.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/medicines/v1")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping("/search")
    public ResponseEntity<Page<MedicineDTO>> searchMedicines(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<MedicineDTO> resultPage = medicineService.searchMedicines(query, page, size);
        return ResponseEntity.ok(resultPage);
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