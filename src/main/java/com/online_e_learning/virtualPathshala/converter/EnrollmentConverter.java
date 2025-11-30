package com.online_e_learning.virtualPathshala.converter;

import com.online_e_learning.virtualPathshala.model.Enrollment;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.requestDto.EnrollmentRequestDto;
import com.online_e_learning.virtualPathshala.responseDto.EnrollmentResponseDto;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentConverter {

    public Enrollment convertToEntity(EnrollmentRequestDto requestDto, User user, Course course) {
        Enrollment enrollment = new Enrollment();
        enrollment.setProgress(requestDto.getProgress() != null ? requestDto.getProgress() : "0%");
        enrollment.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : "ACTIVE");
        enrollment.setUser(user);
        enrollment.setCourse(course);
        return enrollment;
    }

    public void updateEntityFromDto(EnrollmentRequestDto requestDto, Enrollment enrollment) {
        if (requestDto.getProgress() != null) {
            enrollment.setProgress(requestDto.getProgress());
        }
        if (requestDto.getStatus() != null) {
            enrollment.setStatus(requestDto.getStatus());
        }
    }

    public EnrollmentResponseDto convertToResponseDto(Enrollment enrollment) {
        EnrollmentResponseDto responseDto = new EnrollmentResponseDto();

        // Basic enrollment info
        responseDto.setId(enrollment.getId());
        responseDto.setProgress(enrollment.getProgress());
        responseDto.setStatus(enrollment.getStatus());
        responseDto.setEnrolledAt(enrollment.getEnrolledAt());

        // ✅ User details with null safety
        if (enrollment.getUser() != null) {
            User user = enrollment.getUser();
            responseDto.setUserId(user.getId());
            responseDto.setUserName(user.getName() != null ? user.getName() : "Unknown User");
            responseDto.setUserEmail(user.getEmail() != null ? user.getEmail() : "N/A");
            responseDto.setUserRole(user.getRole() != null ? user.getRole().name() : "STUDENT");
            responseDto.setUserMobile(user.getMobile() != null ? user.getMobile() : "N/A");
        } else {
            // Default values if user not loaded
            responseDto.setUserId(0);
            responseDto.setUserName("Unknown Student");
            responseDto.setUserEmail("N/A");
            responseDto.setUserRole("STUDENT");
            responseDto.setUserMobile("N/A");
        }

        // ✅ Course details with null safety
        if (enrollment.getCourse() != null) {
            Course course = enrollment.getCourse();
            responseDto.setCourseId(course.getId());
            responseDto.setCourseTitle(course.getName() != null ? course.getName() : "Unknown Course");
            responseDto.setCourseCode(course.getCode() != null ? course.getCode() : "N/A");
            responseDto.setCourseCategory(course.getCategory() != null ? course.getCategory() : "General");
            responseDto.setCourseDescription(course.getDescription() != null ? course.getDescription() : "No description available");
            responseDto.setCourseLevel(course.getLevel() != null ? course.getLevel() : "General");
            responseDto.setCourseStatus(course.getStatus() != null ? course.getStatus() : "ACTIVE");

            // Teacher details from course
            if (course.getTeacher() != null) {
                User teacher = course.getTeacher();
                responseDto.setTeacherName(teacher.getName() != null ? teacher.getName() : "Unknown Teacher");
                responseDto.setTeacherId(teacher.getId());
            } else {
                responseDto.setTeacherName("Unknown Teacher");
                responseDto.setTeacherId(0);
            }
        } else {
            // Default values if course not loaded
            responseDto.setCourseId(0);
            responseDto.setCourseTitle("Unknown Course");
            responseDto.setCourseCode("N/A");
            responseDto.setCourseCategory("General");
            responseDto.setCourseDescription("No description available");
            responseDto.setCourseLevel("General");
            responseDto.setCourseStatus("ACTIVE");
            responseDto.setTeacherName("Unknown Teacher");
            responseDto.setTeacherId(0);
        }

        return responseDto;
    }

    public EnrollmentRequestDto convertToRequestDto(Enrollment enrollment) {
        EnrollmentRequestDto requestDto = new EnrollmentRequestDto();
        requestDto.setProgress(enrollment.getProgress());
        requestDto.setStatus(enrollment.getStatus());

        if (enrollment.getUser() != null) {
            requestDto.setUserId(enrollment.getUser().getId());
        }
        if (enrollment.getCourse() != null) {
            requestDto.setCourseId(enrollment.getCourse().getId());
        }

        return requestDto;
    }
}