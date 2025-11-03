package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByUserId(int userId);
    List<Course> findByCategory(String category);
    List<Course> findByStatus(String status);

    // ✅ Add these search methods
    List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByCategoryContainingIgnoreCase(String category);

    // ✅ Method 1: Using @Query (Recommended)
    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.category) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Course> searchCourses(@Param("query") String query);

    // ✅ Method 2: Using derived query method name
    List<Course> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String title, String category);
}