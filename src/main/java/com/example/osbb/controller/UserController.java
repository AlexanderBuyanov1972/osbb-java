package com.example.osbb.controller;

import com.example.osbb.entity.User;
import com.example.osbb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ApiConstants.USER)
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private HelpMethodsForController response;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return response.returnResponse(service.createUser(user));
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        return response.returnResponse(service.updateUser(user));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return response.returnResponse(service.getUser(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllUser() {
        return response.returnResponse(service.getAllUser());
    }

    @DeleteMapping(ApiConstants.ALL)
    public void deleteAllUser() {
        service.deleteAllUser();
    }

}
