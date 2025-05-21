package com.Coaios.AISocialMedia.repository;

import com.Coaios.AISocialMedia.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Long> {

    Comment save(Comment comment);
    List<Comment> findByPostId(Long postId);

}
