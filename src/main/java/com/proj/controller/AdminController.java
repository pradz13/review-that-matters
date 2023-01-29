package com.proj.controller;

import com.proj.entities.Post;
import com.proj.entities.User;
import com.proj.repository.PostRepository;
import com.proj.repository.UserRepository;
import com.proj.util.Utility;
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
        Pageable pageable = PageRequest.of(page, 3);
        Page<Post> posts = postRepository.getPostsPendingApproval(pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        return "admin/show-posts-for-approval";
    }

    @RequestMapping("/approve-post/{postId}")
    public String approvePost(@PathVariable("postId") Integer postId) {
        Post postToBeApproved = postRepository.findById(postId).get();
        postToBeApproved.setStatus("APPROVED");
        postRepository.save(postToBeApproved);
        return "redirect:/admin/show-posts-pending-approval/0";
    }

    @RequestMapping("/reject-post/{postId}")
    public String rejectPost(@PathVariable("postId") Integer postId) {
        Post postToBeRejected = postRepository.findById(postId).get();
        postToBeRejected.setStatus("REJECTED");
        postRepository.save(postToBeRejected);
        return "redirect:/admin/show-posts-pending-approval/0";
    }

    @RequestMapping("/review-post/{postId}")
    public String reviewPost(@PathVariable("postId") Integer postId, Model model) {
        Post postToBeReviewed = postRepository.findById(postId).get();
        model.addAttribute("post", postToBeReviewed);
        String formattedDate = Utility.formatDate(postToBeReviewed.getCreateTs());
        model.addAttribute("createdDt", formattedDate);
        return "admin/review-post";
    }
}
