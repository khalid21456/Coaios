package com.Coaios.AISocialMedia.controller;


import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.service.FlickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/flick")
public class FlickController {

    @Autowired
    private FlickService flickService;

    @GetMapping("/poster")
    public Post poster() {
        return flickService.poster();
    }

}
