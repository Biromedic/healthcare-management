package com.management.authenticationservice.security.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenValidationRequest {
    private String token;
}
