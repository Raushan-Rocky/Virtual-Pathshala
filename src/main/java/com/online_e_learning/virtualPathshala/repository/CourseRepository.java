package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    boolean existsByCode(String code);

    // ✅ Add these methods for teacher dashboard
    @Query("SELECT c FROM Course c WHERE c.teacher.id = :teacherId")
    List<Course> findByTeacherId(@Param("teacherId") int teacherId);

    // ✅ Add these missing methods for LessonService
    boolean existsById(int id);

    Optional<Course> findById(int id);

    // Method for teacher courses
    @Query("SELECT c FROM Course c WHERE c.teacher.id = :userId")
    List<Course> findByUserId(@Param("userId") int userId);

    List<Course> findAll();

    // Find course by name (case insensitive)
    Optional<Course> findByNameIgnoreCase(String name);

    // Check if course exists by name
    boolean existsByNameIgnoreCase(String name);

    // Find active courses
    @Query("SELECT c FROM Course c WHERE c.status = 'ACTIVE'")
    List<Course> findActiveCourses();

    // Count courses by teacher
    @Query("SELECT COUNT(c) FROM Course c WHERE c.teacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") int teacherId);
}