package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.model.Assignment;
import com.online_e_learning.virtualPathshala.requestDto.AssignmentRequestDto;
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

    // ✅ CREATE NEW ASSIGNMENT - POST http://localhost:8040/api/assignments
    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> createAssignment(@RequestBody AssignmentRequestDto assignmentRequestDto) {
        try {
            System.out.println("📝 Creating new assignment: " + assignmentRequestDto.getTitle());
            System.out.println("👨‍🏫 Teacher ID: " + assignmentRequestDto.getUserId());
            System.out.println("📚 Course ID: " + assignmentRequestDto.getCourseId());

            Assignment assignment = assignmentService.createAssignment(assignmentRequestDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Assignment created successfully");
            response.put("data", assignment);
            response.put("assignmentId", assignment.getId());

            System.out.println("✅ Assignment created successfully with ID: " + assignment.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.out.println("❌ Error creating assignment: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

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
                data.put("type", assignment.getType());
                data.put("submittedCount", 0); // You can calculate this later
                data.put("totalStudents", 0); // You can calculate this later

                if (assignment.getCourse() != null) {
                    Map<String, Object> courseData = new HashMap<>();
                    courseData.put("id", assignment.getCourse().getId());
                    courseData.put("name", assignment.getCourse().getName()); // Changed from getTitle() to getName()
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
                data.put("type", assignment.getType());
                data.put("submittedCount", 0);
                data.put("totalStudents", 0);

                if (assignment.getCourse() != null) {
                    Map<String, Object> courseData = new HashMap<>();
                    courseData.put("id", assignment.getCourse().getId());
                    courseData.put("name", assignment.getCourse().getName()); // Changed from getTitle() to getName()
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
                data.put("type", assignment.getType());

                if (assignment.getUser() != null) {
                    data.put("teacherName", assignment.getUser().getName());
                }
                if (assignment.getCourse() != null) {
                    data.put("courseName", assignment.getCourse().getName()); // Changed from getTitle() to getName()
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
            assignmentData.put("type", assignment.getType());

            if (assignment.getUser() != null) {
                assignmentData.put("teacherName", assignment.getUser().getName());
            }
            if (assignment.getCourse() != null) {
                assignmentData.put("courseName", assignment.getCourse().getName()); // Changed from getTitle() to getName()
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

    // ✅ UPDATE assignment
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> updateAssignment(@PathVariable int id, @RequestBody AssignmentRequestDto assignmentRequestDto) {
        try {
            System.out.println("📝 Updating assignment ID: " + id);

            Assignment assignment = assignmentService.updateAssignment(id, assignmentRequestDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Assignment updated successfully");
            response.put("data", assignment);

            System.out.println("✅ Assignment updated successfully with ID: " + assignment.getId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Error updating assignment: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // ✅ DELETE assignment
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> deleteAssignment(@PathVariable int id) {
        try {
            System.out.println("🗑️ Deleting assignment ID: " + id);

            assignmentService.deleteAssignment(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Assignment deleted successfully");

            System.out.println("✅ Assignment deleted successfully with ID: " + id);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Error deleting assignment: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
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