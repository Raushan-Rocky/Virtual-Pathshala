package com.online_e_learning.virtualPathshala.responseDto;

public class CourseResponseDto {
    private int id;
    private String title;
    private String code;
    private String category;
    private String description;
    private String department;
    private Integer credits;
    private String semester;
    private Integer capacity;
    private String schedule;
    private String location;
    private String prerequisites;
    private String status;
    private String teacherName;
    private int teacherId;

    // Constructors
    public CourseResponseDto() {}

    public CourseResponseDto(int id, String title, String code, String category,
                             String description, String department, Integer credits,
                             String semester, Integer capacity, String schedule,
                             String location, String prerequisites, String status,
                             String teacherName, int teacherId) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.category = category;
        this.description = description;
        this.department = department;
        this.credits = credits;
        this.semester = semester;
        this.capacity = capacity;
        this.schedule = schedule;
        this.location = location;
        this.prerequisites = prerequisites;
        this.status = status;
        this.teacherName = teacherName;
        this.teacherId = teacherId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPrerequisites() { return prerequisites; }
    public void setPrerequisites(String prerequisites) { this.prerequisites = prerequisites; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
}