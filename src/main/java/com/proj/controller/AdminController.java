package com.proj.controller;

import com.proj.entities.Post;
import com.proj.entities.User;
import com.proj.repository.PostRepository;
import com.proj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

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

    @RequestMapping("/admin-dashboard")
    public String adminDashboard(Model model, Principal principal) {
        return "admin/admin_dashboard";
    }

    @RequestMapping("/show-posts-pending-approval/{page}")
    public String showPosts(@PathVariable("page") Integer page, Model model) {
        model.addAttribute("title", "Show Posts");
        User user = (User) model.getAttribute("user");
        Pageable pageable = PageRequest.of(page, 3);
        Page<Post> posts = postRepository.getPostsPendingApproval(pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        return "admin/show-posts-for-approval";
    }
}
