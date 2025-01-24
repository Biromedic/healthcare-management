package com.management.prescriptionservice.dto;

import com.management.prescriptionservice.model.MedicineItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequest {
    private String patientTC;
    private List<MedicineItem> medicines;
}