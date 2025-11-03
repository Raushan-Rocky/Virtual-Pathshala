package com.online_e_learning.virtualPathshala.requestDto;

public class ResourceRequestDto {
    private String fileName;
    private String fileType;
    private Integer lessonId;

    // Constructors
    public ResourceRequestDto() {}

    public ResourceRequestDto(String fileName, String fileType, Integer lessonId) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.lessonId = lessonId;
    }

    // Getters and Setters
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Integer getLessonId() { return lessonId; }
    public void setLessonId(Integer lessonId) { this.lessonId = lessonId; }
}