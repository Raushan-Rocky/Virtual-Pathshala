package com.online_e_learning.virtualPathshala.converter;

import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.requestDto.CourseRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CourseConverter {

    public Course convertToEntity(CourseRequestDto requestDto, User user) {
        Course course = new Course();
        course.setTitle(requestDto.getTitle());
        course.setCategory(requestDto.getCategory());
        course.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : "ACTIVE");
        course.setUser(user);
        return course;
    }

    public void updateEntityFromDto(CourseRequestDto requestDto, Course course) {
        if (requestDto.getTitle() != null) {
            course.setTitle(requestDto.getTitle());
        }
        if (requestDto.getCategory() != null) {
            course.setCategory(requestDto.getCategory());
        }
        if (requestDto.getStatus() != null) {
            course.setStatus(requestDto.getStatus());
        }
    }

    public CourseRequestDto convertToResponseDto(Course course) {
        CourseRequestDto responseDto = new CourseRequestDto();
        responseDto.setTitle(course.getTitle());
        responseDto.setCategory(course.getCategory());
        responseDto.setStatus(course.getStatus());

        if (course.getUser() != null) {
            responseDto.setUserId(course.getUser().getId());
        }

        return responseDto;
    }
}