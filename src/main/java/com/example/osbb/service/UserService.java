package com.example.osbb.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.osbb.dao.UserDAO;
import com.example.osbb.entity.User;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public User createUser(User user) {
        if (!userDAO.existsByUsername(user.getUsername()) && !userDAO.existsByEmail(user.getEmail())) {
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            return userDAO.save(user);
        }
        return null;
    }

    public User updateUser(User user) {
        if (userDAO.existsByUsername(user.getUsername()) && userDAO.existsByEmail(user.getEmail())) {
            user.setUpdatedAt(LocalDateTime.now());
            return userDAO.save(user);
        }
        return null;
    }

    public User getUser(Long id) {
        return userDAO.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userDAO.deleteById(id);
    }

    public List<User> getAllUser() {
        return userDAO.findAll().stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    public void deleteAllUser() {
        userDAO.deleteAll();
    }

    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public User getUserByActivationLink(String link) {
        return userDAO.findByActivationLink(link);
    }

}
