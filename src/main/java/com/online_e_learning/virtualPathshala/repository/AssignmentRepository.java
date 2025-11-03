package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findByUserId(int userId);
    List<Assignment> findByCourseId(int courseId);
    List<Assignment> findByDueDateAfter(Date date);
    List<Assignment> findByDueDateBefore(Date date);
}