package com.management.pharmacyservice.client;

import com.management.pharmacyservice.dto.MedicineDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "medicine-service", url = "${MEDICINE_SERVICE_URL}")
public interface MedicineServiceClient {
    @GetMapping("/api/medicines/v1/search")
    List<MedicineDTO> searchMedicines(@RequestParam("query") String query);
}
