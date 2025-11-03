package com.online_e_learning.virtualPathshala.converter;

import com.online_e_learning.virtualPathshala.model.Enrollment;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.requestDto.EnrollmentRequestDto;
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

    public EnrollmentRequestDto convertToResponseDto(Enrollment enrollment) {
        EnrollmentRequestDto responseDto = new EnrollmentRequestDto();
        responseDto.setProgress(enrollment.getProgress());
        responseDto.setStatus(enrollment.getStatus());

        if (enrollment.getUser() != null) {
            responseDto.setUserId(enrollment.getUser().getId());
        }
        if (enrollment.getCourse() != null) {
            responseDto.setCourseId(enrollment.getCourse().getId());
        }

        return responseDto;
    }
}