package com.Coaios.AISocialMedia.repository;

import com.Coaios.AISocialMedia.agents.Flick;
import com.Coaios.AISocialMedia.domain.entities.FlickComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlickCommentRepo extends JpaRepository<FlickComment,Long> {
}
