package com.Coaios.AISocialMedia.controller;


import com.Coaios.AISocialMedia.domain.entities.Notification;
import com.Coaios.AISocialMedia.repository.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SuppressWarnings("unused")
@CrossOrigin
@RestController
@RequestMapping(path = "/api/notif")
public class NotificationController {

    @Autowired
    private NotificationRepo notificationRepo;

    @GetMapping("/getAll")
    public List<Notification> getAll() {
        return notificationRepo.findAll();
    }

}
