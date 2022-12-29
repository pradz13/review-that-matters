package com.proj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {

    @RequestMapping("/signup")
    public String home(Model model) {
        model.addAttribute("title", "Register - Review that matters");
        return "signup";
    }
}
