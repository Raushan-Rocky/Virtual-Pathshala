package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {

    // ✅ Find resources by lesson ID
    List<Resource> findByLessonId(int lessonId);

    // ✅ Find resources by file type
    List<Resource> findByFileType(String fileType);

    // ✅ Search resources with custom query using correct field names
    @Query("SELECT r FROM Resource r WHERE LOWER(r.fileName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(r.fileType) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Resource> searchResources(@Param("query") String query);

    // ✅ FIXED: Find latest resources by ID (since createdAt field doesn't exist)
    List<Resource> findTop10ByOrderByIdDesc();

    // ✅ Search by fileName
    List<Resource> findByFileNameContainingIgnoreCase(String fileName);

    // ✅ Search by file type only
    List<Resource> findByFileTypeContainingIgnoreCase(String fileType);
}