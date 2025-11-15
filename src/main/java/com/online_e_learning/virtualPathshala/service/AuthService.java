package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.config.JwtTokenProvider;
import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.enums.Status;
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
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordMigrationService passwordMigrationService;

    /**
     * Authenticates user and returns JWT token upon successful login
     */
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            System.out.println("🔐 Login attempt for email: " + loginRequest.getEmail());

            // Validate input parameters
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email is required"));
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Password is required"));
            }
            if (loginRequest.getRole() == null || loginRequest.getRole().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Role is required"));
            }

            // Find user by email
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail().trim().toLowerCase());

            if (userOptional.isEmpty()) {
                System.out.println("❌ User not found: " + loginRequest.getEmail());
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid email or password"));
            }

            User user = userOptional.get();

            // Check if account is active
            if (user.getStatus() != Status.ACTIVE) {
                System.out.println("❌ Account not active: " + loginRequest.getEmail());
                return ResponseEntity.badRequest().body(createErrorResponse("Account is not active. Please contact administrator."));
            }

            // Validate role
            String requestedRole = loginRequest.getRole().toUpperCase();
            if (!user.getRole().name().equals(requestedRole)) {
                System.out.println("❌ Role mismatch for: " + loginRequest.getEmail() + " Expected: " + user.getRole() + " Requested: " + requestedRole);
                return ResponseEntity.badRequest().body(createErrorResponse("Role mismatch. Please select correct user role."));
            }

            // ✅ Check if password needs migration from SHA-256 to BCrypt
            if (passwordMigrationService.needsMigration(user.getPasswordHash())) {
                System.out.println("🔄 Password migration needed for user: " + user.getEmail());
                if (passwordMigrationService.migratePasswordDuringLogin(loginRequest.getPassword(), user.getPasswordHash(), user)) {
                    System.out.println("✅ Password migrated successfully for: " + user.getEmail());
                    // Reload user with updated password
                    user = userRepository.findByEmail(loginRequest.getEmail().trim().toLowerCase())
                            .orElseThrow(() -> new RuntimeException("User not found after password migration"));
                } else {
                    System.out.println("❌ Password migration failed for: " + user.getEmail());
                    return ResponseEntity.badRequest().body(createErrorResponse("Invalid email or password"));
                }
            }

            // Authenticate user using Spring Security
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail().trim().toLowerCase(),
                                loginRequest.getPassword()
                        )
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Generate JWT token
                String jwtToken = tokenProvider.generateToken(authentication);
                System.out.println("✅ Login successful for: " + loginRequest.getEmail() + " Role: " + user.getRole());

                // Create success response with JWT token
                Map<String, Object> response = createSuccessResponse(user, jwtToken);
                return ResponseEntity.ok(response);

            } catch (BadCredentialsException e) {
                System.out.println("❌ Invalid password for: " + loginRequest.getEmail());
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid email or password"));
            }

        } catch (Exception e) {
            System.out.println("❌ Login error for: " + loginRequest.getEmail() + " - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Login failed: " + e.getMessage()));
        }
    }

    /**
     * Registers a new user in the system
     */
    public ResponseEntity<?> signup(SignupRequest signupRequest) {
        try {
            System.out.println("📝 Signup request received for: " + signupRequest.getEmail() + ", Role: " + signupRequest.getRole());

            // Validate required fields
            if (signupRequest.getName() == null || signupRequest.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Name is required"));
            }
            if (signupRequest.getEmail() == null || signupRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email is required"));
            }
            if (signupRequest.getPassword() == null || signupRequest.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Password is required"));
            }
            if (signupRequest.getMobile() == null || signupRequest.getMobile().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Mobile number is required"));
            }
            if (signupRequest.getRole() == null || signupRequest.getRole().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Role is required"));
            }

            // Validate email format
            String email = signupRequest.getEmail().trim().toLowerCase();
            if (!isValidEmail(email)) {
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid email format"));
            }

            // Validate mobile number format
            String mobile = signupRequest.getMobile().trim();
            if (!isValidMobileNumber(mobile)) {
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid mobile number format"));
            }

            // Validate password strength
            if (signupRequest.getPassword().length() < 6) {
                return ResponseEntity.badRequest().body(createErrorResponse("Password must be at least 6 characters long"));
            }

            // Check password match
            if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(createErrorResponse("Passwords do not match"));
            }

            // Check if email already exists
            if (userRepository.existsByEmail(email)) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email already exists: " + email));
            }

            // Validate and convert role
            Role userRole;
            try {
                userRole = Role.valueOf(signupRequest.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid role. Must be STUDENT, TEACHER or ADMIN"));
            }

            // ✅ ADMIN cannot signup - only through system
            if (userRole == Role.ADMIN) {
                return ResponseEntity.badRequest().body(createErrorResponse("Admin registration is not allowed. Please contact system administrator."));
            }

            // For TEACHER role, set status as INACTIVE (requires admin approval)
            Status status = (userRole == Role.TEACHER) ? Status.INACTIVE : Status.ACTIVE;

            // Create new user with BCrypt encoded password
            User user = new User();
            user.setName(signupRequest.getName().trim());
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode(signupRequest.getPassword()));
            user.setMobile(mobile);
            user.setRole(userRole);
            user.setStatus(status);

            User savedUser = userRepository.save(user);
            System.out.println("✅ User registered successfully - ID: " + savedUser.getId() + ", Email: " + savedUser.getEmail() + ", Role: " + savedUser.getRole());

            // Return success response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User registered successfully" + (status == Status.INACTIVE ? ". Waiting for admin approval." : ""));
            response.put("userId", savedUser.getId());
            response.put("email", savedUser.getEmail());
            response.put("role", savedUser.getRole().toString());
            response.put("status", savedUser.getStatus().toString());
            response.put("name", savedUser.getName());
            response.put("mobile", savedUser.getMobile());
            response.put("requiresApproval", status == Status.INACTIVE);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Registration error for: " + signupRequest.getEmail() + " - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Registration failed: " + e.getMessage()));
        }
    }

    /**
     * Refresh JWT token
     */
    public ResponseEntity<?> refreshToken(String oldToken) {
        try {
            if (!tokenProvider.validateToken(oldToken)) {
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid token"));
            }

            String email = tokenProvider.getEmailFromJWT(oldToken);
            String newToken = tokenProvider.refreshToken(oldToken);

            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("User not found"));
            }

            User user = userOptional.get();
            Map<String, Object> response = createSuccessResponse(user, newToken);
            response.put("message", "Token refreshed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Token refresh error: " + e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse("Token refresh failed: " + e.getMessage()));
        }
    }

    /**
     * Validate JWT token
     */
    public ResponseEntity<?> validateToken(String token) {
        try {
            if (!tokenProvider.validateToken(token)) {
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid token"));
            }

            String email = tokenProvider.getEmailFromJWT(token);
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("User not found"));
            }

            User user = userOptional.get();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("valid", true);
            response.put("email", user.getEmail());
            response.put("role", user.getRole().toString());
            response.put("status", user.getStatus().toString());
            response.put("remainingTime", tokenProvider.getRemainingTime(token));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Token validation error: " + e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse("Token validation failed: " + e.getMessage()));
        }
    }

    /**
     * Get current user profile
     */
    public ResponseEntity<?> getCurrentUser(String email) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("User not found"));
            }

            User user = userOptional.get();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", createUserData(user));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Get user profile error: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Failed to get user profile: " + e.getMessage()));
        }
    }

    /**
     * Verify user credentials manually
     */
    public boolean verifyUserCredentials(String email, String password) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Check if password needs migration
                if (passwordMigrationService.needsMigration(user.getPasswordHash())) {
                    return passwordMigrationService.migratePasswordDuringLogin(password, user.getPasswordHash(), user);
                }

                // Verify with BCrypt
                return passwordEncoder.matches(password, user.getPasswordHash());
            }
            return false;
        } catch (Exception e) {
            System.out.println("❌ Credential verification error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Create success response with user data and token
     */
    private Map<String, Object> createSuccessResponse(User user, String jwtToken) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("token", jwtToken);
        response.put("tokenType", "Bearer");
        response.put("expiresIn", 86400000); // 24 hours in ms
        response.put("user", createUserData(user));
        return response;
    }

    /**
     * Create user data map
     */
    private Map<String, Object> createUserData(User user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("role", user.getRole().toString());
        userData.put("status", user.getStatus().toString());
        userData.put("mobile", user.getMobile());
        userData.put("createdAt", user.getCreatedAt());
        userData.put("updatedAt", user.getUpdatedAt());
        return userData;
    }

    /**
     * Create standardized error response
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Validate mobile number format
     */
    private boolean isValidMobileNumber(String mobile) {
        if (mobile == null || mobile.trim().isEmpty()) {
            return false;
        }
        // Remove any non-digit characters
        String digitsOnly = mobile.replaceAll("\\D", "");
        // Basic validation: 10-15 digits
        return digitsOnly.matches("\\d{10,15}");
    }

    /**
     * Check if user exists by email
     */
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Change user password
     */
    public ResponseEntity<?> changePassword(String email, String currentPassword, String newPassword) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("User not found"));
            }

            User user = userOptional.get();

            // Verify current password
            if (!verifyUserCredentials(email, currentPassword)) {
                return ResponseEntity.badRequest().body(createErrorResponse("Current password is incorrect"));
            }

            // Update to new password
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            System.out.println("✅ Password changed successfully for: " + email);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Password changed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Password change error: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Password change failed: " + e.getMessage()));
        }
    }
}