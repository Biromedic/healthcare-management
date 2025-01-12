package com.management.medicineservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {
    @Id
    private String id;

    private String name;
    private String barcode;
    private String companyName;
    private String status;
}