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
        course.setTitle(courseRequestDto.getTitle());
        course.setCode(courseRequestDto.getCode());
        course.setCategory(courseRequestDto.getCategory());
        course.setDescription(courseRequestDto.getDescription());
        course.setDepartment(courseRequestDto.getDepartment());
        course.setCredits(courseRequestDto.getCredits());
        course.setSemester(courseRequestDto.getSemester());
        course.setCapacity(courseRequestDto.getCapacity());
        course.setSchedule(courseRequestDto.getSchedule());
        course.setLocation(courseRequestDto.getLocation());
        course.setPrerequisites(courseRequestDto.getPrerequisites());
        course.setUser(teacher);
        course.setStatus("ACTIVE");

        return course;
    }

    public CourseResponseDto courseToCourseResponseDto(Course course) {
        CourseResponseDto responseDto = new CourseResponseDto();
        responseDto.setId(course.getId());
        responseDto.setTitle(course.getTitle());
        responseDto.setCode(course.getCode());
        responseDto.setCategory(course.getCategory());
        responseDto.setDescription(course.getDescription());
        responseDto.setDepartment(course.getDepartment());
        responseDto.setCredits(course.getCredits());
        responseDto.setSemester(course.getSemester());
        responseDto.setCapacity(course.getCapacity());
        responseDto.setSchedule(course.getSchedule());
        responseDto.setLocation(course.getLocation());
        responseDto.setPrerequisites(course.getPrerequisites());
        responseDto.setStatus(course.getStatus());
        responseDto.setTeacherName(course.getUser().getName());
        responseDto.setTeacherId(course.getUser().getId());

        return responseDto;
    }
}