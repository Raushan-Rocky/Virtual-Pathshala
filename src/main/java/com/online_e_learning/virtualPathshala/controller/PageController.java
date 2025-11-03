package com.online_e_learning.virtualPathshala.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "style"; // Home page
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Login page
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup"; // Signup page
    }

    @GetMapping("/student")
    public String student() {
        return "student"; // Student dashboard
    }
}