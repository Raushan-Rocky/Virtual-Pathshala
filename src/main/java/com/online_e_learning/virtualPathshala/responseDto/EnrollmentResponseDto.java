package com.online_e_learning.virtualPathshala.responseDto;

import java.time.LocalDateTime;

public class EnrollmentResponseDto {
    private int id;
    private String progress;
    private String status;
    private LocalDateTime enrolledAt;

    // User details
    private int userId;
    private String userName;
    private String userEmail;
    private String userRole;
    private String userMobile;

    // Course details
    private int courseId;
    private String courseTitle;
    private String courseCode;
    private String courseCategory;
    private String courseDescription;
    private String courseLevel;
    private String courseStatus;
    private String teacherName;
    private int teacherId;

    // Constructors
    public EnrollmentResponseDto() {}

    public EnrollmentResponseDto(int id, String progress, String status, LocalDateTime enrolledAt,
                                 int userId, String userName, String userEmail, String userRole, String userMobile,
                                 int courseId, String courseTitle, String courseCode, String courseCategory,
                                 String courseDescription, String courseLevel, String courseStatus,
                                 String teacherName, int teacherId) {
        this.id = id;
        this.progress = progress;
        this.status = status;
        this.enrolledAt = enrolledAt;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.userMobile = userMobile;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseCode = courseCode;
        this.courseCategory = courseCategory;
        this.courseDescription = courseDescription;
        this.courseLevel = courseLevel;
        this.courseStatus = courseStatus;
        this.teacherName = teacherName;
        this.teacherId = teacherId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getProgress() { return progress; }
    public void setProgress(String progress) { this.progress = progress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    // User getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }

    public String getUserMobile() { return userMobile; }
    public void setUserMobile(String userMobile) { this.userMobile = userMobile; }

    // Course getters and setters
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseCategory() { return courseCategory; }
    public void setCourseCategory(String courseCategory) { this.courseCategory = courseCategory; }

    public String getCourseDescription() { return courseDescription; }
    public void setCourseDescription(String courseDescription) { this.courseDescription = courseDescription; }

    public String getCourseLevel() { return courseLevel; }
    public void setCourseLevel(String courseLevel) { this.courseLevel = courseLevel; }

    public String getCourseStatus() { return courseStatus; }
    public void setCourseStatus(String courseStatus) { this.courseStatus = courseStatus; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
}