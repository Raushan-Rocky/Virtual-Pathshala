package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.converter.EnrollmentConverter;
import com.online_e_learning.virtualPathshala.exception.EnrollmentNotFoundException;
import com.online_e_learning.virtualPathshala.exception.UserNotFoundException;
import com.online_e_learning.virtualPathshala.exception.CourseNotFoundException;
import com.online_e_learning.virtualPathshala.model.Enrollment;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.repository.EnrollmentRepository;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.repository.CourseRepository;
import com.online_e_learning.virtualPathshala.requestDto.EnrollmentRequestDto;
import com.online_e_learning.virtualPathshala.responseDto.EnrollmentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentConverter enrollmentConverter;

    // CREATE
    public EnrollmentResponseDto createEnrollment(EnrollmentRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDto.getUserId()));

        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + requestDto.getCourseId()));

        // Check if enrollment already exists
        enrollmentRepository.findByUserIdAndCourseId(requestDto.getUserId(), requestDto.getCourseId())
                .ifPresent(enrollment -> {
                    throw new IllegalArgumentException("User is already enrolled in this course");
                });

        Enrollment enrollment = enrollmentConverter.convertToEntity(requestDto, user, course);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // ✅ Use the method with JOIN FETCH to get complete data
        Enrollment completeEnrollment = enrollmentRepository.findByIdWithDetails(savedEnrollment.getId())
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found after creation"));

        return enrollmentConverter.convertToResponseDto(completeEnrollment);
    }

    // READ ALL - Return Response DTOs with complete data
    public List<EnrollmentResponseDto> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentRepository.findAllWithDetails();
        return enrollments.stream()
                .map(enrollmentConverter::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // READ BY ID - Return Response DTO with complete data
    public EnrollmentResponseDto getEnrollmentById(int id) {
        Enrollment enrollment = enrollmentRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));
        return enrollmentConverter.convertToResponseDto(enrollment);
    }

    // READ BY USER ID - Return Response DTOs with complete data
    public List<EnrollmentResponseDto> getEnrollmentsByUserId(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        List<Enrollment> enrollments = enrollmentRepository.findByUserIdWithDetails(userId);
        return enrollments.stream()
                .map(enrollmentConverter::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // READ BY COURSE ID - Return Response DTOs with complete data
    public List<EnrollmentResponseDto> getEnrollmentsByCourseId(int courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }
        List<Enrollment> enrollments = enrollmentRepository.findByCourseIdWithDetails(courseId);
        return enrollments.stream()
                .map(enrollmentConverter::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // READ BY USER AND COURSE - Return Response DTO with complete data
    public EnrollmentResponseDto getEnrollmentByUserAndCourse(int userId, int courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseIdWithDetails(userId, courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found for user id: " + userId + " and course id: " + courseId));
        return enrollmentConverter.convertToResponseDto(enrollment);
    }

    // READ BY STATUS - Return Response DTOs with complete data
    public List<EnrollmentResponseDto> getEnrollmentsByStatus(String status) {
        List<Enrollment> enrollments = enrollmentRepository.findByStatusWithDetails(status);
        return enrollments.stream()
                .map(enrollmentConverter::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // UPDATE PROGRESS - Return Response DTO with complete data
    public EnrollmentResponseDto updateProgress(int id, String progress) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));

        enrollment.setProgress(progress);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        // ✅ Reload with complete data
        Enrollment completeEnrollment = enrollmentRepository.findByIdWithDetails(updatedEnrollment.getId())
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found after update"));

        return enrollmentConverter.convertToResponseDto(completeEnrollment);
    }

    // UPDATE STATUS - Return Response DTO with complete data
    public EnrollmentResponseDto updateStatus(int id, String status) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));

        enrollment.setStatus(status);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        // ✅ Reload with complete data
        Enrollment completeEnrollment = enrollmentRepository.findByIdWithDetails(updatedEnrollment.getId())
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found after update"));

        return enrollmentConverter.convertToResponseDto(completeEnrollment);
    }

    // UPDATE - Return Response DTO with complete data
    public EnrollmentResponseDto updateEnrollment(int id, EnrollmentRequestDto requestDto) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));

        enrollmentConverter.updateEntityFromDto(requestDto, enrollment);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        // ✅ Reload with complete data
        Enrollment completeEnrollment = enrollmentRepository.findByIdWithDetails(updatedEnrollment.getId())
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found after update"));

        return enrollmentConverter.convertToResponseDto(completeEnrollment);
    }

    // DELETE
    public void deleteEnrollment(int id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new EnrollmentNotFoundException("Enrollment not found with id: " + id);
        }
        enrollmentRepository.deleteById(id);
    }

    // Existing progress calculation methods...
    public void updateCourseProgress(int enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findByIdWithDetails(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + enrollmentId));

        Course course = enrollment.getCourse();
        int totalLessons = course.getLessonList() != null ? course.getLessonList().size() : 0;
        int completedLessons = calculateCompletedLessons(enrollment);

        if (totalLessons > 0) {
            int progress = (completedLessons * 100) / totalLessons;
            enrollment.setProgress(progress + "%");
            enrollmentRepository.save(enrollment);
        }
    }

    private int calculateCompletedLessons(Enrollment enrollment) {
        // Implement based on your business logic
        return 0;
    }
}