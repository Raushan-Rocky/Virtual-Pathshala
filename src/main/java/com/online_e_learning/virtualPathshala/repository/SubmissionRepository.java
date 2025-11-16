package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    List<Submission> findByUserId(int userId);
    List<Submission> findByAssignmentId(int assignmentId);
    Optional<Submission> findByUserIdAndAssignmentId(int userId, int assignmentId);
    List<Submission> findByGradeIsNull(); // Ungraded submissions
    List<Submission> findByGradeIsNotNull(); // Graded submissions

    // ✅ Method 1: Using @Query (Recommended - more explicit)
    @Query("SELECT s FROM Submission s WHERE s.user.id = :userId AND s.assignment.course.id = :courseId")
    List<Submission> findByUserIdAndCourseId(@Param("userId") int userId, @Param("courseId") int courseId);

    // ✅ Method 2: Using derived query method name
    List<Submission> findByUserIdAndAssignment_CourseId(int userId, int courseId);

    // ✅ Method to get all submissions for a course
    @Query("SELECT s FROM Submission s WHERE s.assignment.course.id = :courseId")
    List<Submission> findByAssignment_CourseId(@Param("courseId") int courseId);

    // ✅ Method to get graded submissions for a course
    @Query("SELECT s FROM Submission s WHERE s.assignment.course.id = :courseId AND s.grade IS NOT NULL")
    List<Submission> findGradedSubmissionsByCourseId(@Param("courseId") int courseId);


}