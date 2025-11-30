package com.online_e_learning.virtualPathshala.converter;

import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.requestDto.CourseRequestDto;
import com.online_e_learning.virtualPathshala.responseDto.CourseResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CourseConverter {

    public Course courseRequestDtoToCourse(CourseRequestDto courseRequestDto, User teacher) {
        Course course = new Course();
        course.setName(courseRequestDto.getName());
        course.setTitle(courseRequestDto.getTitle()); // ✅ YEH ADD KAREIN
        course.setDescription(courseRequestDto.getDescription());
        course.setCategory(courseRequestDto.getCategory());
        course.setLevel(courseRequestDto.getLevel());
        course.setTeacher(teacher);
        course.setStatus("ACTIVE");

        // Optional: Set other fields if they exist in CourseRequestDto
        if (courseRequestDto.getCode() != null) {
            course.setCode(courseRequestDto.getCode());
        }

        return course;
    }

    public CourseResponseDto courseToCourseResponseDto(Course course) {
        CourseResponseDto responseDto = new CourseResponseDto();
        responseDto.setId(course.getId());
        responseDto.setName(course.getName());
        responseDto.setTitle(course.getTitle()); // ✅ YEH ADD KAREIN
        responseDto.setDescription(course.getDescription());
        responseDto.setCategory(course.getCategory());
        responseDto.setLevel(course.getLevel());
        responseDto.setStatus(course.getStatus());
        responseDto.setCreatedAt(course.getCreatedAt());
        responseDto.setUpdatedAt(course.getUpdatedAt());

        // Set teacher information
        if (course.getTeacher() != null) {
            responseDto.setTeacherName(course.getTeacher().getName());
            responseDto.setTeacherId(course.getTeacher().getId());
        }

        // Optional: Set other fields if they exist
        if (course.getCode() != null) {
            responseDto.setCode(course.getCode());
        }

        return responseDto;
    }
}