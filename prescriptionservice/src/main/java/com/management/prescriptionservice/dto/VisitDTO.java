package com.management.prescriptionservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VisitDTO {
    private Long id;
    private String doctorId;
    private String patientTC;
    private LocalDateTime visitDateTime;
    private Long prescriptionId;
}
