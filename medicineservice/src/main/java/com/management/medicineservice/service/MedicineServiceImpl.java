package com.management.medicineservice.service;

import com.management.medicineservice.DTO.MedicineDTO;
import com.management.medicineservice.helper.ExcelHelper;
import com.management.medicineservice.model.Medicine;
import com.management.medicineservice.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final RedisTemplate<String, List<MedicineDTO>> redisTemplate;
    private final ExcelHelper excelHelper;
    private final ModelMapper modelMapper;

    private static final String MEDICINE_CACHE_KEY = "ALL_MEDICINES";

    @Override
    public void updateMedicineFromExcel() {
        try {
            ClassPathResource resource = new ClassPathResource("data/medicine_data_updated.xlsx");
            if (!resource.exists()) {
                throw new RuntimeException("Excel file not found in classpath: " + resource.getPath());
            }
            InputStream inputStream = resource.getInputStream();
            List<Medicine> medicines = excelHelper.readMedicinesFromExcel(inputStream);

            medicineRepository.saveAll(medicines);

            List<MedicineDTO> medicineDTOs = medicines.stream()
                    .map(medicine -> modelMapper.map(medicine, MedicineDTO.class))
                    .collect(Collectors.toList());

            redisTemplate.opsForValue().set(MEDICINE_CACHE_KEY, medicineDTOs);

        } catch (Exception e) {
            throw new RuntimeException("Error processing Excel file: " + e.getMessage());
        }
    }

    @Override
    public void cacheAllMedicines() {
        List<Medicine> medicines = medicineRepository.findAll();

        List<MedicineDTO> medicineDTOs = medicines.stream()
                .map(medicine -> modelMapper.map(medicine, MedicineDTO.class))
                .collect(Collectors.toList());
        redisTemplate.opsForValue().set(MEDICINE_CACHE_KEY, medicineDTOs);
    }

    @Override
    @Cacheable(value = "medicineNames", key = "#query")
    public List<MedicineDTO> searchMedicines(String query) {
        List<MedicineDTO> cachedMedicines = redisTemplate.opsForValue().get(MEDICINE_CACHE_KEY);
        if (cachedMedicines == null) {
            cacheAllMedicines();
            cachedMedicines = redisTemplate.opsForValue().get(MEDICINE_CACHE_KEY);
        }
        assert cachedMedicines != null;
        return cachedMedicines.stream()
                .filter(medicine -> medicine.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "0 0 22 ? * SUN")
    public void updateMedicineList() {
        updateMedicineFromExcel();
    }

    @Override
    public MedicineDTO getMedicineById(String id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + id));
        return modelMapper.map(medicine, MedicineDTO.class);
    }
}