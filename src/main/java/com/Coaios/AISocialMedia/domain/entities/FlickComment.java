package com.Coaios.AISocialMedia.domain.entities;

import jakarta.persistence.*;

@Entity
public class FlickComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Post post;

    public FlickComment(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
