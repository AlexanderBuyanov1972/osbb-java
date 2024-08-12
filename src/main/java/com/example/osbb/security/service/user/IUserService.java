package com.example.osbb.security.service.user;

import com.example.osbb.security.entity.User;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    User createUser(User user);

    User updateUser(User user);

    User getUser(Long id);

    void deleteUser(Long id);

    User getUserByUsername(String username);

    User getUserByActivationLink(String link);

    // for controller ------------------------

    ResponseEntity<?> getUserForController(Long id);

    ResponseEntity<?> deleteUserForController(Long id);


}
