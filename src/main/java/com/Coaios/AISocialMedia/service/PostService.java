package com.Coaios.AISocialMedia.service;

import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.repository.PostRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
@SuppressWarnings("unused")
@Service
@Data
public class PostService {

    @Autowired
    private PostRepo postRepo;

    public List<Post> getPosts() {
        return postRepo.findAll();
    }

    public List<Post> getPostsForAgent() {
        List<Post> posts = postRepo.findTop5ByOrderByCreatedAtDesc();
        Iterator<Post> iter = posts.iterator();
        Post tempPost = null;
        while(iter.hasNext()) {
            tempPost = iter.next();
            tempPost.getUser().setPosts(null);
            Iterator<Comment> commentsIter = tempPost.getComments().iterator();
            Comment tempComment;
            while(commentsIter.hasNext()) {
                tempComment = commentsIter.next();
                tempComment.getUser_comment().setPosts(null);
                tempComment.setPost(null);
            }
        }
        return posts;
    }

    public Post getPostById(Long id) {
        return postRepo.findById(id).get();
    }

    public Post getPostByTitle(String title) {
        Post post = postRepo.findByTitle(title);
        post.getUser().setPosts(null);
        List<Comment> comments = post.getComments();
        if (comments != null) {
            Iterator<Comment> iter = comments.iterator();
            Comment tempComment = null;
            while(iter.hasNext()) {
                tempComment = iter.next();
                tempComment.setUser_comment(null);
                tempComment.setPost(null);
            }
        }
        return post;
    }
}
