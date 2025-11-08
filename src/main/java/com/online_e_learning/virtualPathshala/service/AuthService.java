package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.converter.UserConverter;
import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.enums.Status;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.requestDto.LoginRequest;
import com.online_e_learning.virtualPathshala.requestDto.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            // Find user by email
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("User not found"));
            }

            User user = userOptional.get();

            // Check if account is active
            if (user.getStatus() != Status.ACTIVE) {
                return ResponseEntity.badRequest().body(createErrorResponse("Account is not active. Please contact admin."));
            }

            // Verify password
            String hashedPassword = hashPassword(loginRequest.getPassword());
            if (!user.getPasswordHash().equals(hashedPassword)) {
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid password"));
            }

            // Create success response with user data
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole().toString());
            userData.put("status", user.getStatus().toString());

            response.put("data", userData);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Login failed: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> signup(SignupRequest signupRequest) {
        try {
            System.out.println("📝 Signup request received: " + signupRequest.getEmail() + ", Role: " + signupRequest.getRole());

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

            // Check password match
            if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(createErrorResponse("Passwords do not match"));
            }

            // Check if email already exists
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email already exists: " + signupRequest.getEmail()));
            }

            // Validate and convert role
            Role userRole;
            try {
                userRole = Role.valueOf(signupRequest.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid role. Must be STUDENT, TEACHER or ADMIN"));
            }

            // Create new user
            User user = new User();
            user.setName(signupRequest.getName().trim());
            user.setEmail(signupRequest.getEmail().trim().toLowerCase());
            user.setPasswordHash(hashPassword(signupRequest.getPassword()));
            user.setMobile(signupRequest.getMobile().trim());
            user.setRole(userRole);
            user.setStatus(Status.ACTIVE); // Direct activate for now

            User savedUser = userRepository.save(user);
            System.out.println("✅ User registered successfully: " + savedUser.getId());

            // Return success response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("userId", savedUser.getId());
            response.put("role", savedUser.getRole().toString());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Registration failed: " + e.getMessage()));
        }
    }

    // ✅ YEH MISSING METHOD ADD KARO
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return response;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}