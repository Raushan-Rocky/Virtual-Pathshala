package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    List<Resource> findByLessonId(int lessonId);
    List<Resource> findByFileType(String fileType);
    @Query("SELECT c FROM Course c WHERE " +
            "LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.category) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Course> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(@Param("query") String query);
}
