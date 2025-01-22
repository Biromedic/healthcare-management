package com.management.authenticationservice.security.service.securityServiceInterface;

import com.management.authenticationservice.security.request.LoginRequest;
import com.management.authenticationservice.security.request.SignupRequest;
import com.management.authenticationservice.security.response.MessageResponse;
import com.management.authenticationservice.security.response.UserInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface AuthService {
    ResponseEntity<Map<String, Object>> authenticateUser(LoginRequest loginRequest);

    ResponseEntity<MessageResponse> registerUser(SignupRequest signUpRequest);

    ResponseEntity<MessageResponse> signoutUser();

    ResponseEntity<UserInfoResponse> getUserDetails(Authentication authentication);

    boolean validateToken(String token);
}