package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

   // List<Course> findByUserId(int userId);
    boolean existsByCode(String code);
    // ✅ Add these methods for teacher dashboard
    @Query("SELECT c FROM Course c WHERE c.user.id = :teacherId")
    List<Course> findByUserId(@Param("teacherId") int teacherId);
}