package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.enums.Status;
import com.online_e_learning.virtualPathshala.model.Submission;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

        try {
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
            List<Submission> ungradedSubmissions = submissionRepository.findByGradeIsNull();
            stats.put("contentModeration", ungradedSubmissions.size());

            // Storage usage (simulated)
            stats.put("storageUsage", "65%");

            // Support tickets (simulated)
            stats.put("supportTickets", 12);

        } catch (Exception e) {
            // Return default values in case of error
            setDefaultStats(stats);
            System.err.println("❌ Error in getAdminDashboardStats: " + e.getMessage());
        }

        return stats;
    }

    public Map<String, Object> getTeacherDashboardStats(int teacherId) {
        Map<String, Object> stats = new HashMap<>();

        try {
            // My Courses
            int myCoursesCount = courseRepository.findByUserId(teacherId).size();
            stats.put("myCourses", myCoursesCount);

            // My Assignments
            int myAssignmentsCount = assignmentRepository.findByUserId(teacherId).size();
            stats.put("myAssignments", myAssignmentsCount);

            // Students Enrolled in my courses
            Long studentsEnrolled = enrollmentRepository.countByCourse_UserId(teacherId);
            stats.put("studentsEnrolled", studentsEnrolled != null ? studentsEnrolled : 0);

            // Submissions to grade
            int submissionsToGrade = submissionRepository.findByGradeIsNull().size();
            stats.put("submissionsToGrade", submissionsToGrade);

        } catch (Exception e) {
            // Return default values in case of error
            setDefaultStatsForTeacher(stats);
            System.err.println("❌ Error in getTeacherDashboardStats: " + e.getMessage());
        }

        return stats;
    }

    public Map<String, Object> getStudentDashboardStats(int studentId) {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Enrolled Courses
            int enrolledCourses = enrollmentRepository.findByUserId(studentId).size();
            stats.put("enrolledCourses", enrolledCourses);

            // Submitted Assignments
            int submittedAssignments = submissionRepository.findByUserId(studentId).size();
            stats.put("submittedAssignments", submittedAssignments);

            // Average Grade
            double averageGrade = calculateStudentAverageGrade(studentId);
            stats.put("averageGrade", Math.round(averageGrade * 100.0) / 100.0); // Round to 2 decimal places

            // Upcoming Assignments
            int upcomingAssignments = assignmentRepository.findByDueDateAfter(new Date()).size();
            stats.put("upcomingAssignments", upcomingAssignments);

        } catch (Exception e) {
            // Return default values in case of error
            setDefaultStatsForStudent(stats);
            System.err.println("❌ Error in getStudentDashboardStats: " + e.getMessage());
        }

        return stats;
    }

    private double calculateStudentAverageGrade(int studentId) {
        try {
            List<Submission> gradedSubmissions = submissionRepository.findByUserId(studentId)
                    .stream()
                    .filter(s -> s.getGrade() != null)
                    .toList();

            if (gradedSubmissions.isEmpty()) return 0.0;

            double sum = gradedSubmissions.stream()
                    .mapToDouble(Submission::getGrade)
                    .sum();

            return sum / gradedSubmissions.size();
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void setDefaultStats(Map<String, Object> stats) {
        stats.put("totalUsers", 0);
        stats.put("totalCourses", 0);
        stats.put("totalEnrollments", 0);
        stats.put("totalAssignments", 0);
        stats.put("totalStudents", 0);
        stats.put("totalTeachers", 0);
        stats.put("totalAdmins", 0);
        stats.put("pendingApprovals", 0);
        stats.put("systemHealth", "0%");
        stats.put("contentModeration", 0);
        stats.put("storageUsage", "0%");
        stats.put("supportTickets", 0);
    }

    private void setDefaultStatsForTeacher(Map<String, Object> stats) {
        stats.put("myCourses", 0);
        stats.put("myAssignments", 0);
        stats.put("studentsEnrolled", 0);
        stats.put("submissionsToGrade", 0);
    }

    private void setDefaultStatsForStudent(Map<String, Object> stats) {
        stats.put("enrolledCourses", 0);
        stats.put("submittedAssignments", 0);
        stats.put("averageGrade", 0.0);
        stats.put("upcomingAssignments", 0);
    }
}