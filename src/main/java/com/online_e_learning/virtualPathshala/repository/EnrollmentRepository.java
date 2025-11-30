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

    // Existing methods
    List<Enrollment> findByUserId(int userId);
    List<Enrollment> findByCourseId(int courseId);
    Optional<Enrollment> findByUserIdAndCourseId(int userId, int courseId);
    List<Enrollment> findByStatus(String status);
    long countByCourseId(int courseId);

    @Query("SELECT e.user FROM Enrollment e WHERE e.course.id = :courseId")
    List<User> findUsersByCourseId(@Param("courseId") int courseId);

    // ✅ NEW: Eager loading with user and course details
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course JOIN FETCH e.user WHERE e.user.id = :userId")
    List<Enrollment> findByUserIdWithDetails(@Param("userId") int userId);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course JOIN FETCH e.user WHERE e.course.id = :courseId")
    List<Enrollment> findByCourseIdWithDetails(@Param("courseId") int courseId);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course JOIN FETCH e.user WHERE e.id = :id")
    Optional<Enrollment> findByIdWithDetails(@Param("id") int id);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course JOIN FETCH e.user WHERE e.user.id = :userId AND e.course.id = :courseId")
    Optional<Enrollment> findByUserIdAndCourseIdWithDetails(@Param("userId") int userId, @Param("courseId") int courseId);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course JOIN FETCH e.user WHERE e.status = :status")
    List<Enrollment> findByStatusWithDetails(@Param("status") String status);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course JOIN FETCH e.user")
    List<Enrollment> findAllWithDetails();

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.teacher.id = :teacherId")
    Long countByCourse_UserId(@Param("teacherId") int teacherId);
}