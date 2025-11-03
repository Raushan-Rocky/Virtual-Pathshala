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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Enrollment createEnrollment(EnrollmentRequestDto requestDto) {
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
        return enrollmentRepository.save(enrollment);
    }

    // READ ALL
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    // READ BY ID
    public Enrollment getEnrollmentById(int id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));
    }

    // READ BY USER ID
    public List<Enrollment> getEnrollmentsByUserId(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return enrollmentRepository.findByUserId(userId);
    }

    // READ BY COURSE ID
    public List<Enrollment> getEnrollmentsByCourseId(int courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }
        return enrollmentRepository.findByCourseId(courseId);
    }

    // READ BY USER AND COURSE
    public Enrollment getEnrollmentByUserAndCourse(int userId, int courseId) {
        return enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found for user id: " + userId + " and course id: " + courseId));
    }

    // READ BY STATUS
    public List<Enrollment> getEnrollmentsByStatus(String status) {
        return enrollmentRepository.findByStatus(status);
    }

    // UPDATE PROGRESS
    public Enrollment updateProgress(int id, String progress) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));

        enrollment.setProgress(progress);
        return enrollmentRepository.save(enrollment);
    }

    // UPDATE STATUS
    public Enrollment updateStatus(int id, String status) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));

        enrollment.setStatus(status);
        return enrollmentRepository.save(enrollment);
    }

    // UPDATE
    public Enrollment updateEnrollment(int id, EnrollmentRequestDto requestDto) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));

        enrollmentConverter.updateEntityFromDto(requestDto, enrollment);
        return enrollmentRepository.save(enrollment);
    }

    // DELETE
    public void deleteEnrollment(int id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new EnrollmentNotFoundException("Enrollment not found with id: " + id);
        }
        enrollmentRepository.deleteById(id);
    }

    public Enrollment updateProgressAutomatically(int enrollmentId) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        Course course = enrollment.getCourse();

        // Calculate progress based on completed lessons/assignments
        int totalLessons = course.getLessonList().size();
        int completedLessons = calculateCompletedLessons(enrollment);

        if (totalLessons > 0) {
            int progress = (completedLessons * 100) / totalLessons;
            enrollment.setProgress(progress + "%");
        }

        return enrollmentRepository.save(enrollment);
    }

    private int calculateCompletedLessons(Enrollment enrollment) {
        // Logic to calculate completed lessons
        // This could be based on lesson completion status or quiz scores
        return 0; // Implement based on your business logic
    }

    public void updateCourseProgress(int enrollmentId) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        Course course = enrollment.getCourse();

        // Calculate based on completed lessons
        int totalLessons = course.getLessonList().size();
        int completedLessons = calculateCompletedLessons(enrollment);

        if (totalLessons > 0) {
            int progress = (completedLessons * 100) / totalLessons;
            enrollment.setProgress(progress + "%");
            enrollmentRepository.save(enrollment);
        }
    }
}