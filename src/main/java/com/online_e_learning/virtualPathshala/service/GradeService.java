package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.model.Enrollment;
import com.online_e_learning.virtualPathshala.model.Submission;
import com.online_e_learning.virtualPathshala.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GradeService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    public Map<String, Object> getStudentPerformance(int userId, int courseId) {
        // ✅ Use the correct method name
        List<Submission> submissions = submissionRepository.findByUserIdAndCourseId(userId, courseId);

        double averageGrade = submissions.stream()
                .filter(s -> s.getGrade() != null)
                .mapToDouble(Submission::getGrade)
                .average()
                .orElse(0.0);

        long submittedCount = submissions.size();
        long gradedCount = submissions.stream().filter(s -> s.getGrade() != null).count();

        Map<String, Object> performance = new HashMap<>();
        performance.put("averageGrade", Math.round(averageGrade * 100.0) / 100.0); // Round to 2 decimal places
        performance.put("totalAssignments", submittedCount);
        performance.put("gradedAssignments", gradedCount);
        performance.put("completionRate", (submittedCount > 0) ? (gradedCount * 100 / submittedCount) : 0);
        performance.put("pendingAssignments", submittedCount - gradedCount);

        return performance;
    }

    // ✅ Additional useful methods
    public Map<String, Object> getCoursePerformance(int courseId) {
        List<Submission> submissions = submissionRepository.findByAssignment_CourseId(courseId);

        double averageGrade = submissions.stream()
                .filter(s -> s.getGrade() != null)
                .mapToDouble(Submission::getGrade)
                .average()
                .orElse(0.0);

        long totalSubmissions = submissions.size();
        long gradedSubmissions = submissions.stream().filter(s -> s.getGrade() != null).count();

        Map<String, Object> performance = new HashMap<>();
        performance.put("averageGrade", Math.round(averageGrade * 100.0) / 100.0);
        performance.put("totalSubmissions", totalSubmissions);
        performance.put("gradedSubmissions", gradedSubmissions);
        performance.put("gradingProgress", (totalSubmissions > 0) ? (gradedSubmissions * 100 / totalSubmissions) : 0);

        return performance;
    }

    // ✅ Calculate student's overall average across all courses
    public double getStudentOverallAverage(int userId) {
        List<Submission> allSubmissions = submissionRepository.findByUserId(userId);

        return allSubmissions.stream()
                .filter(s -> s.getGrade() != null)
                .mapToDouble(Submission::getGrade)
                .average()
                .orElse(0.0);
    }
}