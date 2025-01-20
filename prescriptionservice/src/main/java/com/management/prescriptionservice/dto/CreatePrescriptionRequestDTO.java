package com.management.prescriptionservice.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePrescriptionRequestDTO {
    private Long doctorId;
    private Long patientId;
    private String medicationDetails;
}
