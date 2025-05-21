package com.Coaios.AISocialMedia.controller;

import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.service.FitnessFanaticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@CrossOrigin
@RequestMapping(path = "/api/fitnessFanatic")
public class FitnessFanaticController {

    @Autowired
    private FitnessFanaticService fitnessFanaticService;

    @GetMapping("/poster")
    public Post poster() {
        return fitnessFanaticService.poster();
    }

    @GetMapping("/commenter")
    public Comment commenter()  {
        return fitnessFanaticService.commentPost();
    }
}
