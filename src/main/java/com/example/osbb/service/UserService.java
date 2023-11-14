package com.example.osbb.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.osbb.dto.UserDto;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.osbb.dao.UserDAO;
import com.example.osbb.entity.User;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TokenService tokenService;

    public User createUser(User user) {
        if (!userDAO.existsByUsername(user.getUsername()) && !userDAO.existsByEmail(user.getEmail())) {
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            return userDAO.save(user);
        } else {
            return null;
        }
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

    // for controller ------------------------

    public Object getUserForController(Long id) {
        return Response
                .builder()
                .data(new UserDto(getUser(id)))
                .messages(List.of("Пользователь получен успешно."))
                .build();
    }

    public Object deleteUserForController(Long id) {
        User user = getUser(id);
        if (user != null) {
            tokenService.removeTokenByEmail(user.getEmail());
            deleteUser(id);
            return new ResponseMessages(List.of("Пользователь удалён успешно."));
        }
        return new ErrorResponseMessages(List.of("Пользователь с таким id не найден."));
    }


    // get user by not id -----------------------------------
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
