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

//    @GetMapping("/style")
//    public String style() {
//        return "style";
//    }

    @GetMapping("/student")
    public String student() {
        return "StudentLogin";  // ✅ StudentLogin.html after login
    }
}