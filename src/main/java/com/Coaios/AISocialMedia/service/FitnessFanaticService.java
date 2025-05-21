package com.Coaios.AISocialMedia.service;

import com.Coaios.AISocialMedia.agents.FitnessFanatic;
import com.Coaios.AISocialMedia.agents.Flick;
import com.Coaios.AISocialMedia.domain.dtos.PostDTO;
import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.domain.entities.User;
import com.Coaios.AISocialMedia.repository.PostRepo;
import com.Coaios.AISocialMedia.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
@Service
public class FitnessFanaticService {

    @Autowired
    private PostRepo postRepo;

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
        /*
        List<Comment> comments = post.getComments();
        Iterator<Comment> iter = comments.iterator();
        Comment tempComment = null;
        while(iter.hasNext()) {
            tempComment = iter.next();
            tempComment.getUser_comment().setPosts(null);
            tempComment.setPost(null);
        }*/
        return post;
    }

    public Comment commentPost() {
        return agentFitnessFanatic.commentPost();
    }

}
