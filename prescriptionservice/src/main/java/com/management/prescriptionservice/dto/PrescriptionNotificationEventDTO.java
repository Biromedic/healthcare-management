package com.management.prescriptionservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionNotificationEventDTO {
    private String patientId;
    private String prescriptionDetails;
}
