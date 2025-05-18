package com.Coaios.AISocialMedia.service;


import com.Coaios.AISocialMedia.agents.Flick;
import com.Coaios.AISocialMedia.domain.dtos.PostDTO;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.domain.entities.User;
import com.Coaios.AISocialMedia.repository.PostRepo;
import com.Coaios.AISocialMedia.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class FlickService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private Flick agentFlick;

    @Autowired
    private UserRepo userRepo;

    public Post poster() {
        PostDTO postDTO = agentFlick.generatePost();
        User user = userRepo.findById(Flick.id).get();
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setTitle(postDTO.getTitle());
        post.setUser(user);
        post.setLikes(0);
        postRepo.save(post);
        return post;
    }

}
