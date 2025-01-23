package com.management.authenticationservice.controller;

import com.management.authenticationservice.security.request.LoginRequest;
import com.management.authenticationservice.security.request.SignupRequest;
import com.management.authenticationservice.security.request.TokenValidationRequest;
import com.management.authenticationservice.security.response.MessageResponse;
import com.management.authenticationservice.security.response.UserInfoResponse;
import com.management.authenticationservice.security.service.securityServiceInterface.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Email already in use")
    })
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @Operation(summary = "Authenticate user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @Operation(summary = "Get logged-in user details")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserDetails(Authentication authentication) {
        return authService.getUserDetails(authentication);
    }

    @Operation(summary = "Sign out the user")
    @PostMapping("/sign-out")
    public ResponseEntity<MessageResponse> signOutUser() {
        return authService.signoutUser();
    }

    @Operation(summary = "Validate token")
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return authService.validateToken(token);
    }
}
