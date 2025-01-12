package com.management.prescriptionservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequestDTO {
    private String patientName;
    private String doctorName;
    private List<String> medicines;
}
