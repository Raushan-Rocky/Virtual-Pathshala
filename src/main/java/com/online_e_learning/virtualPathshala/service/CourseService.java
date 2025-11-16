package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.CourseRepository;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.requestDto.CourseRequestDto;
import com.online_e_learning.virtualPathshala.responseDto.ApiResponse;
import com.online_e_learning.virtualPathshala.responseDto.CourseResponseDto;
import com.online_e_learning.virtualPathshala.converter.CourseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseConverter courseConverter;

    public ApiResponse<CourseResponseDto> createCourse(CourseRequestDto courseRequestDto) {
        try {
            // Check if teacher exists
            Optional<User> teacherOptional = userRepository.findById(courseRequestDto.getTeacherId());
            if (teacherOptional.isEmpty()) {
                return ApiResponse.error("Teacher not found with ID: " + courseRequestDto.getTeacherId());
            }

            // Check if course code already exists
            if (courseRepository.existsByCode(courseRequestDto.getCode())) {
                return ApiResponse.error("Course with code " + courseRequestDto.getCode() + " already exists");
            }

            User teacher = teacherOptional.get();
            Course course = courseConverter.courseRequestDtoToCourse(courseRequestDto, teacher);
            Course savedCourse = courseRepository.save(course);
            CourseResponseDto responseDto = courseConverter.courseToCourseResponseDto(savedCourse);

            return ApiResponse.success("Course created successfully", responseDto);

        } catch (Exception e) {
            return ApiResponse.error("Error creating course: " + e.getMessage());
        }
    }

    public ApiResponse<List<CourseResponseDto>> getCoursesByTeacher(int teacherId) {
        try {
            List<Course> courses = courseRepository.findByUserId(teacherId);
            List<CourseResponseDto> responseDtos = courses.stream()
                    .map(courseConverter::courseToCourseResponseDto)
                    .collect(Collectors.toList());

            return ApiResponse.success("Courses retrieved successfully", responseDtos);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving courses: " + e.getMessage());
        }
    }

    // Add other methods as needed...
    // CourseService.java - ye method add karein
    public ApiResponse<List<CourseResponseDto>> getAllCourses() {
        try {
            List<Course> courses = courseRepository.findAll();
            List<CourseResponseDto> responseDtos = courses.stream()
                    .map(courseConverter::courseToCourseResponseDto)
                    .collect(Collectors.toList());

            return ApiResponse.success("All courses retrieved successfully", responseDtos);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving all courses: " + e.getMessage());
        }
    }
}