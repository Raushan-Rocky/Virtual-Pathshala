package com.online_e_learning.virtualPathshala.requestDto;

public class EnrollmentRequestDto {
    private String progress;
    private String status;
    private Integer userId;
    private Integer courseId;

    // Constructors
    public EnrollmentRequestDto() {}

    public EnrollmentRequestDto(String progress, String status, Integer userId, Integer courseId) {
        this.progress = progress;
        this.status = status;
        this.userId = userId;
        this.courseId = courseId;
    }

    // Getters and Setters
    public String getProgress() { return progress; }
    public void setProgress(String progress) { this.progress = progress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
}