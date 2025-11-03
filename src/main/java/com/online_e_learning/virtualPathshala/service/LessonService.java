package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.converter.LessonConverter;
import com.online_e_learning.virtualPathshala.exception.LessonNotFoundException;
import com.online_e_learning.virtualPathshala.exception.CourseNotFoundException;
import com.online_e_learning.virtualPathshala.model.Lesson;
import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.repository.LessonRepository;
import com.online_e_learning.virtualPathshala.repository.CourseRepository;
import com.online_e_learning.virtualPathshala.requestDto.LessonRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonConverter lessonConverter;

    // CREATE
    public Lesson createLesson(LessonRequestDto requestDto) {
        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + requestDto.getCourseId()));

        Lesson lesson = lessonConverter.convertToEntity(requestDto, course);
        return lessonRepository.save(lesson);
    }

    // READ ALL
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    // READ BY ID
    public Lesson getLessonById(int id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + id));
    }

    // READ BY COURSE ID
    public List<Lesson> getLessonsByCourseId(int courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }
        return lessonRepository.findByCourseId(courseId);
    }

    // READ BY COURSE ID ORDERED
    public List<Lesson> getLessonsByCourseIdOrdered(int courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }
        return lessonRepository.findByCourseIdOrderByOrderIndex(courseId);
    }

    // UPDATE
    public Lesson updateLesson(int id, LessonRequestDto requestDto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + id));

        // If course is being updated, validate new course exists
        if (requestDto.getCourseId() != null) {
            Course course = courseRepository.findById(requestDto.getCourseId())
                    .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + requestDto.getCourseId()));
            lesson.setCourse(course);
        }

        lessonConverter.updateEntityFromDto(requestDto, lesson);
        return lessonRepository.save(lesson);
    }

    // DELETE
    public void deleteLesson(int id) {
        if (!lessonRepository.existsById(id)) {
            throw new LessonNotFoundException("Lesson not found with id: " + id);
        }
        lessonRepository.deleteById(id);
    }
}