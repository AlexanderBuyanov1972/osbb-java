package com.example.osbb.security.controller;

import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(ApiConstants.AUTH)
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private HelpMethodsForController response;

    @PostMapping(value = ApiConstants.REGISTRATION)
    @ResponseBody
    public ResponseEntity<?> registration(HttpServletRequest request) {
        return response.returnResponse(authService.registration(request));
    }

    @PostMapping(value = ApiConstants.LOGIN)
    @ResponseBody
    public ResponseEntity<?> login(HttpServletRequest request) {
        return response.returnResponse(authService.login(request));
    }

    @GetMapping(value = ApiConstants.LOGOUT)
    @ResponseBody
    public  ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Object object = authService.logout(request, response);
        return object.getClass().equals(ErrorResponseMessages.class) ?
                ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(object)
                :
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(object);
    }

    @GetMapping(value = ApiConstants.ACTIVATE + ApiConstants.LINK)
    @ResponseBody
    public ResponseEntity<?> activate(HttpServletRequest request) {
        return response.returnResponse(authService.activate(request));
    }

    @GetMapping(value = ApiConstants.REFRESH)
    @ResponseBody
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        return response.returnResponse(authService.refresh(request));
    }

    @GetMapping(value = ApiConstants.CHECK + ApiConstants.LINK)
    @ResponseBody
    public ResponseEntity<?> check(HttpServletRequest request) {
        return response.returnResponse(authService.check(request));
    }
}
