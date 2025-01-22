package com.management.prescriptionservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateVisitRequestDTO {
    private Long doctorId;
    private Long patientId;
    private LocalDateTime visitDate;
    private Long prescriptionId;
}
