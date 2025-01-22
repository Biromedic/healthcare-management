package com.management.prescriptionservice.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
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
    private String missingDetails;
} 