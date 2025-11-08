package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.converter.CourseConverter;
import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.CourseRepository;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.requestDto.CourseRequestDto;
import com.online_e_learning.virtualPathshala.responseDto.ApiResponse;
import com.online_e_learning.virtualPathshala.responseDto.CourseResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
//@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseConverter courseConverter;

    // CREATE COURSE - POST http://localhost:8040/api/courses
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseRequestDto courseRequestDto) {
        try {
            System.out.println("✅ Course Controller reached! Creating course: " + courseRequestDto.getTitle());

            // Check if teacher exists
            Optional<User> teacherOptional = userRepository.findById(courseRequestDto.getTeacherId());
            if (teacherOptional.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Teacher not found with ID: " + courseRequestDto.getTeacherId()));
            }

            // Check if course code already exists
            if (courseRequestDto.getCode() != null && courseRepository.existsByCode(courseRequestDto.getCode())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Course with code " + courseRequestDto.getCode() + " already exists"));
            }

            User teacher = teacherOptional.get();
            Course course = courseConverter.courseRequestDtoToCourse(courseRequestDto, teacher);
            Course savedCourse = courseRepository.save(course);
            CourseResponseDto responseDto = courseConverter.courseToCourseResponseDto(savedCourse);

            return ResponseEntity.ok(new ApiResponse<>(true, "Course created successfully", responseDto));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating course: " + e.getMessage()));
        }
    }

    // GET COURSES BY TEACHER - GET http://localhost:8040/api/courses/user/1
    @GetMapping("/user/{teacherId}")
    public ResponseEntity<?> getCoursesByTeacher(@PathVariable int teacherId) {
        try {
            System.out.println("✅ Fetching courses for teacher ID: " + teacherId);

            List<Course> courses = courseRepository.findByUserId(teacherId);
            List<CourseResponseDto> responseDtos = courses.stream()
                    .map(courseConverter::courseToCourseResponseDto)
                    .toList();

            return ResponseEntity.ok(new ApiResponse<>(true, "Courses retrieved successfully", responseDtos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving courses: " + e.getMessage()));
        }
    }

    // TEST ENDPOINT - DIFFERENT URL PE RAKHO
    @GetMapping("/test")
    public String testEndpoint() {
        return "✅ Course Controller is working!";
    }
}