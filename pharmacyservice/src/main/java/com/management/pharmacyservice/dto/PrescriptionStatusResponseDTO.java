package com.management.pharmacyservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionStatusResponseDTO {
    private Long prescriptionId;
    private String status; // "COMPLETED", "INCOMPLETE", etc.
    private String pharmacyName;
    private List<String> missingMedicines;
}
