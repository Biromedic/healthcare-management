package com.management.prescriptionservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long doctorId;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private LocalDateTime visitDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription_id", referencedColumnName = "id")
    private Prescription prescription;
}
