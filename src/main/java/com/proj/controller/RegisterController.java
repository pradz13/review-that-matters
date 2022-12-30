package com.proj.controller;

import com.proj.entities.User;
import com.proj.helper.Message;
import com.proj.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/signup")
    public String register(Model model) {
        model.addAttribute("title", "Register - Review that matters");
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/do_signup", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession httpSession) {
        try {
            if(result.hasErrors()) {
                System.out.println(result.toString());
                model.addAttribute("user", user);
                return "signup";
            }
            System.out.println(user);
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            this.userRepository.save(user);
            model.addAttribute("user", new User());
            httpSession.setAttribute("message", new Message("Successfully registered", "alert-success"));
        } catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            httpSession.setAttribute("message", new Message("Something went wrong", "alert-danger"));
        }
        return "signup";
    }
}
