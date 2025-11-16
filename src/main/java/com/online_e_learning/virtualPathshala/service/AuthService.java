package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.config.JwtTokenProvider;
import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.requestDto.LoginRequest;
import com.online_e_learning.virtualPathshala.requestDto.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            System.out.println("🔐 Login attempt for: " + loginRequest.getEmail() + " with role: " + loginRequest.getRole());

            // ✅ FIRST: Check if user exists
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            if (userOptional.isEmpty()) {
                System.out.println("❌ User not found: " + loginRequest.getEmail());
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "error", "Invalid credentials"));
            }

            User user = userOptional.get();
            System.out.println("👤 Found user: " + user.getEmail() + " with role: " + user.getRole());

            // ✅ CRITICAL FIX: Admin Role Validation
            if (loginRequest.getRole() != null && loginRequest.getRole().equalsIgnoreCase("ADMIN")) {
                // Only allow admin login for the specific admin user
                if (!user.getRole().equals(Role.ADMIN)) {
                    System.out.println("🚫 BLOCKED: User " + user.getEmail() + " tried to login as ADMIN but has role: " + user.getRole());
                    return ResponseEntity.badRequest()
                            .body(Map.of("success", false, "error", "Admin access denied. Only authorized administrators can login."));
                }

                // Additional check: Ensure it's the predefined admin
                if (!user.getEmail().equals("admin@virtualpathshala.com")) {
                    System.out.println("🚫 BLOCKED: Unauthorized admin login attempt: " + user.getEmail());
                    return ResponseEntity.badRequest()
                            .body(Map.of("success", false, "error", "Administrator access restricted."));
                }
            }

            // ✅ SECOND: Validate password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
                System.out.println("❌ Invalid password for: " + loginRequest.getEmail());
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "error", "Invalid credentials"));
            }

            // ✅ THIRD: Check if user is active
            if (user.getStatus() != null && !user.getStatus().name().equals("ACTIVE")) {
                System.out.println("❌ User account not active: " + loginRequest.getEmail());
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "error", "Account is not active. Please contact administrator."));
            }

            // ✅ FOURTH: Role matching validation
            if (loginRequest.getRole() != null && !user.getRole().name().equalsIgnoreCase(loginRequest.getRole())) {
                System.out.println("❌ Role mismatch. User role: " + user.getRole() + ", Requested role: " + loginRequest.getRole());
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "error",
                                "Invalid role selection. Please select " + user.getRole() + " role to login."));
            }

            // ✅ FIFTH: Authenticate with Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // ✅ SIXTH: Generate JWT token
            String jwt = tokenProvider.generateToken(authentication);
            System.out.println("✅ Login successful for: " + user.getEmail() + " with role: " + user.getRole());

            // ✅ Prepare user data for response
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole().name());
            userData.put("mobile", user.getMobile());
            userData.put("status", user.getStatus() != null ? user.getStatus().name() : "ACTIVE");

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", jwt,
                    "tokenType", "Bearer",
                    "user", userData,
                    "message", "Login successful"
            ));

        } catch (BadCredentialsException e) {
            System.out.println("❌ Bad credentials for: " + loginRequest.getEmail());
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "error", "Invalid email or password"));
        } catch (Exception e) {
            System.out.println("❌ Login error for " + loginRequest.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "error", "Login failed: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> signup(SignupRequest signupRequest) {
        try {
            // ✅ Check if user already exists
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "error", "Email is already registered!"));
            }

            // ✅ CRITICAL: Prevent admin registration through signup
            if (signupRequest.getRole() != null &&
                    (signupRequest.getRole().equalsIgnoreCase("ADMIN") ||
                            signupRequest.getRole().equals(Role.ADMIN.name()))) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "error", "Admin registration is not allowed through this portal."));
            }

            // ✅ Create new user
            User user = new User();
            user.setName(signupRequest.getName());
            user.setEmail(signupRequest.getEmail());
            user.setMobile(signupRequest.getMobile());

            // Set role - default to STUDENT if not provided or invalid
            try {
                if (signupRequest.getRole() != null) {
                    user.setRole(Role.valueOf(signupRequest.getRole().toUpperCase()));
                } else {
                    user.setRole(Role.STUDENT);
                }
            } catch (IllegalArgumentException e) {
                user.setRole(Role.STUDENT); // Default role
            }

            user.setStatus(com.online_e_learning.virtualPathshala.enums.Status.ACTIVE);
            user.setPasswordHash(passwordEncoder.encode(signupRequest.getPassword()));

            User savedUser = userRepository.save(user);

            System.out.println("✅ New user registered: " + savedUser.getEmail() + " with role: " + savedUser.getRole());

            // ✅ Automatically login after signup
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(signupRequest.getEmail());
            loginRequest.setPassword(signupRequest.getPassword());
            loginRequest.setRole(savedUser.getRole().name());

            return login(loginRequest);

        } catch (Exception e) {
            System.out.println("❌ Signup error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "error", "Registration failed: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> refreshToken(String token) {
        try {
            if (token == null || !tokenProvider.validateToken(token)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "error", "Invalid or expired token"));
            }

            String newToken = tokenProvider.refreshToken(token);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", newToken,
                    "tokenType", "Bearer"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "error", "Token refresh failed: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> validateToken(String token) {
        try {
            boolean isValid = tokenProvider.validateToken(token);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "valid", isValid
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "error", "Token validation failed: " + e.getMessage()));
        }
    }
}