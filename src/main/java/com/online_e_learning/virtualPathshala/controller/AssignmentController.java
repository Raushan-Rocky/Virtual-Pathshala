package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.converter.AssignmentConverter;
import com.online_e_learning.virtualPathshala.model.Assignment;
import com.online_e_learning.virtualPathshala.requestDto.AssignmentRequestDto;
import com.online_e_learning.virtualPathshala.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "*")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AssignmentConverter assignmentConverter;

    // CREATE - POST /api/assignments
    @PostMapping
    public ResponseEntity<?> createAssignment(@RequestBody AssignmentRequestDto requestDto) {
        try {
            Assignment assignment = assignmentService.createAssignment(requestDto);
            AssignmentRequestDto response = assignmentConverter.convertToResponseDto(assignment);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Assignment created successfully");
            responseBody.put("data", response);
            responseBody.put("assignmentId", assignment.getId());
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // READ ALL - GET /api/assignments
    @GetMapping
    public ResponseEntity<?> getAllAssignments() {
        try {
            List<Assignment> assignments = assignmentService.getAllAssignments();
            List<AssignmentRequestDto> assignmentDtos = assignments.stream()
                    .map(assignmentConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", assignmentDtos);
            response.put("count", assignmentDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ BY ID - GET /api/assignments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable int id) {
        try {
            Assignment assignment = assignmentService.getAssignmentById(id);
            AssignmentRequestDto response = assignmentConverter.convertToResponseDto(assignment);

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

    // READ BY USER ID - GET /api/assignments/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAssignmentsByUserId(@PathVariable int userId) {
        try {
            List<Assignment> assignments = assignmentService.getAssignmentsByUserId(userId);
            List<AssignmentRequestDto> assignmentDtos = assignments.stream()
                    .map(assignmentConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", assignmentDtos);
            response.put("count", assignmentDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ BY COURSE ID - GET /api/assignments/course/{courseId}
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getAssignmentsByCourseId(@PathVariable int courseId) {
        try {
            List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId);
            List<AssignmentRequestDto> assignmentDtos = assignments.stream()
                    .map(assignmentConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", assignmentDtos);
            response.put("count", assignmentDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ UPCOMING - GET /api/assignments/upcoming
    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingAssignments() {
        try {
            List<Assignment> assignments = assignmentService.getUpcomingAssignments();
            List<AssignmentRequestDto> assignmentDtos = assignments.stream()
                    .map(assignmentConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", assignmentDtos);
            response.put("count", assignmentDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ OVERDUE - GET /api/assignments/overdue
    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdueAssignments() {
        try {
            List<Assignment> assignments = assignmentService.getOverdueAssignments();
            List<AssignmentRequestDto> assignmentDtos = assignments.stream()
                    .map(assignmentConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", assignmentDtos);
            response.put("count", assignmentDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - PUT /api/assignments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable int id, @RequestBody AssignmentRequestDto requestDto) {
        try {
            Assignment assignment = assignmentService.updateAssignment(id, requestDto);
            AssignmentRequestDto response = assignmentConverter.convertToResponseDto(assignment);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Assignment updated successfully");
            responseBody.put("data", response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE - DELETE /api/assignments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable int id) {
        try {
            assignmentService.deleteAssignment(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Assignment deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
}