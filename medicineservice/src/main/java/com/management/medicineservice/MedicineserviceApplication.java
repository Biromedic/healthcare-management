package com.management.medicineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
})
@EnableMongoRepositories(basePackages = "com.management.medicineservice.repository")
public class MedicineserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicineserviceApplication.class, args);
    }

}
