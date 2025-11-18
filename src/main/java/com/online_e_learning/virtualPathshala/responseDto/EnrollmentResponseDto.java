package com.online_e_learning.virtualPathshala.responseDto;

import com.online_e_learning.virtualPathshala.model.User;

import java.time.LocalDateTime;

public class EnrollmentResponseDto {
    private int id;
    private String progress;
    private String status;
    private LocalDateTime enrolledAt;
    private User user;

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
    private String courseDepartment;
    private Integer courseCredits;
    private String courseSemester;
    private Integer courseCapacity;
    private String courseSchedule;
    private String courseLocation;
    private String coursePrerequisites;
    private String courseStatus;
    private String teacherName;
    private int teacherId;

    // Constructors
    public EnrollmentResponseDto() {}

    public EnrollmentResponseDto(int id, String progress, String status, LocalDateTime enrolledAt,
                                 int userId, String userName, String userEmail, String userRole, String userMobile,
                                 int courseId, String courseTitle, String courseCode, String courseCategory,
                                 String courseDescription, String courseDepartment, Integer courseCredits,
                                 String courseSemester, Integer courseCapacity, String courseSchedule,
                                 String courseLocation, String coursePrerequisites, String courseStatus,
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
        this.courseDepartment = courseDepartment;
        this.courseCredits = courseCredits;
        this.courseSemester = courseSemester;
        this.courseCapacity = courseCapacity;
        this.courseSchedule = courseSchedule;
        this.courseLocation = courseLocation;
        this.coursePrerequisites = coursePrerequisites;
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

    public String getCourseDepartment() { return courseDepartment; }
    public void setCourseDepartment(String courseDepartment) { this.courseDepartment = courseDepartment; }

    public Integer getCourseCredits() { return courseCredits; }
    public void setCourseCredits(Integer courseCredits) { this.courseCredits = courseCredits; }

    public String getCourseSemester() { return courseSemester; }
    public void setCourseSemester(String courseSemester) { this.courseSemester = courseSemester; }

    public Integer getCourseCapacity() { return courseCapacity; }
    public void setCourseCapacity(Integer courseCapacity) { this.courseCapacity = courseCapacity; }

    public String getCourseSchedule() { return courseSchedule; }
    public void setCourseSchedule(String courseSchedule) { this.courseSchedule = courseSchedule; }

    public String getCourseLocation() { return courseLocation; }
    public void setCourseLocation(String courseLocation) { this.courseLocation = courseLocation; }

    public String getCoursePrerequisites() { return coursePrerequisites; }
    public void setCoursePrerequisites(String coursePrerequisites) { this.coursePrerequisites = coursePrerequisites; }

    public String getCourseStatus() { return courseStatus; }
    public void setCourseStatus(String courseStatus) { this.courseStatus = courseStatus; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}