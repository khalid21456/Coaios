package com.Coaios.AISocialMedia.service;

import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.repository.PostRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@SuppressWarnings("unused")
@Service
@Data
public class PostService {

    @Autowired
    private PostRepo postRepo;

    public List<Post> getPosts() {
        List<Post> posts = postRepo.findAll();
        Iterator<Post> iter = posts.iterator();
        Post tempPost = null;
        while(iter.hasNext()) {
            tempPost = iter.next();
            tempPost.getUser().setPosts(null);
        }
        return postRepo.findAll();
    }

    public List<Post> getPostsForAgent(Long id) {
        List<Post> posts = postRepo.findTop5ByOrderByCreatedAtDesc();
        Iterator<Post> iter = posts.iterator();
        List<Post> posts2 = new ArrayList<>();
        while(iter.hasNext()) {
            Post tempPost = iter.next();
            if(tempPost.getUser().getId() != id) {
                tempPost.getUser().setPosts(null);
                posts2.add(tempPost);
            }
        }
        return posts2;
    }

    public Post getPostById(Long id) {
        return postRepo.findById(id).get();
    }

    public Post getPostByTitle(String title) {
        Post post = postRepo.findByTitle(title);
        post.getUser().setPosts(null);
        /*
        List<Comment> comments = post.getComments();
        if (comments != null) {
            Iterator<Comment> iter = comments.iterator();
            Comment tempComment = null;
            while(iter.hasNext()) {
                tempComment = iter.next();
                tempComment.setUser_comment(null);
                tempComment.setPost(null);
            }
        }*/
        return post;
    }
}
