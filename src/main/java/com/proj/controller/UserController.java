package com.proj.controller;

import com.proj.entities.Post;
import com.proj.entities.User;
import com.proj.helper.Message;
import com.proj.repository.PostRepository;
import com.proj.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

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
    public String addPost(@ModelAttribute("post") Post post, Model model, HttpSession httpSession) {
        try {
            User user = (User) model.getAttribute("user");
            post.setUser(user);
            postRepository.save(post);
            user.getPosts().add(post);
            userRepository.save(user);
            httpSession.setAttribute("message", new Message("Successfully added a new post", "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            httpSession.setAttribute("message", new Message("Something went wrong while adding a new post", "alert-danger"));
        }
        return "normal/add_post";
    }

    @RequestMapping("/show-posts/{page}")
    public String showPosts(@PathVariable("page") Integer page, Model model) {
        model.addAttribute("title", "Show Posts");
        User user = (User) model.getAttribute("user");
        Pageable pageable = PageRequest.of(page, 3);
        Page<Post> posts = postRepository.getPostsByUser(user.getId(), pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        return "normal/show-posts";
    }


    @RequestMapping("/delete-posts/{postId}")
    public String deletePost(@PathVariable("postId") Integer postId) {
        Post postToBeDeleted = postRepository.findById(postId).get();
        User user = postToBeDeleted.getUser();
        user.getPosts().remove(postToBeDeleted);
        userRepository.save(user);
        postRepository.delete(postToBeDeleted);
        System.out.println("Post deleted successfully");
        return "redirect:/user/show-posts/0";
    }
}