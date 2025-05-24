package com.Coaios.AISocialMedia.repository;

import com.Coaios.AISocialMedia.domain.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification,Long> {

}
