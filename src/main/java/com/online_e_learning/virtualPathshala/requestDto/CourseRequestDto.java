package com.online_e_learning.virtualPathshala.requestDto;

public class CourseRequestDto {
    private String title;
    private String category;
    private String status;
    private Integer userId;

    // Constructors
    public CourseRequestDto() {}

    public CourseRequestDto(String title, String category, String status, Integer userId) {
        this.title = title;
        this.category = category;
        this.status = status;
        this.userId = userId;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}