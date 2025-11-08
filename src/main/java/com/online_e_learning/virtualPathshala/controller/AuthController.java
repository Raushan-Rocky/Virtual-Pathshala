package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.requestDto.SignupRequest;
import com.online_e_learning.virtualPathshala.requestDto.LoginRequest;
import com.online_e_learning.virtualPathshala.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            System.out.println("🔍 Received signup request:");
            System.out.println("Name: " + signupRequest.getName());
            System.out.println("Email: " + signupRequest.getEmail());
            System.out.println("Mobile: " + signupRequest.getMobile());
            System.out.println("Role: " + signupRequest.getRole());
            System.out.println("Password length: " + (signupRequest.getPassword() != null ? signupRequest.getPassword().length() : "null"));
            System.out.println("Confirm Password length: " + (signupRequest.getConfirmPassword() != null ? signupRequest.getConfirmPassword().length() : "null"));

            // Add validation
            if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
                System.out.println("❌ Passwords do not match");
                return ResponseEntity.badRequest().body("Passwords do not match");
            }

            // Call service method
            return authService.signup(signupRequest);

        } catch (Exception e) {
            System.out.println("❌ Signup controller error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Signup failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return authService.login(loginRequest);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Login failed: " + e.getMessage());
        }
    }
}