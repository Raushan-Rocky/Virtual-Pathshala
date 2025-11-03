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
            // Add validation
            if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Passwords do not match");
            }

            // Call service method
            return authService.signup(signupRequest);

        } catch (Exception e) {
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