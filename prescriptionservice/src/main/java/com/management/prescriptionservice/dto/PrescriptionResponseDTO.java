package com.management.prescriptionservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponseDTO {
    private Long id;
    private String patientName;
    private String doctorName;
    private List<String> medicines;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
