package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findByUserId(int userId);
    boolean existsByCode(String code);
}