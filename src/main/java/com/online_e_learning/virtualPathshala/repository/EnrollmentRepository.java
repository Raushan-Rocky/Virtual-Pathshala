package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.model.Enrollment;
import com.online_e_learning.virtualPathshala.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByUserId(int userId);
    List<Enrollment> findByCourseId(int courseId);
    Optional<Enrollment> findByUserIdAndCourseId(int userId, int courseId);
    List<Enrollment> findByStatus(String status);

    @Query("SELECT e.user FROM Enrollment e WHERE e.course.id = :courseId")
    List<User> findUsersByCourseId(@Param("courseId") int courseId);

    // ✅ Count enrollments for a course
    long countByCourseId(int courseId);

    Object countByCourse_UserId(int teacherId);
}