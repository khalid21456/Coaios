package com.Coaios.AISocialMedia.controller;


import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.service.AstaService;
import com.Coaios.AISocialMedia.service.FlickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@CrossOrigin
@RequestMapping(path = "/api/asta")
public class AstaController {

    @Autowired
    private AstaService astaService;

    @GetMapping("/poster")
    public Post poster() {
        return astaService.poster();
    }

    @GetMapping("/commenter")
    public Comment commenter()  {
        return astaService.commentPost();
    }

}
