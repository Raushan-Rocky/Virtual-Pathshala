package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.converter.CourseConverter;
import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.CourseRepository;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.requestDto.CourseRequestDto;
import com.online_e_learning.virtualPathshala.responseDto.ApiResponse;
import com.online_e_learning.virtualPathshala.responseDto.CourseResponseDto;
import com.online_e_learning.virtualPathshala.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseConverter courseConverter;

    @Autowired
    private CourseService courseService;

    // ✅ CREATE COURSE - POST http://localhost:8040/api/courses
    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> createCourse(@RequestBody CourseRequestDto courseRequestDto,
                                          HttpServletRequest request) {
        try {
            System.out.println("✅ Course Controller reached! Creating course: " + courseRequestDto.getName());

            // ✅ SECURITY: Get logged-in teacher ID from security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            // Get current user from database
            Optional<User> currentUserOptional = userRepository.findByEmail(currentUsername);
            if (currentUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not found"));
            }

            User currentUser = currentUserOptional.get();

            // ✅ VERIFY: Only teachers can create courses (unless admin)
            if (!currentUser.getRole().name().equals("TEACHER") &&
                    !currentUser.getRole().name().equals("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Only teachers and admins can create courses"));
            }

            // ✅ SECURITY: Ensure teacher can only create courses for themselves
            // If user is TEACHER, override the teacherId with their own ID
            if (currentUser.getRole().name().equals("TEACHER")) {
                courseRequestDto.setTeacherId(currentUser.getId());
                System.out.println("✅ Overriding teacherId to current user ID: " + currentUser.getId());
            }

            // ✅ Use CourseService instead of direct repository calls
            ApiResponse<CourseResponseDto> serviceResponse = courseService.createCourse(courseRequestDto);

            if (serviceResponse.isSuccess()) {
                System.out.println("✅ Course created successfully via Service!");
                return ResponseEntity.ok(serviceResponse);
            } else {
                return ResponseEntity.badRequest().body(serviceResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating course: " + e.getMessage()));
        }
    }

    // ✅ GET COURSES BY TEACHER - GET http://localhost:8040/api/courses/user/{teacherId}
    @GetMapping("/user/{teacherId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    public ResponseEntity<?> getCoursesByTeacher(@PathVariable int teacherId) {
        try {
            System.out.println("✅ Fetching courses for teacher ID: " + teacherId);

            // ✅ SECURITY: Check if user has access to these courses
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            Optional<User> currentUserOptional = userRepository.findByEmail(currentUsername);
            if (currentUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not found"));
            }

            User currentUser = currentUserOptional.get();

            // ✅ SECURITY: Users can only access their own courses unless admin
            if (currentUser.getRole().name().equals("TEACHER") && currentUser.getId() != teacherId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Access denied: You can only view your own courses"));
            }

            // ✅ Use Service method
            ApiResponse<List<CourseResponseDto>> serviceResponse = courseService.getCoursesByTeacher(teacherId);

            if (serviceResponse.isSuccess()) {
                return ResponseEntity.ok(serviceResponse);
            } else {
                return ResponseEntity.badRequest().body(serviceResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving courses: " + e.getMessage()));
        }
    }

    // ✅ GET ALL COURSES - GET http://localhost:8040/api/courses/all
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    public ResponseEntity<?> getAllCourses() {
        try {
            System.out.println("✅ Fetching all courses for student view");

            ApiResponse<List<CourseResponseDto>> serviceResponse = courseService.getAllCourses();

            if (serviceResponse.isSuccess()) {
                return ResponseEntity.ok(serviceResponse);
            } else {
                return ResponseEntity.badRequest().body(serviceResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving courses: " + e.getMessage()));
        }
    }

    // ✅ TEST ENDPOINT - GET http://localhost:8040/api/courses/test
    @GetMapping("/test")
    public String testEndpoint() {
        return "✅ Course Controller is working!";
    }
}