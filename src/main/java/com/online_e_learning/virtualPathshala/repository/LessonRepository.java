package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByCourseId(int courseId);
    List<Lesson> findByCourseIdOrderByOrderIndex(int courseId);
}