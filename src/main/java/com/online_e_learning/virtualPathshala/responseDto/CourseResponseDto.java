package com.online_e_learning.virtualPathshala.responseDto;

import java.time.LocalDateTime;

public class CourseResponseDto {
    private int id;
    private String name;
    private String title; // ✅ YEH ADD KAREIN
    private String description;
    private String category;
    private String level;
    private String status;
    private String teacherName;
    private int teacherId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String code;

    // Constructors
    public CourseResponseDto() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; } // ✅ YEH ADD KAREIN
    public void setTitle(String title) { this.title = title; } // ✅ YEH ADD KAREIN

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}