package com.example.osbb.controller.authorization;

import java.util.List;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.entity.authorization.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.osbb.service.user.IUserService;

@RestController
@RequestMapping(value = ApiConstants.USER)
public class UserController {

    @Autowired
    private IUserService service;

    @Autowired
    private HelpMethodsForController response;

//    // ---------------------- one --------------------
//
//    @PostMapping
//    public ResponseEntity<?> createUser(@RequestBody User user) {
//        return response.returnResponse(service.createUser(user));
//    }
//
//    @PutMapping
//    public ResponseEntity<?> updateUser(@RequestBody User user) {
//        return response.returnResponse(service.updateUser(user));
//    }
//
//    @GetMapping(ApiConstants.ID)
//    public ResponseEntity<?> getUser(@PathVariable Long id) {
//        return response.returnResponse(service.getUser(id));
//    }
//
//    @DeleteMapping(ApiConstants.ID)
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        return response.returnResponse(service.deleteUser(id));
//    }
//
//    // ---------------------- all ---------------------
//
//    @PostMapping(ApiConstants.ALL)
//    public ResponseEntity<?> createAllUser(@RequestBody List<User> list) {
//        return response.returnResponse(service.createAllUser(list));
//    }
//
//    @PutMapping(ApiConstants.ALL)
//    public ResponseEntity<?> updateAllUser(@RequestBody List<User> list) {
//        return response.returnResponse(service.updateAllUser(list));
//    }
//
//    @GetMapping(ApiConstants.ALL)
//    public ResponseEntity<?> getAllUser() {
//        return response.returnResponse(service.getAllUser());
//    }
//
//    @DeleteMapping(ApiConstants.ALL)
//    public ResponseEntity<?> deleteAllUser() {
//        return response.returnResponse(service.deleteAllUser());
//    }

}
