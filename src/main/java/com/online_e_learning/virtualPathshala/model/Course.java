package com.online_e_learning.virtualPathshala.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    // NEW FIELDS ADDED
    @Column(unique = true)
    private String code; // CS-301, MATH-101 etc.

    @Column(length = 1000)
    private String description;

    private String department;

    private Integer credits;

    private String semester; // Fall 2023, Spring 2024 etc.

    private Integer capacity;

    private String schedule; // Mon/Wed 9:00-10:30 AM

    private String location; // Room 301, Science Building

    private String prerequisites; // CS-201, MATH-101

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonBackReference("user-course")
    @ManyToOne
    private User user;

    @JsonManagedReference("lesson-course")
    @OneToMany(mappedBy = "course")
    private List<Lesson> lessonList;

    @JsonManagedReference("assignment-course")
    @OneToMany(mappedBy = "course")
    private List<Assignment> assignmentList;

    @JsonManagedReference("enrollment-course")
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollmentList;

    // Constructors
    public Course() {}

    public Course(String title, String code, String category, String description,
                  String department, Integer credits, String semester,
                  Integer capacity, String schedule, String location,
                  String prerequisites, User user) {
        this.title = title;
        this.code = code;
        this.category = category;
        this.description = description;
        this.department = department;
        this.credits = credits;
        this.semester = semester;
        this.capacity = capacity;
        this.schedule = schedule;
        this.location = location;
        this.prerequisites = prerequisites;
        this.user = user;
        this.status = "ACTIVE";
    }

    // Getters and Setters (existing + new)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // NEW GETTERS AND SETTERS
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    // Existing getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Lesson> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public List<Enrollment> getEnrollmentList() {
        return enrollmentList;
    }

    public void setEnrollmentList(List<Enrollment> enrollmentList) {
        this.enrollmentList = enrollmentList;
    }
}