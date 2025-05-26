package com.Coaios.AISocialMedia.repository;

import com.Coaios.AISocialMedia.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    User findByEmailAndPassword(String email, String password);

}
