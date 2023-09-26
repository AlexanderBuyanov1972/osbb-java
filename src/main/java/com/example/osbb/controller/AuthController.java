package com.example.osbb.controller;

import com.example.osbb.entity.authorization.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.osbb.dto.Auth;
import com.example.osbb.service.auth.IAuthService;

@RestController
public class AuthController {

//    @Autowired
//    IAuthService service;
//
//    @Autowired
//    UserController userController;
//
//    @Autowired
//    private HelpMethodsForController response;
//
//    @PostMapping(ApiConstants.REGISTRATION)
//    public ResponseEntity<?> registration(@RequestBody User user) {
//        return response.returnResponse(userController.createUser(user));
//    }
//
//    @PostMapping(ApiConstants.LOGIN)
//    public ResponseEntity<?> login(@RequestBody Auth auth) {
//        return response.returnResponse(service.login(auth));
//    }
//
//    @GetMapping(ApiConstants.LOGOUT)
//    public ResponseEntity<?> logout() {
//        return response.returnResponse(service.logout());
//    }

}
