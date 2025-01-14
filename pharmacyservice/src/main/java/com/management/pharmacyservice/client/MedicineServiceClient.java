package com.management.pharmacyservice.client;

import com.management.pharmacyservice.dto.MedicineDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "medicine-service", url = "http://localhost:8082/api/medicines/v1")
public interface MedicineServiceClient {
    @GetMapping("/search")
    List<MedicineDTO> searchMedicines(@RequestParam("query") String query);
}
