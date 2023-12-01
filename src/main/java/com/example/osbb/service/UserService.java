package com.example.osbb.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.security.dto.UserDto;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.osbb.security.dao.UserDAO;
import com.example.osbb.security.entity.User;

@Service
public class UserService {
    private static final Logger log = Logger.getLogger(UserService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TokenService tokenService;

    public User createUser(User user) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        if (!userDAO.existsByUsername(user.getUsername()) && !userDAO.existsByEmail(user.getEmail())) {
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user = userDAO.save(user);
            log.info("Пользователь успешно создан : " + user);
            log.info(messageExit(methodName));
            return user;
        } else {
            log.info("Пользователь с такими данными уже существует");
            log.info(messageExit(methodName));
            return null;
        }
    }

    public User updateUser(User user) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        if (userDAO.existsByUsername(user.getUsername()) && userDAO.existsByEmail(user.getEmail())) {
            user.setUpdatedAt(LocalDateTime.now());
            log.info("Пользователь обновлён успешно");
            log.info(messageExit(methodName));
            return userDAO.save(user);
        }
        log.info("Пользователь с такими данными не существует");
        log.info(messageExit(methodName));
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
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            User user = getUser(id);
            String messageResponse = "Пользователь c ID : " + user.getId() + " получен успешно";
            log.info(messageResponse);
            return new Response(new UserDto(user), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    public Object deleteUserForController(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Пользователь с id : " + id + " не найден";
        try {
            log.info(messageEnter(methodName));
            User user = getUser(id);
            if (user != null) {
                tokenService.removeTokenByEmail(user.getEmail());
                deleteUser(id);
                messageResponse = "Пользователь с ID : " + id + " удалён успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
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

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}
