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

    @GetMapping("/student")
    public String student(@RequestParam(value = "token", required = false) String token) {
        return "StudentDashboard";
    }

    @GetMapping("/studentlogin")
    public String studentlogin() {
        return "Studentlogin";
    }

    @GetMapping("/teacher")
    public String teacher(@RequestParam(value = "token", required = false) String token) {
        return "TeacherDashboard";
    }

    // ✅ FIXED: Add direct /admin route with token support
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