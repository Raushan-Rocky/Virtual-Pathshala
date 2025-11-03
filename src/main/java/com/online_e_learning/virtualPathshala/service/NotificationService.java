package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public void notifyNewAssignment(int courseId, String assignmentTitle) {
        // ✅ Correct way to get enrolled students
        List<User> enrolledStudents = enrollmentRepository.findUsersByCourseId(courseId);

        enrolledStudents.forEach(student -> {
            System.out.println("Notification to " + student.getEmail() +
                    ": New assignment - " + assignmentTitle);
            // In real app: Send email, push notification, etc.
        });
    }

    public void notifyGradePosted(int userId, String assignmentTitle, Float grade) {
        // This method doesn't need the enrolled students query
        // You can keep it as is or get user from UserRepository

    }
}