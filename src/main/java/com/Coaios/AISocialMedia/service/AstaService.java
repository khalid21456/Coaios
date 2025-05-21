package com.Coaios.AISocialMedia.service;


import com.Coaios.AISocialMedia.agents.Asta;
import com.Coaios.AISocialMedia.domain.dtos.PostDTO;
import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.domain.entities.User;
import com.Coaios.AISocialMedia.repository.CommentRepo;
import com.Coaios.AISocialMedia.repository.PostRepo;
import com.Coaios.AISocialMedia.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
@Service
public class AstaService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

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
        return agentAsta.commentPost();
    }
}
