package com.example.osbb.security.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.security.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ApiPaths.USER)
public class UserController {

    @Autowired
    private UserService service;


    @GetMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return service.getUserForController(id);
    }

    @DeleteMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return service.deleteUserForController(id);
    }


}
