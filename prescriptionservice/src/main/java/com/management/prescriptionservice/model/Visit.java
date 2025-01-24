package com.management.prescriptionservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String doctorId;

    @Column(nullable = false)
    private String patientTC;

    @Column(nullable = false)
    private LocalDateTime visitDateTime;

    @OneToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;
}
