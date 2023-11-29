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
        log.info("Method createUser : enter");
        if (!userDAO.existsByUsername(user.getUsername()) && !userDAO.existsByEmail(user.getEmail())) {
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            log.info("Пользователь успешно создан");
            log.info("Method createUser : exit");
            return userDAO.save(user);
        } else {
            log.info("Пользователь с такими данными уже существует");
            log.info("Method createUser : exit");
            return null;
        }
    }

    public User updateUser(User user) {
        log.info("Method updateUser : enter");
        if (userDAO.existsByUsername(user.getUsername()) && userDAO.existsByEmail(user.getEmail())) {
            user.setUpdatedAt(LocalDateTime.now());
            log.info("Пользователь обновлён успешно");
            log.info("Method updateUser : exit");
            return userDAO.save(user);
        }
        log.info("Пользователь с такими данными нне существует");
        log.info("Method updateUser : exit");
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
        log.info("Method getUserForController : enter");
        try {
            log.info("Пользователь получен успешно.");
            return Response
                    .builder()
                    .data(new UserDto(getUser(id)))
                    .messages(List.of("Пользователь получен успешно."))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    public Object deleteUserForController(Long id) {
        try {
            log.info("Method deleteUserForController : enter");
            User user = getUser(id);
            if (user != null) {
                tokenService.removeTokenByEmail(user.getEmail());
                deleteUser(id);
                log.info("Пользователь удалён успешно.");
                log.info("Method deleteUserForController : exit");
                return new Response(List.of("Пользователь удалён успешно."));
            }
            log.info("Пользователь с id : " + id + " не найден.");
            log.info("Method deleteUserForController : exit");
            return new ErrorResponseMessages(List.of("Пользователь с id : " +  id + " не найден."));
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

}
