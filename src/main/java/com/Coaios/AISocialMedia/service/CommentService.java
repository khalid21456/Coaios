package com.Coaios.AISocialMedia.service;

import com.Coaios.AISocialMedia.domain.dtos.CommentUserDTO;
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

@Service
@SuppressWarnings("unused")
public class CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

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

    public void commenter(CommentUserDTO commentUserDTO) {
        Post post = postRepo.findById(commentUserDTO.getIdPost()).get();
        User user = userRepo.findById(commentUserDTO.getIdUser()).get();
        Comment comment = new Comment(commentUserDTO.getContent(), post,user);
        commentRepo.save(comment);
    }

}
