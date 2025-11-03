package com.online_e_learning.virtualPathshala.requestDto;

public class SubmissionRequestDto {
    private String filePath;
    private Float grade;
    private String feedback;
    private Integer userId;
    private Integer assignmentId;

    // Constructors
    public SubmissionRequestDto() {}

    public SubmissionRequestDto(String filePath, Float grade, String feedback, Integer userId, Integer assignmentId) {
        this.filePath = filePath;
        this.grade = grade;
        this.feedback = feedback;
        this.userId = userId;
        this.assignmentId = assignmentId;
    }

    // Getters and Setters
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public Float getGrade() { return grade; }
    public void setGrade(Float grade) { this.grade = grade; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Integer assignmentId) { this.assignmentId = assignmentId; }
}