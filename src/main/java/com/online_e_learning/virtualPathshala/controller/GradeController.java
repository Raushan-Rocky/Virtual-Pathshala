package com.online_e_learning.virtualPathshala.controller;

import com.online_e_learning.virtualPathshala.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "*")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    // GET STUDENT PERFORMANCE - /api/grades/student/{userId}/course/{courseId}
    @GetMapping("/student/{userId}/course/{courseId}")
    public ResponseEntity<?> getStudentPerformance(
            @PathVariable int userId,
            @PathVariable int courseId) {
        try {
            Map<String, Object> performance = gradeService.getStudentPerformance(userId, courseId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", performance);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET COURSE PERFORMANCE - /api/grades/course/{courseId}
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getCoursePerformance(@PathVariable int courseId) {
        try {
            Map<String, Object> performance = gradeService.getCoursePerformance(courseId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", performance);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET STUDENT OVERALL AVERAGE - /api/grades/student/{userId}/overall
    @GetMapping("/student/{userId}/overall")
    public ResponseEntity<?> getStudentOverallAverage(@PathVariable int userId) {
        try {
            double overallAverage = gradeService.getStudentOverallAverage(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("overallAverage", Math.round(overallAverage * 100.0) / 100.0);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}