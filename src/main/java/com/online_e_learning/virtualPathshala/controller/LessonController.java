package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.converter.LessonConverter;
import com.online_e_learning.virtualPathshala.model.Lesson;
import com.online_e_learning.virtualPathshala.requestDto.LessonRequestDto;
import com.online_e_learning.virtualPathshala.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "*")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonConverter lessonConverter;

    // CREATE - POST /api/lessons
    @PostMapping
    public ResponseEntity<?> createLesson(@RequestBody LessonRequestDto requestDto) {
        try {
            Lesson lesson = lessonService.createLesson(requestDto);
            LessonRequestDto response = lessonConverter.convertToResponseDto(lesson);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Lesson created successfully");
            responseBody.put("data", response);
            responseBody.put("lessonId", lesson.getId());
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // READ ALL - GET /api/lessons
    @GetMapping
    public ResponseEntity<?> getAllLessons() {
        try {
            List<Lesson> lessons = lessonService.getAllLessons();
            List<LessonRequestDto> lessonDtos = lessons.stream()
                    .map(lessonConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", lessonDtos);
            response.put("count", lessonDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ BY ID - GET /api/lessons/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getLessonById(@PathVariable int id) {
        try {
            Lesson lesson = lessonService.getLessonById(id);
            LessonRequestDto response = lessonConverter.convertToResponseDto(lesson);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("data", response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ BY COURSE ID - GET /api/lessons/course/{courseId}
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getLessonsByCourseId(@PathVariable int courseId) {
        try {
            List<Lesson> lessons = lessonService.getLessonsByCourseId(courseId);
            List<LessonRequestDto> lessonDtos = lessons.stream()
                    .map(lessonConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", lessonDtos);
            response.put("count", lessonDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // READ BY COURSE ID ORDERED - GET /api/lessons/course/{courseId}/ordered
    @GetMapping("/course/{courseId}/ordered")
    public ResponseEntity<?> getLessonsByCourseIdOrdered(@PathVariable int courseId) {
        try {
            List<Lesson> lessons = lessonService.getLessonsByCourseIdOrdered(courseId);
            List<LessonRequestDto> lessonDtos = lessons.stream()
                    .map(lessonConverter::convertToResponseDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", lessonDtos);
            response.put("count", lessonDtos.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE - PUT /api/lessons/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(@PathVariable int id, @RequestBody LessonRequestDto requestDto) {
        try {
            Lesson lesson = lessonService.updateLesson(id, requestDto);
            LessonRequestDto response = lessonConverter.convertToResponseDto(lesson);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Lesson updated successfully");
            responseBody.put("data", response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE - DELETE /api/lessons/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable int id) {
        try {
            lessonService.deleteLesson(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Lesson deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
}