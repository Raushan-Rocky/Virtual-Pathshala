package com.online_e_learning.virtualPathshala.requestDto;

import java.util.Date;

public class AssignmentRequestDto {
    private String title;
    private String description;
    private Date dueDate;
    private String type = "HOMEWORK"; // ✅ ADD THIS FIELD
    private Integer userId;
    private Integer courseId;

    // Constructors
    public AssignmentRequestDto() {}

    public AssignmentRequestDto(String title, String description, Date dueDate, String type, Integer userId, Integer courseId) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.type = type; // ✅ ADD IN CONSTRUCTOR
        this.userId = userId;
        this.courseId = courseId;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    // ✅ ADD TYPE GETTER AND SETTER
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
}