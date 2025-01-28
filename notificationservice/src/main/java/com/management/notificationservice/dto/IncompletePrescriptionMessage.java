package com.management.notificationservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncompletePrescriptionMessage {
    private Long prescriptionId;
    private String patientTC;
    private String pharmacyUserId;
    private int missingMedicinesCount;
}
