package com.management.medicineservice.config;

import com.management.medicineservice.DTO.MedicineDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, List<MedicineDTO>> medicineRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<MedicineDTO>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}

