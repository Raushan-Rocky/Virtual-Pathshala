package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.CourseRepository;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.responseDto.EnrollmentResponseDto;
import com.online_e_learning.virtualPathshala.service.DashboardService;
import com.online_e_learning.virtualPathshala.service.UserService;
import com.online_e_learning.virtualPathshala.service.CourseService;
import com.online_e_learning.virtualPathshala.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    // ✅ Page route with token support
    @GetMapping("/dashboard")
    public String adminDashboard(@RequestParam(value = "token", required = false) String token) {
        return "AdminDashboard";
    }

    // ✅ Direct /admin route
    @GetMapping("/")
    public String adminRoot(@RequestParam(value = "token", required = false) String token) {
        return "redirect:/api/admin/dashboard?token=" + (token != null ? token : "");
    }

    // ✅ Dashboard statistics API
    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminDashboardStats() {
        try {
            Map<String, Object> stats = dashboardService.getAdminDashboardStats();
            return ResponseEntity.ok(Map.of("success", true, "data", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // ✅ User management APIs - GET ALL USERS
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(Map.of("success", true, "data", users, "count", users.size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // ✅ UPDATE USER ROLE - CRITICAL FOR USER MANAGEMENT
    @PostMapping("/users/role")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> updateUserRole(@RequestBody Map<String, Object> request) {
        try {
            int userId = Integer.parseInt(request.get("userId").toString());
            String roleStr = request.get("role").toString();

            // Validate role
            Role role;
            try {
                role = Role.valueOf(roleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "error", "Invalid role. Use ADMIN, TEACHER, or STUDENT"
                ));
            }

            userService.updateUserRole(userId, role);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "User role updated successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    // ✅ DELETE USER
    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable int userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "User deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    // ✅ GET USER BY ID
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable int userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(Map.of("success", true, "data", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // ✅ GET USERS BY ROLE
    @GetMapping("/users/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role) {
        try {
            Role userRole = Role.valueOf(role.toUpperCase());
            List<User> users = userRepository.findByRole(userRole);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", users,
                    "count", users.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    // ✅ Course management APIs
    @GetMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCourses() {
        try {
            return ResponseEntity.ok(Map.of("success", true, "data", courseRepository.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // ✅ Enrollment management
    @GetMapping("/enrollments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllEnrollments() {
        try {
            List<EnrollmentResponseDto> enrollments = enrollmentService.getAllEnrollments();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", enrollments,
                    "count", enrollments.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    // ✅ CREATE NEW USER (ADMIN ONLY)
    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> userData) {
        try {
            // This would create a new user - you can implement this based on your UserService
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "User created successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    // ✅ AdminController.java me ye endpoints add karo
    @PostMapping("/users/teachers/{teacherId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveTeacher(@PathVariable int teacherId) {
        try {
            userService.approveTeacher(teacherId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Teacher approved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/users/teachers/{teacherId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectTeacher(@PathVariable int teacherId) {
        try {
            userService.rejectTeacher(teacherId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Teacher rejected successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/users/teachers/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPendingTeachers() {
        try {
            List<User> pendingTeachers = userService.getPendingTeachers();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", pendingTeachers,
                    "count", pendingTeachers.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }
}
