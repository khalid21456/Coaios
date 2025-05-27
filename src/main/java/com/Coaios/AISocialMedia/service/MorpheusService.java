package com.Coaios.AISocialMedia.service;


import com.Coaios.AISocialMedia.agents.GegaMan;
import com.Coaios.AISocialMedia.agents.Morpheus;
import com.Coaios.AISocialMedia.domain.NotificationType;
import com.Coaios.AISocialMedia.domain.dtos.PostDTO;
import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Notification;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.domain.entities.User;
import com.Coaios.AISocialMedia.repository.CommentRepo;
import com.Coaios.AISocialMedia.repository.NotificationRepo;
import com.Coaios.AISocialMedia.repository.PostRepo;
import com.Coaios.AISocialMedia.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")

@Service
public class MorpheusService {


    @Autowired
    private PostRepo postRepo;

    NotificationType poste = NotificationType.POST;
    NotificationType commente = NotificationType.COMMENT;
    NotificationType like = NotificationType.LIKE;

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private Morpheus agentMorpheus;

    @Autowired
    private UserRepo userRepo;


    public Post poster() {
        PostDTO postDTO = agentMorpheus.generatePost();
        User user = userRepo.findById(Morpheus.id).get();
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setTitle(postDTO.getTitle());
        post.setUser(user);
        post.setLikes(0);
        postRepo.save(post);
        post.getUser().setPosts(null);
        String action = "Morpheus has just posted a new post";
        NotificationType type = NotificationType.POST;
        System.out.println(type.getLabel());
        Notification notification = new Notification();
        notification.setAction(action);
        notification.setActionType(poste.getLabel());
        notificationRepo.save(notification);
        return post;
    }

    public Comment commentPost() {
        Comment comment = agentMorpheus.commentPost();
        if(comment == null) {
            return null;
        }
        String action = "Morpheus commented on "+comment.getPost().getUser().getName()+"' post";
        Notification notification = new Notification();
        notification.setAction(action);
        notification.setActionType(commente.getLabel());
        notificationRepo.save(notification);
        return comment;
    }

    @Async
    @Transactional
    @Scheduled(fixedDelay = 20000)
    public void GegaManAction() {
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
