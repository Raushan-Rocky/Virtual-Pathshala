package com.online_e_learning.virtualPathshala.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String progress = "0%"; // ✅ Default value

    private String status = "ACTIVE"; // ✅ Default value

    @CreationTimestamp // ✅ Added enrollment date
    private LocalDateTime enrolledAt;

    @JsonBackReference("user-enrollment")
    @ManyToOne
    private User user;

    @JsonBackReference("course-enrollment")
    @ManyToOne
    private Course course;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getProgress() { return progress; }
    public void setProgress(String progress) { this.progress = progress; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}