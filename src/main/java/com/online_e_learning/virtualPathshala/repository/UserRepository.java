package com.online_e_learning.virtualPathshala.repository;

import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.enums.Status;
import com.online_e_learning.virtualPathshala.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.enrollmentList e WHERE e.course.id = :courseId")
    List<User> findUsersEnrolledInCourse(@Param("courseId") int courseId);

    List<User> findByEnrollmentList_CourseId(int courseId);

    Optional<User> findByEmailAndRole(String email, String role);

    List<User> findByRole(String role);

    // New security-related queries
    List<User> findByStatus(Status status);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.status = :status")
    long countByRoleAndStatus(@Param("role") Role role, @Param("status") Status status);

    @Query("SELECT u FROM User u WHERE u.role = 'TEACHER' AND u.status = 'ACTIVE'")
    List<User> findActiveTeachers();

    @Query("SELECT u FROM User u WHERE u.role = 'STUDENT' AND u.status = 'ACTIVE'")
    List<User> findActiveStudents();
}