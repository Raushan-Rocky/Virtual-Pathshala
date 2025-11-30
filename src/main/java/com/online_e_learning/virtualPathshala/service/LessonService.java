package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.converter.LessonConverter;
import com.online_e_learning.virtualPathshala.exception.LessonNotFoundException;
import com.online_e_learning.virtualPathshala.exception.CourseNotFoundException;
import com.online_e_learning.virtualPathshala.model.Lesson;
import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.repository.LessonRepository;
import com.online_e_learning.virtualPathshala.repository.CourseRepository;
import com.online_e_learning.virtualPathshala.requestDto.LessonRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    private static final Logger logger = LoggerFactory.getLogger(LessonService.class);

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonConverter lessonConverter;

    // READ BY COURSE ID - FIXED VERSION
    public List<Lesson> getLessonsByCourseId(int courseId) {
        try {
            logger.info("🔍 Searching for lessons with course ID: {}", courseId);

            // First check if course exists using Optional
            Optional<Course> courseOptional = courseRepository.findById(courseId);

            if (courseOptional.isEmpty()) {
                logger.warn("⚠️ Course not found with id: {}", courseId);
                // Return empty list instead of throwing exception
                return List.of();
            }

            // Get lessons from repository
            List<Lesson> lessons = lessonRepository.findByCourseId(courseId);
            logger.info("✅ Found {} lessons for course ID: {}", lessons.size(), courseId);

            return lessons;

        } catch (Exception e) {
            logger.error("❌ Error in getLessonsByCourseId for course {}: {}", courseId, e.getMessage(), e);
            // Return empty list instead of throwing exception
            return List.of();
        }
    }

    // CREATE
    public Lesson createLesson(LessonRequestDto requestDto) {
        try {
            logger.info("🆕 Creating lesson with title: {}", requestDto.getTitle());

            Optional<Course> courseOptional = courseRepository.findById(requestDto.getCourseId());
            if (courseOptional.isEmpty()) {
                throw new CourseNotFoundException("Course not found with id: " + requestDto.getCourseId());
            }

            Course course = courseOptional.get();
            Lesson lesson = lessonConverter.convertToEntity(requestDto, course);
            Lesson savedLesson = lessonRepository.save(lesson);

            logger.info("✅ Lesson created successfully with ID: {}", savedLesson.getId());
            return savedLesson;

        } catch (Exception e) {
            logger.error("❌ Error creating lesson: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Other methods remain same as previous...
    public List<Lesson> getAllLessons() {
        try {
            List<Lesson> lessons = lessonRepository.findAll();
            logger.info("📖 Loaded {} total lessons", lessons.size());
            return lessons;
        } catch (Exception e) {
            logger.error("❌ Error loading all lessons: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Lesson getLessonById(int id) {
        try {
            logger.info("🔍 Searching for lesson with ID: {}", id);
            return lessonRepository.findById(id)
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + id));
        } catch (Exception e) {
            logger.error("❌ Error loading lesson by ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public List<Lesson> getLessonsByCourseIdOrdered(int courseId) {
        try {
            logger.info("🔍 Searching for ordered lessons with course ID: {}", courseId);

            if (!courseRepository.existsById(courseId)) {
                logger.warn("⚠️ Course not found with id: {}", courseId);
                return List.of();
            }

            List<Lesson> lessons = lessonRepository.findByCourseIdOrderByOrderIndex(courseId);
            logger.info("✅ Found {} ordered lessons for course ID: {}", lessons.size(), courseId);
            return lessons;

        } catch (Exception e) {
            logger.error("❌ Error in getLessonsByCourseIdOrdered for course {}: {}", courseId, e.getMessage(), e);
            return List.of();
        }
    }

    public Lesson updateLesson(int id, LessonRequestDto requestDto) {
        try {
            logger.info("✏️ Updating lesson with ID: {}", id);

            Lesson lesson = lessonRepository.findById(id)
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + id));

            if (requestDto.getCourseId() != null) {
                Optional<Course> courseOptional = courseRepository.findById(requestDto.getCourseId());
                if (courseOptional.isEmpty()) {
                    throw new CourseNotFoundException("Course not found with id: " + requestDto.getCourseId());
                }
                lesson.setCourse(courseOptional.get());
            }

            lessonConverter.updateEntityFromDto(requestDto, lesson);
            Lesson updatedLesson = lessonRepository.save(lesson);

            logger.info("✅ Lesson updated successfully with ID: {}", updatedLesson.getId());
            return updatedLesson;

        } catch (Exception e) {
            logger.error("❌ Error updating lesson ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public void deleteLesson(int id) {
        try {
            logger.info("🗑️ Deleting lesson with ID: {}", id);

            if (!lessonRepository.existsById(id)) {
                throw new LessonNotFoundException("Lesson not found with id: " + id);
            }

            lessonRepository.deleteById(id);
            logger.info("✅ Lesson deleted successfully with ID: {}", id);

        } catch (Exception e) {
            logger.error("❌ Error deleting lesson ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}