package com.management.medicineservice.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter

public class MedicineDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String barcode;
    private String companyName;
    private String status;
    private Integer price;
}

