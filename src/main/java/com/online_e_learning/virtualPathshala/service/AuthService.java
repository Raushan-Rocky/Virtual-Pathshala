package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.requestDto.ApiResponse; // ✅ Changed to requestDto
import com.online_e_learning.virtualPathshala.requestDto.LoginRequest;
import com.online_e_learning.virtualPathshala.requestDto.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> signup(SignupRequest signupRequest) {
        try {
            // Check if user already exists
            if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
                ApiResponse errorResponse = new ApiResponse();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("User already exists with this email");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Create new user
            User user = new User();
            user.setName(signupRequest.getName());
            user.setEmail(signupRequest.getEmail());
            user.setPasswordHash(signupRequest.getPassword()); // Encrypt later
            user.setMobile(signupRequest.getMobile());
            user.setRole(com.online_e_learning.virtualPathshala.enums.Role.STUDENT);
            user.setStatus(com.online_e_learning.virtualPathshala.enums.Status.ACTIVE);

            User savedUser = userRepository.save(user);

            ApiResponse successResponse = new ApiResponse();
            successResponse.setSuccess(true);
            successResponse.setMessage("User registered successfully");
            successResponse.setData(savedUser);

            return ResponseEntity.ok().body(successResponse);

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Registration failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            // Find user by email
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

            if (userOptional.isEmpty()) {
                ApiResponse errorResponse = new ApiResponse();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("User not found with this email");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            User user = userOptional.get();

            // Check password (for now simple check, later use password encoder)
            if (!user.getPasswordHash().equals(loginRequest.getPassword())) {
                ApiResponse errorResponse = new ApiResponse();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Invalid password");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Login successful
            ApiResponse successResponse = new ApiResponse();
            successResponse.setSuccess(true);
            successResponse.setMessage("Login successful");
            successResponse.setData(user);

            return ResponseEntity.ok().body(successResponse);

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Login failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}