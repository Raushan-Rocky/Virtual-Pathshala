package com.online_e_learning.virtualPathshala.controller;
import com.online_e_learning.virtualPathshala.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Admin Dashboard
    @GetMapping("/admin")
    public ResponseEntity<?> getAdminDashboard() {
        Map<String, Object> stats = dashboardService.getAdminDashboardStats();
        return ResponseEntity.ok(stats);
    }

    // Teacher Dashboard
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getTeacherDashboard(@PathVariable int teacherId) {
        Map<String, Object> stats = dashboardService.getTeacherDashboardStats(teacherId);
        return ResponseEntity.ok(stats);
    }

    // Student Dashboard
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentDashboard(@PathVariable int studentId) {
        Map<String, Object> stats = dashboardService.getStudentDashboardStats(studentId);
        return ResponseEntity.ok(stats);
    }
}