package com.Coaios.AISocialMedia.repository;

import com.Coaios.AISocialMedia.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Long> {

    Comment save(Comment comment);

}
