package com.example.osbb.controller;

import com.example.osbb.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping()
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = ApiConstants.REGISTRATION)
    @ResponseBody
    public Object registration(HttpServletRequest request) {
        return authService.registration(request);
    }

    @PostMapping(value = ApiConstants.LOGIN)
    @ResponseBody
    public Object login(HttpServletRequest request) {
        return authService.login(request);
    }

    @GetMapping(value = ApiConstants.LOGOUT)
    @ResponseBody
    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        return authService.logout(request, response);
    }

    @GetMapping(value = ApiConstants.ACTIVATE + ApiConstants.LINK)
    @ResponseBody
    public Object activate(HttpServletRequest request) {
        return authService.activate(request);
    }

    @GetMapping(value = ApiConstants.REFRESH)
    @ResponseBody
    public Object refresh(HttpServletRequest request) {
        return authService.refresh(request);
    }

    @GetMapping(value = ApiConstants.CHECK + ApiConstants.EMAIL)
    @ResponseBody
    public Object check(HttpServletRequest request) {
        return authService.check(request);
    }
}
