package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.requestDto.SignupRequest;
import com.online_e_learning.virtualPathshala.requestDto.LoginRequest;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

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
            // 🔹 Role uppercase me convert (Teacher/Student/Admin)
            String selectedRole = loginRequest.getRole().toUpperCase();

            // 🔹 Get user by email
            Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(401).body("Invalid Email");
            }

            User user = optionalUser.get();

            // ✅ ROLE CHECK: Selected role must match user's real role
            if (!user.getRole().name().equals(selectedRole)) {
                return ResponseEntity.status(403).body("Role mismatch! Please select correct user role.");
            }

            // ✅ Password check from AuthService as earlier
            return authService.login(loginRequest);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Login failed: " + e.getMessage());
        }
    }


    // ✅ SIRF YEH EK METHOD ADD KARO - Teacher dashboard ke liye
    @GetMapping("/teacher/profile")
    public ResponseEntity<?> getTeacherProfile(@RequestParam String email) {
        try {
            Optional<User> teacher = userRepository.findByEmail(email);

            if (teacher.isPresent() && "TEACHER".equals(teacher.get().getRole())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);

                // Simple response - sirf necessary fields
                Map<String, Object> teacherData = new HashMap<>();
                teacherData.put("id", teacher.get().getId());
                teacherData.put("name", teacher.get().getName());
                teacherData.put("email", teacher.get().getEmail());
                teacherData.put("role", teacher.get().getRole());

                response.put("data", teacherData);
                return ResponseEntity.ok(response);
            }

            return ResponseEntity.status(404).body("Teacher not found");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}