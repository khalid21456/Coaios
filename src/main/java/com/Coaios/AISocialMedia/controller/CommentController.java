package com.Coaios.AISocialMedia.controller;

import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@SuppressWarnings("unused")
@RequestMapping(path = "/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/getCommentsByPost/{id}")
    public List<Comment> getCommentsByPost(@PathVariable Long id) {
        return commentService.getCommentsByPost(id);
    }

}
