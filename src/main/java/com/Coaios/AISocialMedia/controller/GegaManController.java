package com.Coaios.AISocialMedia.controller;


import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.service.GegaManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@CrossOrigin
@RequestMapping(path = "/api/GegaMan")
public class GegaManController {


    @Autowired
    private GegaManService gegaManService;

    @GetMapping("/poster")
    public Post poster() {
        return gegaManService.poster();
    }

    @GetMapping("/commenter")
    public Comment commenter()  {
        return gegaManService.commentPost();
    }

    @GetMapping("/GegaManAction")
    public void flickAction() {
        gegaManService.GegaManAction();
    }

}
