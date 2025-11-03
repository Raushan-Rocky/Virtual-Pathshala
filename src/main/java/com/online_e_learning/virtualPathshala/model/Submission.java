package com.online_e_learning.virtualPathshala.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "submission")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String filePath;

    private Float grade; // ✅ Changed to Float (can be null if not graded)

    private String feedback;

    @CreationTimestamp
    private LocalDateTime submittedAt;

    @JsonBackReference("user-submission")
    @ManyToOne
    private User user;

    // ✅ Changed from @OneToOne to @ManyToOne
    @JsonBackReference("assignment-submission")
    @ManyToOne
    private Assignment assignment;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public Float getGrade() { return grade; }
    public void setGrade(Float grade) { this.grade = grade; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Assignment getAssignment() { return assignment; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }
}