package com.example.osbb.security.service.user;

import java.time.LocalDateTime;
import java.util.List;

import com.example.osbb.security.dto.UserDto;
import com.example.osbb.dto.Response;
import com.example.osbb.security.service.token.ITokenService;
import com.example.osbb.security.service.token.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.osbb.security.dao.UserDAO;
import com.example.osbb.security.entity.User;

@Slf4j
@Service
public class UserService implements IUserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ITokenService iTokenService;

    @Override
    public User createUser(User user) {
        if (!userDAO.existsByUsername(user.getUsername()) && !userDAO.existsByEmail(user.getEmail())) {
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user = userDAO.save(user);
            log.info("Пользователь успешно создан : {}", user);
            return user;
        } else {
            log.info("Пользователь с такими данными уже существует");
            return null;
        }
    }

    @Override
    public User updateUser(User user) {
        if (userDAO.existsByUsername(user.getUsername()) && userDAO.existsByEmail(user.getEmail())) {
            user.setUpdatedAt(LocalDateTime.now());
            log.info("Пользователь обновлён успешно");
            return userDAO.save(user);
        }
        log.info("Пользователь с такими данными не существует");
        return null;
    }

    @Override
    public User getUser(Long id) {
        return userDAO.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userDAO.deleteById(id);
    }

    // get user by not id -----------------------------------

    @Override
    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public User getUserByActivationLink(String link) {
        return userDAO.findByActivationLink(link);
    }

    // for controller ------------------------
    @Override
    public ResponseEntity<?> getUserForController(Long id) {
        String message = "Пользователь c ID : " + id + " получен успешно";
        User user = getUser(id);
        if (user == null) {
            message = "Пользователь c ID : " + id + " не найден";
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        log.info(message);
        return ResponseEntity.ok(new Response(new UserDto(user), List.of(message)));

    }

    @Override
    public ResponseEntity<?> deleteUserForController(Long id) {
        String message = "Пользователь с id : " + id + " не найден";
        User user = getUser(id);
        if (user != null) {
            iTokenService.removeTokenByUsername(user.getUsername());
            deleteUser(id);
            message = "Пользователь с ID : " + id + " удалён успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(id, List.of(message)));
        }
        log.info(message);
        return ResponseEntity.badRequest().body(new Response(List.of(message)));
    }


}
