package com.management.pharmacyservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionProcessRequestDTO {
    private Long prescriptionId;
    private String pharmacyName;
    private List<String> medicinesProvided;
}
