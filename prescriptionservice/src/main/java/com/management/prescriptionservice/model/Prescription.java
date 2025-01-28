package com.management.prescriptionservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.management.prescriptionservice.model.enums.PrescriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_user_id", nullable = false)
    private String doctorUserId; // From Auth Service

    @Column(name = "pharmacy_user_id")
    private String pharmacyUserId; // From Auth Service

    @Column(nullable = false)
    private String patientTC; // Mocked TC lookup

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "prescription")
    @JsonIgnore
    private Visit visit;

    @ElementCollection
    @CollectionTable(name = "prescription_medicines", joinColumns = @JoinColumn(name = "prescription_id"))
    private List<MedicineItem> medicines;

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status = PrescriptionStatus.CREATED;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}

