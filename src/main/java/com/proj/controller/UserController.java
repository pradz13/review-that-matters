package com.proj.controller;

import com.proj.entities.Post;
import com.proj.entities.User;
import com.proj.repository.PostRepository;
import com.proj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();
        User user = this.userRepository.getUserByEmail(userName);
        model.addAttribute("user", user);
    }

    @RequestMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        return "normal/user_dashboard";
    }

    @RequestMapping("/add-post")
    public String openAddPostForm(Model model) {
        model.addAttribute("title", "Add Post");
        model.addAttribute("post", new Post());
        return "normal/add_post";
    }

    @RequestMapping("/save-post")
    public String addPost(@ModelAttribute("post") Post post, Model model) {
        User user = (User) model.getAttribute("user");
        post.setUser(user);
        postRepository.save(post);
        user.getPosts().add(post);
        userRepository.save(user);
        return "normal/add_post";
    }
}
