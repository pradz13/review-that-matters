package com.proj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomLoginController {

    @GetMapping("/signin")
    public String login(Model model) {
        model.addAttribute("title", "Login Page");
        return "login";
    }
}
