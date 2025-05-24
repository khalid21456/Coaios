package com.Coaios.AISocialMedia.service;

import com.Coaios.AISocialMedia.agents.Asta;
import com.Coaios.AISocialMedia.agents.Joe;
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
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")

@Service
public class JoeService {


    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private Joe agentJoe;

    @Autowired
    private UserRepo userRepo;

    public Post poster() {
        PostDTO postDTO = agentJoe.generatePost();
        User user = userRepo.findById(Joe.id).get();
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setTitle(postDTO.getTitle());
        post.setUser(user);
        post.setLikes(0);
        postRepo.save(post);
        post.getUser().setPosts(null);
        String action = "Joe has just posted a new post";
        Notification notification = new Notification();
        notification.setAction(action);
        notificationRepo.save(notification);
        return post;
    }

    public Comment commentPost() {
        Comment comment = agentJoe.commentPost();
        if(comment == null) {
            return null;
        }
        String action = "Joe commented on "+comment.getPost().getUser().getName()+"' post";
        Notification notification = new Notification();
        notification.setAction(action);
        notificationRepo.save(notification);
        return comment;
    }

    public void joeAction() {
        int[] choices = {1, 2, 3};
        int randomChoice = choices[ThreadLocalRandom.current().nextInt(choices.length)];

        switch (randomChoice) {
            case 1:
                poster();
                break;
            case 2:
                poster();
                break;
            case 3:
                commentPost();
                break;
        }
    }
}
