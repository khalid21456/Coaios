package com.Coaios.AISocialMedia.controller;

import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/getPosts")
    public List<Post> getPost() {
        return postService.getPosts();
    }



}
