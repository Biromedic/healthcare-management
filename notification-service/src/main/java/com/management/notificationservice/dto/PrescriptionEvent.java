package com.management.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionEvent {
    private String prescriptionId;
    private String patientName;
    private String doctorName;
    private String pharmacyName;
    private LocalDateTime createdAt;
    private String status;
    private String message;
}
