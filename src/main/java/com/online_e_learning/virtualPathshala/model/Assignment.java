package com.online_e_learning.virtualPathshala.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "assignment")
public class Assignment {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id;

 @Column(nullable = false)
 private String title;

 @Column(nullable = false)
 private String description;

 @Column(nullable = false)
 private Date dueDate;

 @CreationTimestamp
 private LocalDateTime createdAt;

 @JsonBackReference("user-assignment")
 @ManyToOne
 private User user;

 @JsonBackReference("course-assignment")
 @ManyToOne
 private Course course;

 // ✅ Changed from @OneToOne to @OneToMany
 @JsonManagedReference("submission-assignment")
 @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
 private List<Submission> submissions;

 // Getters and Setters
 public int getId() { return id; }
 public void setId(int id) { this.id = id; }
 public String getTitle() { return title; }
 public void setTitle(String title) { this.title = title; }
 public String getDescription() { return description; }
 public void setDescription(String description) { this.description = description; }
 public Date getDueDate() { return dueDate; }
 public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
 public LocalDateTime getCreatedAt() { return createdAt; }
 public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
 public User getUser() { return user; }
 public void setUser(User user) { this.user = user; }
 public Course getCourse() { return course; }
 public void setCourse(Course course) { this.course = course; }
 public List<Submission> getSubmissions() { return submissions; }
 public void setSubmissions(List<Submission> submissions) { this.submissions = submissions; }
}