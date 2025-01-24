package com.management.prescriptionservice.dto;

import com.management.prescriptionservice.model.MedicineItem;
import com.management.prescriptionservice.model.enums.PrescriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
    private Long id;
    private String doctorUserId;
    private String pharmacyUserId;
    private String patientTC;
    private List<MedicineItem> medicines;
    private PrescriptionStatus status;
}