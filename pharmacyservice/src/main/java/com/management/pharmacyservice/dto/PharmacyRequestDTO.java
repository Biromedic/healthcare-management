package com.management.pharmacyservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyRequestDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
}
