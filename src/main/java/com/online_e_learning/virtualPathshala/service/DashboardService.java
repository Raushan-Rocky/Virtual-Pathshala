package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.enums.Status;
import com.online_e_learning.virtualPathshala.model.Submission;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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

        // Total counts
        stats.put("totalUsers", userRepository.count());
        stats.put("totalCourses", courseRepository.count());
        stats.put("totalEnrollments", enrollmentRepository.count());
        stats.put("totalAssignments", assignmentRepository.count());

        // User breakdown by role
        stats.put("totalStudents", userRepository.findByRole(Role.STUDENT).size());
        stats.put("totalTeachers", userRepository.findByRole(Role.TEACHER).size());
        stats.put("totalAdmins", userRepository.findByRole(Role.ADMIN).size());

        // Pending approvals (inactive teachers)
        List<User> pendingTeachers = userRepository.findByRole(Role.TEACHER)
                .stream()
                .filter(user -> user.getStatus() == Status.INACTIVE)
                .toList();
        stats.put("pendingApprovals", pendingTeachers.size());

        // System health (simulated)
        stats.put("systemHealth", "98%");

        // Content moderation (ungraded submissions)
        stats.put("contentModeration", submissionRepository.findByGradeIsNull().size());

        // Storage usage (simulated)
        stats.put("storageUsage", "65%");

        // Support tickets (simulated)
        stats.put("supportTickets", 12);

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
        stats.put("upcomingAssignments", assignmentRepository.findByDueDateAfter(new java.util.Date()).size());

        return stats;
    }

    private double calculateStudentAverageGrade(int studentId) {
        List<Submission> gradedSubmissions = submissionRepository.findByUserId(studentId)
                .stream()
                .filter(s -> s.getGrade() != null)
                .toList();

        if (gradedSubmissions.isEmpty()) return 0.0;

        double sum = gradedSubmissions.stream()
                .mapToDouble(Submission::getGrade)
                .sum();

        return sum / gradedSubmissions.size();
    }
}