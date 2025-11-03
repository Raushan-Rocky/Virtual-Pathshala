package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("totalCourses", courseRepository.count());
        stats.put("totalEnrollments", enrollmentRepository.count());
        stats.put("totalAssignments", assignmentRepository.count());
        stats.put("pendingSubmissions", submissionRepository.findByGradeIsNull().size());

        return stats;
    }

    public Map<String, Object> getTeacherDashboardStats(int teacherId) {
        Map<String, Object> stats = new HashMap<>();

        stats.put("myCourses", courseRepository.findByUserId(teacherId).size());
        stats.put("myAssignments", assignmentRepository.findByUserId(teacherId).size());
        stats.put("studentsEnrolled", enrollmentRepository.countByCourse_UserId(teacherId));
        stats.put("submissionsToGrade", submissionRepository.findByGradeIsNull().size());

        return stats;
    }

    public Map<String, Object> getStudentDashboardStats(int studentId) {
        Map<String, Object> stats = new HashMap<>();

        stats.put("enrolledCourses", enrollmentRepository.findByUserId(studentId).size());
        stats.put("submittedAssignments", submissionRepository.findByUserId(studentId).size());
        stats.put("averageGrade", calculateStudentAverageGrade(studentId));
        stats.put("upcomingAssignments", assignmentRepository.findByDueDateAfter(new Date()).size());

        return stats;
    }

    private double calculateStudentAverageGrade(int studentId) {
        // Implementation for average grade calculation
        return 0.0;
    }
}