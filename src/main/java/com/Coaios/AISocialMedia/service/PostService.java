package com.Coaios.AISocialMedia.service;

import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.repository.PostRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class PostService {

    @Autowired
    private PostRepo postRepo;

    public List<Post> getPosts() {
        return postRepo.findAll();
    }
}
