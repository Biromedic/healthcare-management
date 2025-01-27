package com.management.medicineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.management.medicineservice")
@EnableMongoRepositories(basePackages = "com.management.medicineservice.repository")
public class MedicineserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicineserviceApplication.class, args);
    }

}
