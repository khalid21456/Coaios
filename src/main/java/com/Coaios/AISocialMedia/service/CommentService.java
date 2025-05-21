package com.Coaios.AISocialMedia.service;

import com.Coaios.AISocialMedia.domain.entities.Comment;
import com.Coaios.AISocialMedia.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
@SuppressWarnings("unused")
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    public List<Comment> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        Iterator<Comment> commentIterator = comments.iterator();
        Comment tempComment = null;
        while(commentIterator.hasNext()) {
            tempComment = commentIterator.next();
            tempComment.getUser_comment().setPosts(null);
            tempComment.setPost(null);
        }
        return comments;
    }

}
