package com.management.authenticationservice;

import com.management.authenticationservice.model.Role;
import com.management.authenticationservice.model.enums.AppRole;
import com.management.authenticationservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class RoleLoader {
    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles() {
        return args -> Arrays.stream(AppRole.values()).forEach(appRole -> {
            if (roleRepository.findByRoleName(appRole).isEmpty()) {
                roleRepository.save(new Role(appRole));
            }
        });
    }
}
