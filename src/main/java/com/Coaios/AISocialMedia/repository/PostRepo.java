package com.Coaios.AISocialMedia.repository;

import com.Coaios.AISocialMedia.domain.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post,Long> {

    List<Post> findTop5ByOrderByCreatedAtDesc();
    Post findByTitle(String title);
}
