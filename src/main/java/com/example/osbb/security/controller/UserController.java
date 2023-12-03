package com.example.osbb.security.controller;

import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.controller.constants.ApiConstants;
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


    @GetMapping(ApiConstants.PARAM_ID)
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return response.returnResponse(service.getUserForController(id));
    }

    @DeleteMapping(ApiConstants.PARAM_ID)
    public Object deleteUser(@PathVariable Long id) {
        return response.returnResponse(service.deleteUserForController(id));
    }


}
