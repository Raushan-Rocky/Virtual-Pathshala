package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.converter.AssignmentConverter;
import com.online_e_learning.virtualPathshala.exception.AssignmentNotFoundException;
import com.online_e_learning.virtualPathshala.exception.UserNotFoundException;
import com.online_e_learning.virtualPathshala.exception.CourseNotFoundException;
import com.online_e_learning.virtualPathshala.model.Assignment;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.repository.AssignmentRepository;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.repository.CourseRepository;
import com.online_e_learning.virtualPathshala.requestDto.AssignmentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssignmentConverter assignmentConverter;

    @Autowired
    private NotificationService notificationService; // ✅ Add this

    // CREATE
    public Assignment createAssignment(AssignmentRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDto.getUserId()));

        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + requestDto.getCourseId()));

        Assignment assignment = assignmentConverter.convertToEntity(requestDto, user, course);
        Assignment savedAssignment = assignmentRepository.save(assignment); // ✅ Save first

        // ✅ Notify enrolled students about new assignment
        notificationService.notifyNewAssignment(course.getId(), savedAssignment.getTitle());

        return savedAssignment;
    }

    // READ ALL
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    // READ BY ID
    public Assignment getAssignmentById(int id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found with id: " + id));
    }

    // READ BY USER ID (Instructor's assignments)
    public List<Assignment> getAssignmentsByUserId(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return assignmentRepository.findByUserId(userId);
    }

    // READ BY COURSE ID
    public List<Assignment> getAssignmentsByCourseId(int courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }
        return assignmentRepository.findByCourseId(courseId);
    }

    // READ UPCOMING ASSIGNMENTS
    public List<Assignment> getUpcomingAssignments() {
        return assignmentRepository.findByDueDateAfter(new Date());
    }

    // READ OVERDUE ASSIGNMENTS
    public List<Assignment> getOverdueAssignments() {
        return assignmentRepository.findByDueDateBefore(new Date());
    }

    // UPDATE
    public Assignment updateAssignment(int id, AssignmentRequestDto requestDto) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found with id: " + id));

        // If instructor is being updated, validate new user exists
        if (requestDto.getUserId() != null) {
            User user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDto.getUserId()));
            assignment.setUser(user);
        }

        // If course is being updated, validate new course exists
        if (requestDto.getCourseId() != null) {
            Course course = courseRepository.findById(requestDto.getCourseId())
                    .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + requestDto.getCourseId()));
            assignment.setCourse(course);
        }

        assignmentConverter.updateEntityFromDto(requestDto, assignment);
        Assignment updatedAssignment = assignmentRepository.save(assignment);

        // ✅ Optional: Notify about assignment updates
        notificationService.notifyNewAssignment(updatedAssignment.getCourse().getId(),
                "Updated: " + updatedAssignment.getTitle());

        return updatedAssignment;
    }

    // DELETE
    public void deleteAssignment(int id) {
        if (!assignmentRepository.existsById(id)) {
            throw new AssignmentNotFoundException("Assignment not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
    }
}