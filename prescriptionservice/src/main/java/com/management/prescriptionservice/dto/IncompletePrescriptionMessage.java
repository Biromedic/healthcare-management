package com.management.prescriptionservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncompletePrescriptionMessage {
    private Long prescriptionId;
    private String patientTC;
    private Integer missingMedicinesCount;
    private String pharmacyUserId;

}
