package com.online_e_learning.virtualPathshala.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "Homepage";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/student")
    public String student() {
        return "StudentDashboard"; // Changed from StudentLogin to StudentDashboard
    }

    @GetMapping("/teacher")
    public String teacher() {
        return "TeacherDashboard"; // Create this new template
    }

    @GetMapping("/admin")
    public String admin() {
        return "AdminDashboard"; // Create this new template
    }

    @GetMapping("/homepage")
    public String homepage() {
        return "Homepage";
    }
}