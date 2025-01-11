package com.management.medicineservice.service;

import com.management.medicineservice.DTO.MedicineDTO;
import com.management.medicineservice.helper.ExcelHelper;
import com.management.medicineservice.model.Medicine;
import com.management.medicineservice.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

            File file = new ClassPathResource("data/medicine_data.xlsx").getFile();
            List<Medicine> medicines = excelHelper.readMedicinesFromExcel(file.getAbsolutePath());

            medicineRepository.saveAll(medicines);

            List<MedicineDTO> medicineDTOs = medicines.stream()
                    .map(medicine -> modelMapper.map(medicine, MedicineDTO.class))
                    .collect(Collectors.toList());
            redisTemplate.opsForValue().set(MEDICINE_CACHE_KEY, medicineDTOs);

        } catch (IOException e) {
            throw new RuntimeException("Excel file not found in resources/data folder", e);
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
}
