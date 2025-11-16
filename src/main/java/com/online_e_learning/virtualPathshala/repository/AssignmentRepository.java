package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    //List<Assignment> findByUserId(int userId);
    List<Assignment> findByCourseId(int courseId);
    //List<Assignment> findByDueDateAfter(Date date);
    List<Assignment> findByDueDateBefore(Date date);
    @Query("SELECT a FROM Assignment a WHERE a.user.id = :teacherId")
    List<Assignment> findByUserId(@Param("teacherId") int teacherId);

    @Query("SELECT a FROM Assignment a WHERE a.dueDate > :currentDate")
    List<Assignment> findByDueDateAfter(@Param("currentDate") Date currentDate);
}