package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.converter.CourseConverter;
import com.online_e_learning.virtualPathshala.exception.CourseNotFoundException;
import com.online_e_learning.virtualPathshala.exception.UserNotFoundException;
import com.online_e_learning.virtualPathshala.model.Course;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.CourseRepository;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import com.online_e_learning.virtualPathshala.requestDto.CourseRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseConverter courseConverter;

    // CREATE
    public Course createCourse(CourseRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDto.getUserId()));

        Course course = courseConverter.convertToEntity(requestDto, user);
        return courseRepository.save(course);
    }

    // READ ALL
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // READ BY ID
    public Course getCourseById(int id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
    }

    // READ BY USER ID
    public List<Course> getCoursesByUserId(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return courseRepository.findByUserId(userId);
    }

    // READ BY CATEGORY
    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    // READ BY STATUS
    public List<Course> getCoursesByStatus(String status) {
        return courseRepository.findByStatus(status);
    }

    // UPDATE
    public Course updateCourse(int id, CourseRequestDto requestDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));

        if (requestDto.getUserId() != null) {
            User user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDto.getUserId()));
            course.setUser(user);
        }

        courseConverter.updateEntityFromDto(requestDto, course);
        return courseRepository.save(course);
    }

    // DELETE
    public void deleteCourse(int id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    // SEARCH
    public List<Course> searchCourses(String query, String category, String status) {
        if (query != null && !query.trim().isEmpty()) {
            return courseRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(query, query);
        } else if (category != null) {
            return courseRepository.findByCategory(category);
        } else if (status != null) {
            return courseRepository.findByStatus(status);
        } else {
            return getAllCourses();
        }
    }

    public List<Course> getFeaturedCourses() {
        return courseRepository.findAll().stream()
                .limit(6)
                .collect(Collectors.toList());
    }
}
