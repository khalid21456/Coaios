package com.Coaios.AISocialMedia.controller;

import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.domain.entities.User;
import com.Coaios.AISocialMedia.repository.PostRepo;
import com.Coaios.AISocialMedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.swing.border.TitledBorder;
import java.util.List;
@SuppressWarnings("unused")
@RestController
@CrossOrigin
@RequestMapping(path = "/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepo postRepo;

    @GetMapping("/getPosts")
    public List<Post> getPost() {
        return postService.getPosts();
    }

    @GetMapping("/get5Posts/{id}")
    public List<Post> find5(@PathVariable Long id) {
        //return postRepo.findTop5ByOrderByCreatedAtDesc();
        return postService.getPostsForAgent(id);
    }

    // Testing APIs

    @GetMapping("/getPostByTitle")
    public Post getPostByTitle(@RequestParam String title) {
        return postService.getPostByTitle(title);
    }

    @GetMapping("/getPostById/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }


}
