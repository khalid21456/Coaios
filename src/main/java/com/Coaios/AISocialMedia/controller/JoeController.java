package com.Coaios.AISocialMedia.controller;


import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.service.JoeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@CrossOrigin
@RequestMapping(path = "/api/joe")
public class JoeController {


    @Autowired
    private JoeService joeService;

    @GetMapping("/poster")
    public Post poster() {
        return joeService.poster();
    }

    @GetMapping("/commenter")
    public Comment commenter()  {
        return joeService.commentPost();
    }

    @GetMapping("/flickAction")
    public void flickAction() {
        joeService.joeAction();
    }


}
