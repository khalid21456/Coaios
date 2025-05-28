package com.Coaios.AISocialMedia.service;

import com.Coaios.AISocialMedia.agents.FitnessFanatic;
import com.Coaios.AISocialMedia.domain.NotificationType;
import com.Coaios.AISocialMedia.domain.dtos.PostDTO;
import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Notification;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.domain.entities.User;
import com.Coaios.AISocialMedia.repository.NotificationRepo;
import com.Coaios.AISocialMedia.repository.PostRepo;
import com.Coaios.AISocialMedia.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
@Service
public class FitnessFanaticService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    NotificationType poste = NotificationType.POST;
    NotificationType commente = NotificationType.COMMENT;
    NotificationType like = NotificationType.LIKE;

    @Autowired
    private FitnessFanatic agentFitnessFanatic;

    @Autowired
    private UserRepo userRepo;

    public Post poster() {
        PostDTO postDTO = agentFitnessFanatic.generatePost();
        User user = userRepo.findById(FitnessFanatic.id).get();
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setTitle(postDTO.getTitle());
        post.setUser(user);
        post.setLikes(0);
        postRepo.save(post);
        post.getUser().setPosts(null);
        String action = "FitnessFanatic has just posted a new post";
        Notification notification = new Notification();
        notification.setAction(action);
        notification.setActionType(poste.getLabel());
        notificationRepo.save(notification);
        return post;
    }

    public Comment commentPost() {
        Comment comment = agentFitnessFanatic.commentPost();
        if(comment == null) {
            return null;
        }
        String action = "FitnessFanatic commented on "+comment.getPost().getUser().getName()+"' post";
        Notification notification = new Notification();
        notification.setAction(action);
        notification.setActionType(commente.getLabel());
        notificationRepo.save(notification);
        return comment;
    }

//    @Async
//    @Transactional
//    @Scheduled(fixedDelay = 100000)
    public void fitnessFanaticAction() {
        int[] choices = {1, 2, 3};
        int randomChoice = choices[ThreadLocalRandom.current().nextInt(choices.length)];

        switch (randomChoice) {
            case 1:
                poster();
                break;
            case 2:
                commentPost();
                break;
            case 3:
                commentPost();
                break;
        }
    }

}
