package com.proj.controller;

import com.proj.entities.Post;
import com.proj.repository.PostRepository;
import com.proj.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/show-content")
public class ShowContentController {

    @Autowired
    private PostRepository postRepository;

    @RequestMapping("/posts/{page}")
    public String viewPosts(@PathVariable("page") Integer page, Model model) {
        model.addAttribute("title", "Show Posts");
        Pageable pageable = PageRequest.of(page, 3);
        Page<Post> posts = postRepository.getApprovedPosts(pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        return "show-content";
    }

    @RequestMapping("/single-post/{postId}")
    public String viewPost(@PathVariable("postId") Integer postId, Model model) {
        model.addAttribute("title", "Show Posts");
        Post postToBeShown = postRepository.findById(postId).get();
        model.addAttribute("post", postToBeShown);
        String formattedDate = Utility.formatDate(postToBeShown.getCreateTs());
        model.addAttribute("createdDt", formattedDate);
        return "show-single-post";
    }
}
