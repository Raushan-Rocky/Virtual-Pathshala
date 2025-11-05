package com.online_e_learning.virtualPathshala.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "Homepage";  // ✅ Homepage.html
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
        return "StudentLogin";  // ✅ Student dashboard
    }

    @GetMapping("/homepage")
    public String homepage() {
        return "Homepage";  // ✅ Alternative route
    }
}