package com.management.prescriptionservice.service.impl;

import com.management.prescriptionservice.dto.PrescriptionDTO;
import com.management.prescriptionservice.dto.PrescriptionRequest;
import com.management.prescriptionservice.exception.ResourceNotFoundException;
import com.management.prescriptionservice.exception.TCValidationException;
import com.management.prescriptionservice.model.Prescription;
import com.management.prescriptionservice.model.Visit;
import com.management.prescriptionservice.model.enums.PrescriptionStatus;
import com.management.prescriptionservice.repository.PrescriptionRepository;
import com.management.prescriptionservice.service.PrescriptionService;
import com.management.prescriptionservice.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
    private final WebClient webClient;
    private final VisitService visitService;

    @Value("${tc-validation-url}")
    private String tcValidationUrlValid;

    @Override
    public PrescriptionDTO createPrescription(PrescriptionRequest request, String doctorUserId) {
        validateTC(request.getPatientTC());

        // Create Visit
        Visit visit = visitService.createVisit(doctorUserId, request.getPatientTC());

        // Create Prescription
        Prescription prescription = new Prescription();
        prescription.setDoctorUserId(doctorUserId);
        prescription.setPatientTC(request.getPatientTC());
        prescription.setMedicines(request.getMedicines());
        prescription.setStatus(PrescriptionStatus.CREATED);
        prescription.setCreatedAt(LocalDateTime.now());

        // Save Prescription and Link to Visit
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        visit.setPrescription(savedPrescription);

        return modelMapper.map(savedPrescription, PrescriptionDTO.class);
    }


    private void validateTC(String tcNumber) {
        try {
            // Fetch response from TC validation service
            Map<String, Object> response = webClient.get()
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
            rabbitTemplate.convertAndSend("prescription.queue", prescription);
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
}