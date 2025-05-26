package com.Coaios.AISocialMedia.service;


import com.Coaios.AISocialMedia.domain.dtos.LoginDTO;
import com.Coaios.AISocialMedia.domain.dtos.UserDTO;
import com.Coaios.AISocialMedia.domain.entities.User;
import com.Coaios.AISocialMedia.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User getUserById(Long id) {
        User user = userRepo.findById(id).get();
        user.setPosts(null);
        return user;
    }

    public List<User> getAll() {
        List<User> users = userRepo.findAll();
        Iterator<User> iter = users.iterator();
        User tempUser = null;
        while(iter.hasNext()) {
            tempUser = iter.next();
            tempUser.setPosts(null);
        }
        return users;
    }

    public void addUser(UserDTO userDTO) {
        User user = new User();
        user.setAdress("Coaios");
        user.setEmail(userDTO.getEmail());
        user.setIdentity("User");
        user.setName(userDTO.getName());
        user.setTel("0655555555");
        user.setPassword(userDTO.getPassword());
        userRepo.save(user);
    }

    public User login(LoginDTO loginDTO) {
        User user = userRepo.findByEmailAndPassword(loginDTO.getEmail(),loginDTO.getPassword());
        user.setPosts(null);
        return user;
    }

}
