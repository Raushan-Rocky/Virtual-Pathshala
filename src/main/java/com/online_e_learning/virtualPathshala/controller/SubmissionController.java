package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.converter.SubmissionConverter;
import com.online_e_learning.virtualPathshala.model.Submission;
import com.online_e_learning.virtualPathshala.requestDto.SubmissionRequestDto;
import com.online_e_learning.virtualPathshala.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "*")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private SubmissionConverter submissionConverter;

    // CREATE - POST /api/submissions
    @PostMapping
    public ResponseEntity<?> createSubmission(@RequestBody SubmissionRequestDto requestDto) {
        try {
            Submission submission = submissionService.createSubmission(requestDto);
            SubmissionRequestDto response = submissionConverter.convertToResponseDto(submission);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Submission created successfully");
            responseBody.put("data", response);
            responseBody.put("submissionId", submission.getId());
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // READ ALL - GET /api/submissions
    @GetMapping
    public ResponseEntity<?> getAllSubmissions() {
        try {
            List<Submission> submissions = submissionService.getAllSubmissions();
            List<SubmissionRequestDto> submissionDtos = submissions.stream()
                    .map(submissionConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", submissionDtos);
            response.put("count", submissionDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ BY ID - GET /api/submissions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getSubmissionById(@PathVariable int id) {
        try {
            Submission submission = submissionService.getSubmissionById(id);
            SubmissionRequestDto response = submissionConverter.convertToResponseDto(submission);

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

    // READ BY USER ID - GET /api/submissions/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getSubmissionsByUserId(@PathVariable int userId) {
        try {
            List<Submission> submissions = submissionService.getSubmissionsByUserId(userId);
            List<SubmissionRequestDto> submissionDtos = submissions.stream()
                    .map(submissionConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", submissionDtos);
            response.put("count", submissionDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ BY ASSIGNMENT ID - GET /api/submissions/assignment/{assignmentId}
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<?> getSubmissionsByAssignmentId(@PathVariable int assignmentId) {
        try {
            List<Submission> submissions = submissionService.getSubmissionsByAssignmentId(assignmentId);
            List<SubmissionRequestDto> submissionDtos = submissions.stream()
                    .map(submissionConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", submissionDtos);
            response.put("count", submissionDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ BY USER AND ASSIGNMENT - GET /api/submissions/user/{userId}/assignment/{assignmentId}
    @GetMapping("/user/{userId}/assignment/{assignmentId}")
    public ResponseEntity<?> getSubmissionByUserAndAssignment(@PathVariable int userId, @PathVariable int assignmentId) {
        try {
            Submission submission = submissionService.getSubmissionByUserAndAssignment(userId, assignmentId);
            SubmissionRequestDto response = submissionConverter.convertToResponseDto(submission);

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

    // READ UNGRADED - GET /api/submissions/ungraded
    @GetMapping("/ungraded")
    public ResponseEntity<?> getUngradedSubmissions() {
        try {
            List<Submission> submissions = submissionService.getUngradedSubmissions();
            List<SubmissionRequestDto> submissionDtos = submissions.stream()
                    .map(submissionConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", submissionDtos);
            response.put("count", submissionDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ GRADED - GET /api/submissions/graded
    @GetMapping("/graded")
    public ResponseEntity<?> getGradedSubmissions() {
        try {
            List<Submission> submissions = submissionService.getGradedSubmissions();
            List<SubmissionRequestDto> submissionDtos = submissions.stream()
                    .map(submissionConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", submissionDtos);
            response.put("count", submissionDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - PUT /api/submissions/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubmission(@PathVariable int id, @RequestBody SubmissionRequestDto requestDto) {
        try {
            Submission submission = submissionService.updateSubmission(id, requestDto);
            SubmissionRequestDto response = submissionConverter.convertToResponseDto(submission);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Submission updated successfully");
            responseBody.put("data", response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // GRADE SUBMISSION - PUT /api/submissions/{id}/grade
    @PutMapping("/{id}/grade")
    public ResponseEntity<?> gradeSubmission(@PathVariable int id, @RequestBody Map<String, Object> gradeRequest) {
        try {
            Float grade = ((Number) gradeRequest.get("grade")).floatValue();
            String feedback = (String) gradeRequest.get("feedback");

            Submission submission = submissionService.gradeSubmission(id, grade, feedback);
            SubmissionRequestDto response = submissionConverter.convertToResponseDto(submission);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Submission graded successfully");
            responseBody.put("data", response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE - DELETE /api/submissions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubmission(@PathVariable int id) {
        try {
            submissionService.deleteSubmission(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Submission deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
}