package com.online_e_learning.virtualPathshala.converter;

import com.online_e_learning.virtualPathshala.model.Lesson;
import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.requestDto.LessonRequestDto;
import org.springframework.stereotype.Component;

@Component
public class LessonConverter {

    public Lesson convertToEntity(LessonRequestDto requestDto, Course course) {
        Lesson lesson = new Lesson();
        lesson.setTitle(requestDto.getTitle());
        lesson.setOrderIndex(requestDto.getOrderIndex());
        lesson.setCourse(course);
        return lesson;
    }

    public void updateEntityFromDto(LessonRequestDto requestDto, Lesson lesson) {
        if (requestDto.getTitle() != null) {
            lesson.setTitle(requestDto.getTitle());
        }
        if (requestDto.getOrderIndex() >= 0) {
            lesson.setOrderIndex(requestDto.getOrderIndex());
        }
    }

    public LessonRequestDto convertToResponseDto(Lesson lesson) {
        LessonRequestDto responseDto = new LessonRequestDto();
        responseDto.setTitle(lesson.getTitle());
        responseDto.setOrderIndex(lesson.getOrderIndex());

        if (lesson.getCourse() != null) {
            responseDto.setCourseId(lesson.getCourse().getId());
        }

        return responseDto;
    }
}