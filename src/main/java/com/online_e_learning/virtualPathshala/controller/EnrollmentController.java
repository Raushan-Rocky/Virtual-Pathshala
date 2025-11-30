package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.CourseRepository;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.requestDto.EnrollmentRequestDto;
import com.online_e_learning.virtualPathshala.responseDto.EnrollmentResponseDto;
import com.online_e_learning.virtualPathshala.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ GET enrollments by course ID - PROPER SECURITY FIX
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getEnrollmentsByCourseId(@PathVariable int courseId) {
        try {
            System.out.println("✅ Fetching enrollments for course ID: " + courseId);

            // ✅ SECURITY: Get current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            Optional<User> currentUserOptional = userRepository.findByEmail(currentUsername);
            if (currentUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("User not found"));
            }

            User currentUser = currentUserOptional.get();
            System.out.println("🔐 Current user: " + currentUser.getId() + " - " + currentUser.getName() + " - " + currentUser.getRole());

            // ✅ Check if course exists
            Optional<Course> courseOptional = courseRepository.findById(courseId);
            if (courseOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Course not found with ID: " + courseId));
            }

            Course course = courseOptional.get();
            System.out.println("📚 Course found: " + course.getId() + " - " + course.getName());
            System.out.println("👨‍🏫 Course teacher ID: " + (course.getTeacher() != null ? course.getTeacher().getId() : "NULL"));

            // ✅ SECURITY: Only course teacher or admin can access enrollments
            boolean isCourseTeacher = course.getTeacher() != null && course.getTeacher().getId() == currentUser.getId();
            boolean isAdmin = currentUser.getRole().name().equals("ADMIN");

            System.out.println("🔍 Security check - Is Admin: " + isAdmin + ", Is Course Teacher: " + isCourseTeacher);

            if (!isAdmin && !isCourseTeacher) {
                System.out.println("❌ ACCESS DENIED: User " + currentUser.getId() + " is not teacher of course " + courseId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(createErrorResponse("Access denied: You can only view enrollments for your own courses. Course belongs to teacher ID: " +
                                (course.getTeacher() != null ? course.getTeacher().getId() : "none")));
            }

            // ✅ Get enrollments
            List<EnrollmentResponseDto> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", enrollments);
            response.put("count", enrollments.size());
            response.put("courseName", course.getName());

            System.out.println("✅ Found " + enrollments.size() + " enrollments for course: " + courseId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("❌ Error fetching enrollments: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching enrollments: " + e.getMessage()));
        }
    }

    // ✅ GET enrollments by teacher ID (All courses of a teacher)
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getEnrollmentsByTeacher(@PathVariable int teacherId) {
        try {
            System.out.println("✅ Fetching all enrollments for teacher ID: " + teacherId);

            // ✅ SECURITY: Get current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            Optional<User> currentUserOptional = userRepository.findByEmail(currentUsername);
            if (currentUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("User not found"));
            }

            User currentUser = currentUserOptional.get();

            // ✅ SECURITY: Teachers can only access their own data unless admin
            if (!currentUser.getRole().name().equals("ADMIN") && currentUser.getId() != teacherId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(createErrorResponse("Access denied: You can only view your own enrollments"));
            }

            // ✅ Get teacher's courses
            List<Course> teacherCourses = courseRepository.findByTeacherId(teacherId);
            System.out.println("📚 Found " + teacherCourses.size() + " courses for teacher " + teacherId);

            // ✅ Get enrollments for all courses
            List<EnrollmentResponseDto> allEnrollments = teacherCourses.stream()
                    .flatMap(course -> enrollmentService.getEnrollmentsByCourseId(course.getId()).stream())
                    .toList();

            // ✅ Count unique students
            long uniqueStudents = allEnrollments.stream()
                    .map(EnrollmentResponseDto::getUserId)
                    .distinct()
                    .count();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", allEnrollments);
            response.put("totalEnrollments", allEnrollments.size());
            response.put("totalStudents", uniqueStudents);
            response.put("totalCourses", teacherCourses.size());

            System.out.println("✅ Found " + allEnrollments.size() + " enrollments across " + teacherCourses.size() + " courses for teacher " + teacherId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("❌ Error fetching teacher enrollments: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching enrollments: " + e.getMessage()));
        }
    }

    // ✅ GET enrollments by user ID
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    public ResponseEntity<?> getEnrollmentsByUserId(@PathVariable int userId) {
        try {
            System.out.println("✅ Fetching enrollments for user ID: " + userId);

            // ✅ SECURITY: Users can only access their own enrollments unless admin/teacher
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            Optional<User> currentUserOptional = userRepository.findByEmail(currentUsername);
            if (currentUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("User not found"));
            }

            User currentUser = currentUserOptional.get();

            if (!currentUser.getRole().name().equals("ADMIN") &&
                    !currentUser.getRole().name().equals("TEACHER") &&
                    currentUser.getId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(createErrorResponse("Access denied: You can only view your own enrollments"));
            }

            List<EnrollmentResponseDto> enrollments = enrollmentService.getEnrollmentsByUserId(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", enrollments);
            response.put("count", enrollments.size());

            System.out.println("✅ Found " + enrollments.size() + " enrollments for user: " + userId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("❌ Error fetching user enrollments: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching enrollments: " + e.getMessage()));
        }
    }

    // ✅ CREATE enrollment
    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<?> createEnrollment(@RequestBody EnrollmentRequestDto requestDto) {
        try {
            System.out.println("✅ Enrollment Controller: Creating enrollment for user: " + requestDto.getUserId());

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

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Error creating enrollment: " + e.getMessage()));
        }
    }

    // ✅ GET all enrollments (Admin only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllEnrollments() {
        try {
            List<EnrollmentResponseDto> enrollments = enrollmentService.getAllEnrollments();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", enrollments);
            response.put("count", enrollments.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching enrollments: " + e.getMessage()));
        }
    }

    // ✅ Helper method for error responses
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", message);
        return errorResponse;
    }

    // ✅ Test endpoint
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Enrollment Controller is working!");
        return ResponseEntity.ok(response);
    }
}