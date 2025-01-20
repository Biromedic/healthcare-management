package com.management.prescriptionservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponseDTO {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private String medicationDetails;
    private LocalDateTime issuedAt;
}
