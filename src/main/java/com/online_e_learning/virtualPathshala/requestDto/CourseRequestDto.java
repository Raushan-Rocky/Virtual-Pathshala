package com.online_e_learning.virtualPathshala.requestDto;

public class CourseRequestDto {
    private String name;
    private String title; // ✅ YEH ADD KAREIN
    private String description;
    private String category;
    private String level;
    private String code;
    private int teacherId;

    // Constructors
    public CourseRequestDto() {}

    public CourseRequestDto(String name, String title, String description, String category, String level) {
        this.name = name;
        this.title = title; // ✅ YEH ADD KAREIN
        this.description = description;
        this.category = category;
        this.level = level;
    }

    // Getters and Setters
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

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
}