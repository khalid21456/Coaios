package com.Coaios.AISocialMedia.domain.dtos;

import com.Coaios.AISocialMedia.domain.entities.Post;
import com.Coaios.AISocialMedia.domain.entities.User;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
public class CommentDTO {

    public CommentDTO(String content, User commenterName, Post post) {
        this.content = content;
        this.commenterName = commenterName;
        this.post = post;
    }

    private String content;
    private User commenterName;
    private Post post;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(User commenterName) {
        this.commenterName = commenterName;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
