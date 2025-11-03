package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.converter.EnrollmentConverter;
import com.online_e_learning.virtualPathshala.model.Enrollment;
import com.online_e_learning.virtualPathshala.requestDto.EnrollmentRequestDto;
import com.online_e_learning.virtualPathshala.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentConverter enrollmentConverter;

    // CREATE - POST /api/enrollments
    @PostMapping
    public ResponseEntity<?> createEnrollment(@RequestBody EnrollmentRequestDto requestDto) {
        try {
            Enrollment enrollment = enrollmentService.createEnrollment(requestDto);
            EnrollmentRequestDto response = enrollmentConverter.convertToResponseDto(enrollment);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Enrollment created successfully");
            responseBody.put("data", response);
            responseBody.put("enrollmentId", enrollment.getId());
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        } catch (Exception e) {
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
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            List<EnrollmentRequestDto> enrollmentDtos = enrollments.stream()
                    .map(enrollmentConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", enrollmentDtos);
            response.put("count", enrollmentDtos.size());
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
            Enrollment enrollment = enrollmentService.getEnrollmentById(id);
            EnrollmentRequestDto response = enrollmentConverter.convertToResponseDto(enrollment);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("data", response);
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
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUserId(userId);
            List<EnrollmentRequestDto> enrollmentDtos = enrollments.stream()
                    .map(enrollmentConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", enrollmentDtos);
            response.put("count", enrollmentDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
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
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
            List<EnrollmentRequestDto> enrollmentDtos = enrollments.stream()
                    .map(enrollmentConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", enrollmentDtos);
            response.put("count", enrollmentDtos.size());
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
            Enrollment enrollment = enrollmentService.getEnrollmentByUserAndCourse(userId, courseId);
            EnrollmentRequestDto response = enrollmentConverter.convertToResponseDto(enrollment);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("data", response);
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
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStatus(status);
            List<EnrollmentRequestDto> enrollmentDtos = enrollments.stream()
                    .map(enrollmentConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", enrollmentDtos);
            response.put("count", enrollmentDtos.size());
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
            Enrollment enrollment = enrollmentService.updateProgress(id, progress);
            EnrollmentRequestDto response = enrollmentConverter.convertToResponseDto(enrollment);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Progress updated successfully");
            responseBody.put("data", response);
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
            Enrollment enrollment = enrollmentService.updateStatus(id, status);
            EnrollmentRequestDto response = enrollmentConverter.convertToResponseDto(enrollment);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Status updated successfully");
            responseBody.put("data", response);
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
            Enrollment enrollment = enrollmentService.updateEnrollment(id, requestDto);
            EnrollmentRequestDto response = enrollmentConverter.convertToResponseDto(enrollment);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Enrollment updated successfully");
            responseBody.put("data", response);
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