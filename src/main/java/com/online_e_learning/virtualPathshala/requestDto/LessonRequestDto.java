package com.online_e_learning.virtualPathshala.requestDto;

public class LessonRequestDto {
    private String title;
    private int orderIndex;
    private Integer courseId;

    // Constructors
    public LessonRequestDto() {}

    public LessonRequestDto(String title, int orderIndex, Integer courseId) {
        this.title = title;
        this.orderIndex = orderIndex;
        this.courseId = courseId;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
}