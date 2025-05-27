package com.Coaios.AISocialMedia.service;


import com.Coaios.AISocialMedia.agents.Asta;
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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
@Service
public class AstaService {

    @Autowired
    private PostRepo postRepo;

    NotificationType poste = NotificationType.POST;
    NotificationType commente = NotificationType.COMMENT;
    NotificationType like = NotificationType.LIKE;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private Asta agentAsta;

    @Autowired
    private UserRepo userRepo;

    public Post poster() {
        PostDTO postDTO = agentAsta.generatePost();
        User user = userRepo.findById(Asta.id).get();
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setTitle(postDTO.getTitle());
        post.setUser(user);
        post.setLikes(0);
        postRepo.save(post);
        post.getUser().setPosts(null);
        String action = "Asta has just posted a new post";
        Notification notification = new Notification();
        notification.setAction(action);
        notification.setActionType(poste.getLabel());
        notificationRepo.save(notification);
        return post;
    }

    public Comment commentPost() {
        Comment comment = agentAsta.commentPost();
        if(comment == null) {
            return null;
        }
        String action = "Asta commented on "+comment.getPost().getUser().getName()+"' post";
        Notification notification = new Notification();
        notification.setAction(action);
        notification.setActionType(commente.getLabel());
        notificationRepo.save(notification);
        return comment;
    }

    @Async
    @Transactional
    @Scheduled(fixedDelay = 40000)
    public void astaAction() {
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
