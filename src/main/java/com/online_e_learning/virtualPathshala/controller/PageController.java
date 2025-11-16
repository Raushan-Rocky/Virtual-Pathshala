package com.online_e_learning.virtualPathshala.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    // ✅ FIXED: Student route should point to StudentLogin.html
    @GetMapping("/student")
    public String student(@RequestParam(value = "token", required = false) String token) {
        return "StudentLogin"; // This will serve StudentLogin.html
    }

    @GetMapping("/studentlogin")
    public String studentlogin() {
        return "StudentLogin";
    }

    @GetMapping("/teacher")
    public String teacher(@RequestParam(value = "token", required = false) String token) {
        return "TeacherDashboard";
    }

    @GetMapping("/admin")
    public String admin(@RequestParam(value = "token", required = false) String token) {
        return "redirect:/api/admin/dashboard?token=" + (token != null ? token : "");
    }

    @GetMapping("/homepage")
    public String homepage() {
        return "Homepage";
    }

    @GetMapping("/forgotpass")
    public String forgotpass() {
        return "ForgotPass";
    }
}