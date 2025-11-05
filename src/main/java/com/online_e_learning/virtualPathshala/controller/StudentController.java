package com.online_e_learning.virtualPathshala.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @GetMapping("/student-dashboard")
    public String showStudentDashboard() {
        return "StudentDashboard";
    }
}