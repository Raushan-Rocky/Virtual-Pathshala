package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.model.Assignment;
import com.online_e_learning.virtualPathshala.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // ✅ GET assignments by teacher ID
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getAssignmentsByTeacher(@PathVariable int teacherId) {
        try {
            System.out.println("📝 Fetching assignments for teacher ID: " + teacherId);

            List<Assignment> assignments = assignmentService.getAssignmentsByUserId(teacherId);

            // Convert to response format
            List<Map<String, Object>> assignmentData = assignments.stream().map(assignment -> {
                Map<String, Object> data = new HashMap<>();
                data.put("id", assignment.getId());
                data.put("title", assignment.getTitle());
                data.put("description", assignment.getDescription());
                data.put("dueDate", assignment.getDueDate());
                data.put("submittedCount", 0); // You can calculate this later
                data.put("totalStudents", 0); // You can calculate this later

                if (assignment.getCourse() != null) {
                    Map<String, Object> courseData = new HashMap<>();
                    courseData.put("id", assignment.getCourse().getId());
                    courseData.put("title", assignment.getCourse().getTitle());
                    courseData.put("code", assignment.getCourse().getCode());
                    data.put("course", courseData);
                }

                return data;
            }).toList();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", assignmentData);
            response.put("count", assignmentData.size());

            System.out.println("✅ Found " + assignmentData.size() + " assignments for teacher " + teacherId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("❌ Error fetching assignments for teacher: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    // ✅ GET assignments by course ID
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    public ResponseEntity<?> getAssignmentsByCourse(@PathVariable int courseId) {
        try {
            System.out.println("📝 Fetching assignments for course ID: " + courseId);

            List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId);

            List<Map<String, Object>> assignmentData = assignments.stream().map(assignment -> {
                Map<String, Object> data = new HashMap<>();
                data.put("id", assignment.getId());
                data.put("title", assignment.getTitle());
                data.put("description", assignment.getDescription());
                data.put("dueDate", assignment.getDueDate());
                data.put("submittedCount", 0);
                data.put("totalStudents", 0);

                if (assignment.getCourse() != null) {
                    Map<String, Object> courseData = new HashMap<>();
                    courseData.put("id", assignment.getCourse().getId());
                    courseData.put("title", assignment.getCourse().getTitle());
                    data.put("course", courseData);
                }

                return data;
            }).toList();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", assignmentData);
            response.put("count", assignmentData.size());

            System.out.println("✅ Found " + assignmentData.size() + " assignments for course " + courseId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("❌ Error fetching assignments for course: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    // ✅ GET all assignments (ADMIN only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllAssignments() {
        try {
            List<Assignment> assignments = assignmentService.getAllAssignments();

            List<Map<String, Object>> assignmentData = assignments.stream().map(assignment -> {
                Map<String, Object> data = new HashMap<>();
                data.put("id", assignment.getId());
                data.put("title", assignment.getTitle());
                data.put("description", assignment.getDescription());
                data.put("dueDate", assignment.getDueDate());

                if (assignment.getUser() != null) {
                    data.put("teacherName", assignment.getUser().getName());
                }
                if (assignment.getCourse() != null) {
                    data.put("courseName", assignment.getCourse().getTitle());
                }

                return data;
            }).toList();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", assignmentData);
            response.put("count", assignmentData.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ✅ GET assignment by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    public ResponseEntity<?> getAssignmentById(@PathVariable int id) {
        try {
            Assignment assignment = assignmentService.getAssignmentById(id);

            Map<String, Object> assignmentData = new HashMap<>();
            assignmentData.put("id", assignment.getId());
            assignmentData.put("title", assignment.getTitle());
            assignmentData.put("description", assignment.getDescription());
            assignmentData.put("dueDate", assignment.getDueDate());

            if (assignment.getUser() != null) {
                assignmentData.put("teacherName", assignment.getUser().getName());
            }
            if (assignment.getCourse() != null) {
                assignmentData.put("courseName", assignment.getCourse().getTitle());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", assignmentData);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    // ✅ Test endpoint
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Assignment Controller is working!");
        return ResponseEntity.ok(response);
    }
}