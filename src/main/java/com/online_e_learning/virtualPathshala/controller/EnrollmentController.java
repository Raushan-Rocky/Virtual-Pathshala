package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.requestDto.EnrollmentRequestDto;
import com.online_e_learning.virtualPathshala.responseDto.EnrollmentResponseDto;
import com.online_e_learning.virtualPathshala.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // CREATE - POST /api/enrollments
    @PostMapping
    public ResponseEntity<?> createEnrollment(@RequestBody EnrollmentRequestDto requestDto) {
        try {
            System.out.println("✅ Enrollment Controller: Creating enrollment for user: " + requestDto.getUserId());

            // ✅ Service returns Response DTO directly
            EnrollmentResponseDto enrollment = enrollmentService.createEnrollment(requestDto);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Enrollment created successfully");
            responseBody.put("data", enrollment);
            responseBody.put("enrollmentId", enrollment.getId());

            System.out.println("✅ Enrollment created successfully for user: " + requestDto.getUserId());
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);

        } catch (Exception e) {
            System.err.println("❌ Enrollment error: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // READ ALL - GET /api/enrollments
    @GetMapping
    public ResponseEntity<?> getAllEnrollments() {
        try {
            // ✅ Service returns Response DTOs directly
            List<EnrollmentResponseDto> enrollments = enrollmentService.getAllEnrollments();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", enrollments);
            response.put("count", enrollments.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ BY ID - GET /api/enrollments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable int id) {
        try {
            // ✅ Service returns Response DTO directly
            EnrollmentResponseDto enrollment = enrollmentService.getEnrollmentById(id);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("data", enrollment);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ BY USER ID - GET /api/enrollments/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getEnrollmentsByUserId(@PathVariable int userId) {
        try {
            System.out.println("✅ Fetching enrollments for user ID: " + userId);

            // ✅ Service returns Response DTOs directly
            List<EnrollmentResponseDto> enrollments = enrollmentService.getEnrollmentsByUserId(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", enrollments);
            response.put("count", enrollments.size());

            System.out.println("✅ Found " + enrollments.size() + " enrollments for user: " + userId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("❌ Error fetching enrollments: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ BY COURSE ID - GET /api/enrollments/course/{courseId}
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getEnrollmentsByCourseId(@PathVariable int courseId) {
        try {
            // ✅ Service returns Response DTOs directly
            List<EnrollmentResponseDto> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", enrollments);
            response.put("count", enrollments.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ BY USER AND COURSE - GET /api/enrollments/user/{userId}/course/{courseId}
    @GetMapping("/user/{userId}/course/{courseId}")
    public ResponseEntity<?> getEnrollmentByUserAndCourse(@PathVariable int userId, @PathVariable int courseId) {
        try {
            // ✅ Service returns Response DTO directly
            EnrollmentResponseDto enrollment = enrollmentService.getEnrollmentByUserAndCourse(userId, courseId);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("data", enrollment);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ BY STATUS - GET /api/enrollments/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getEnrollmentsByStatus(@PathVariable String status) {
        try {
            // ✅ Service returns Response DTOs directly
            List<EnrollmentResponseDto> enrollments = enrollmentService.getEnrollmentsByStatus(status);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", enrollments);
            response.put("count", enrollments.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE PROGRESS - PUT /api/enrollments/{id}/progress
    @PutMapping("/{id}/progress")
    public ResponseEntity<?> updateProgress(@PathVariable int id, @RequestBody Map<String, String> progressRequest) {
        try {
            String progress = progressRequest.get("progress");
            // ✅ Service returns Response DTO directly
            EnrollmentResponseDto enrollment = enrollmentService.updateProgress(id, progress);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Progress updated successfully");
            responseBody.put("data", enrollment);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // UPDATE STATUS - PUT /api/enrollments/{id}/status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable int id, @RequestBody Map<String, String> statusRequest) {
        try {
            String status = statusRequest.get("status");
            // ✅ Service returns Response DTO directly
            EnrollmentResponseDto enrollment = enrollmentService.updateStatus(id, status);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Status updated successfully");
            responseBody.put("data", enrollment);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // UPDATE - PUT /api/enrollments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnrollment(@PathVariable int id, @RequestBody EnrollmentRequestDto requestDto) {
        try {
            // ✅ Service returns Response DTO directly
            EnrollmentResponseDto enrollment = enrollmentService.updateEnrollment(id, requestDto);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Enrollment updated successfully");
            responseBody.put("data", enrollment);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE - DELETE /api/enrollments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable int id) {
        try {
            enrollmentService.deleteEnrollment(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Enrollment deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
}