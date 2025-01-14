package com.management.pharmacyservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pharmacies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;
}
