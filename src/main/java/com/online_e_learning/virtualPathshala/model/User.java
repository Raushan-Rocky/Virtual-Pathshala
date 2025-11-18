package com.online_e_learning.virtualPathshala.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.enums.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String mobile;

    // ✅ ADD DEPARTMENT FIELD
    private String department;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status = Status.INACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonManagedReference("user-enrollment")
    @OneToMany(mappedBy = "user")
    private List<Enrollment> enrollmentList;

    // ✅ ADD COURSE RELATIONSHIP (For teachers)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;

    // ✅ ADD ASSIGNMENT RELATIONSHIP (For teachers)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assignment> assignments;

    // Constructors
    public User() {}

    public User(String name, String email, String passwordHash, String mobile, Role role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.mobile = mobile;
        this.role = role;
        this.department = "Computer Science"; // Default department
    }

    public User(String name, String email, String passwordHash, String mobile, Role role, Status status) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.mobile = mobile;
        this.role = role;
        this.status = status;
        this.department = "Computer Science"; // Default department
    }

    // ✅ NEW CONSTRUCTOR WITH DEPARTMENT
    public User(String name, String email, String passwordHash, String mobile, String department, Role role, Status status) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.mobile = mobile;
        this.department = department;
        this.role = role;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    // ✅ ADD DEPARTMENT GETTER AND SETTER
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Enrollment> getEnrollmentList() {
        return enrollmentList;
    }

    public void setEnrollmentList(List<Enrollment> enrollmentList) {
        this.enrollmentList = enrollmentList;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", department='" + department + '\'' +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}