package com.management.prescriptionservice.service.impl;

import com.management.prescriptionservice.dto.IncompletePrescriptionMessage;
import com.management.prescriptionservice.dto.PrescriptionDTO;
import com.management.prescriptionservice.dto.PrescriptionRequest;
import com.management.prescriptionservice.exception.ResourceNotFoundException;
import com.management.prescriptionservice.exception.TCValidationException;
import com.management.prescriptionservice.model.MedicineItem;
import com.management.prescriptionservice.model.Prescription;
import com.management.prescriptionservice.model.Visit;
import com.management.prescriptionservice.model.enums.PrescriptionStatus;
import com.management.prescriptionservice.repository.PrescriptionRepository;
import com.management.prescriptionservice.service.PrescriptionService;
import com.management.prescriptionservice.service.VisitService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ModelMapper modelMapper;
    private final RabbitTemplate rabbitTemplate;
    private final VisitService visitService;

    @Value("${tc-validation-url}")
    private String tcValidationUrlValid;

    @Value("${gateway.base-url}")
    private String gatewayBaseUrl;

    private WebClient gatewayWebClient;

    @PostConstruct
    public void init() {
        this.gatewayWebClient = WebClient.builder()
                .baseUrl(gatewayBaseUrl)
                .build();
    }

    @Override
    public PrescriptionDTO createPrescription(PrescriptionRequest request, String doctorUserId) {
        validateTC(request.getPatientTC());

        List<MedicineItem> medicineItems = request.getMedicines().stream()
                .map(med -> fetchMedicineDetails(med.getMedicineId(), med.getMedicineName(), med.getQuantity()))
                .collect(Collectors.toCollection(ArrayList::new));

        // Create Visit
        Visit visit = visitService.createVisit(doctorUserId, request.getPatientTC());

        // Create Prescription
        Prescription prescription = new Prescription();
        prescription.setDoctorUserId(doctorUserId);
        prescription.setPatientTC(request.getPatientTC());
        prescription.setMedicines(medicineItems);
        prescription.setStatus(PrescriptionStatus.CREATED);
        prescription.setCreatedAt(LocalDateTime.now());

        // Save Prescription and Link to Visit
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        visit.setPrescription(savedPrescription);

        visitService.saveOrUpdate(visit);

        PrescriptionDTO dto = modelMapper.map(savedPrescription, PrescriptionDTO.class);
        dto.setVisitId(visit.getId());
        return dto;
    }

    private void validateTC(String tcNumber) {
        try {
            // Fetch response from TC validation service
            Map<String, Object> response = gatewayWebClient.get()
                    .uri(tcValidationUrlValid + "?tc={tc}", tcNumber)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block(Duration.ofSeconds(5));

            // Log the response for debugging
            System.out.println("Validation Response: " + response);

            // Ensure response is not null and contains the expected key
            if (response == null || !response.containsKey("isValid")) {
                throw new TCValidationException("Validation service returned an invalid response");
            }

            // Parse and return the validation result
            response.get("isValid");
        } catch (Exception e) {
            throw new TCValidationException("Error during TC validation: " + e.getMessage());
        }
    }

    @Override
    public PrescriptionDTO submitPrescription(Long prescriptionId, String pharmacyUserId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + prescriptionId));

        prescription.setPharmacyUserId(pharmacyUserId);
        prescription.setStatus(PrescriptionStatus.SUBMITTED);
        prescription.setUpdatedAt(LocalDateTime.now());

        if (prescription.getMedicines().isEmpty()) {
            prescription.setStatus(PrescriptionStatus.INCOMPLETE);


            IncompletePrescriptionMessage msg = new IncompletePrescriptionMessage();
            msg.setPrescriptionId(prescription.getId());
            msg.setPatientTC(prescription.getPatientTC());
            msg.setPharmacyUserId(prescription.getPharmacyUserId());
            msg.setMissingMedicinesCount(prescription.getMedicines().size());

            rabbitTemplate.convertAndSend("prescription.queue", msg);
        }

        Prescription updatedPrescription = prescriptionRepository.save(prescription);
        return modelMapper.map(updatedPrescription, PrescriptionDTO.class);
    }


    @Override
    public List<PrescriptionDTO> getIncompletePrescriptions(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return prescriptionRepository.findByStatusAndCreatedAtBetween(
                        PrescriptionStatus.INCOMPLETE,
                        start,
                        end
                ).stream()
                .map(p -> modelMapper.map(p, PrescriptionDTO.class))
                .collect(Collectors.toList());
    }

    private MedicineItem fetchMedicineDetails(String medicineId, String medicineName, int quantity) {
        try {
            MedicineItem medicine = gatewayWebClient.get()
                    .uri("/api/medicines/v1/{id}", medicineId)
                    .retrieve()
                    .bodyToMono(MedicineItem.class)
                    .block();

            if (medicine == null) {
                throw new ResourceNotFoundException("Medicine not found with ID: " + medicineId);
            }

            medicine.setMedicineId(medicineId);
            medicine.setQuantity(quantity);
            medicine.setMedicineName(medicineName);
            return medicine;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error fetching medicine details: " + e.getMessage());
        }
    }

}