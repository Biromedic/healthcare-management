package com.management.authenticationservice.security.service.securityServiceImpl;

import com.management.authenticationservice.excepiton.ErrorMessages;
import com.management.authenticationservice.excepiton.RoleNotFoundException;
import com.management.authenticationservice.model.Role;
import com.management.authenticationservice.model.User;
import com.management.authenticationservice.model.enums.AppRole;
import com.management.authenticationservice.repository.RoleRepository;
import com.management.authenticationservice.repository.UserRepository;
import com.management.authenticationservice.security.jwt.JwtUtils;
import com.management.authenticationservice.security.request.LoginRequest;
import com.management.authenticationservice.security.request.SignupRequest;
import com.management.authenticationservice.security.response.MessageResponse;
import com.management.authenticationservice.security.response.UserInfoResponse;
import com.management.authenticationservice.security.service.UserDetailsImpl;
import com.management.authenticationservice.security.service.securityServiceInterface.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public ResponseEntity<Map<String, Object>> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = performAuthentication(loginRequest);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromEmail(
                userDetails.getEmail(),
                userDetails.getId(),
                userDetails.getAuthorities().stream()
                        .map(auth -> new Role(AppRole.valueOf(auth.getAuthority())))
                        .toList()
        );

        List<String> roles = extractRoles(userDetails);

        return ResponseEntity.ok()
                .body(Map.of(
                        "token", jwtToken,
                        "userInfo", new UserInfoResponse(
                                userDetails.getId(),
                                userDetails.getEmail(),
                                roles
                        )
                ));
    }

    @Override
    public ResponseEntity<MessageResponse> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail().toLowerCase())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = createUser(signUpRequest);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Override
    public ResponseEntity<MessageResponse> signoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("You've been signed out!"));
    }

    @Override
    public ResponseEntity<?> validateToken(String token) {
        if (!jwtUtils.validateJwtToken(token)) {
            return ResponseEntity.ok(Map.of("isValid", false));
        }

        Long userId = Long.parseLong(jwtUtils.getUserIdFromJwtToken(token));
        List<String> roles = jwtUtils.getRolesFromJwtToken(token);
        return ResponseEntity.ok(Map.of("isValid", true, "userId", userId, "roles", roles));
    }

    @Override
    public ResponseEntity<UserInfoResponse> getUserDetails(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(new UserInfoResponse(
                userDetails.getId(),
                userDetails.getEmail(),
                extractRoles(userDetails)));
    }

    private Authentication performAuthentication(LoginRequest loginRequest) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException(ErrorMessages.BAD_CREDENTIALS);
        }
    }

    private List<String> extractRoles(UserDetailsImpl userDetails) {
        return userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList());
    }

    private User createUser(SignupRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail().toLowerCase());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());

        Role role = roleRepository.findByRoleName(
                        AppRole.valueOf("ROLE_" + signUpRequest.getRole().toUpperCase()))
                .orElseThrow(() -> new RoleNotFoundException(ErrorMessages.ROLE_NOT_FOUND));

        user.setRoles(Set.of(role));
        return user;
    }
}