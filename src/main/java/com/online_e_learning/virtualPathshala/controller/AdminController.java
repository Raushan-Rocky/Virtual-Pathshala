package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.repository.CourseRepository;
import com.online_e_learning.virtualPathshala.responseDto.EnrollmentResponseDto;
import com.online_e_learning.virtualPathshala.service.DashboardService;
import com.online_e_learning.virtualPathshala.service.UserService;
import com.online_e_learning.virtualPathshala.service.CourseService;
import com.online_e_learning.virtualPathshala.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    // ✅ FIXED: Page route with token support
    @GetMapping("/dashboard")
    public String adminDashboard(@RequestParam(value = "token", required = false) String token) {
        // Token parameter is handled by JwtAuthenticationFilter
        return "AdminDashboard";
    }

    // ✅ FIXED: Also add direct /admin route
    @GetMapping("/")
    public String adminRoot(@RequestParam(value = "token", required = false) String token) {
        return "redirect:/api/admin/dashboard";
    }

    // Dashboard statistics API
    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getAdminDashboardStats() {
        try {
            Map<String, Object> stats = dashboardService.getAdminDashboardStats();
            return ResponseEntity.ok(Map.of("success", true, "data", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // User management APIs
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(Map.of("success", true, "data", userService.getAllUsers()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // Course management APIs
    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourses() {
        try {
            return ResponseEntity.ok(Map.of("success", true, "data", courseRepository.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // Enrollment management
    @GetMapping("/enrollments")
    public ResponseEntity<?> getAllEnrollments() {
        try {
            List<EnrollmentResponseDto> enrollments = enrollmentService.getAllEnrollments();
            return ResponseEntity.ok(Map.of("success", true, "data", enrollments, "count", enrollments.size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }
}