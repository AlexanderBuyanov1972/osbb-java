package com.example.osbb.security.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.security.dto.LoginRequest;
import com.example.osbb.security.dto.RegistrationRequest;
import com.example.osbb.security.service.auth.AuthService;
import com.example.osbb.security.service.auth.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(ApiPaths.AUTH)
public class AuthController {
    @Autowired
    private IAuthService iAuthService;

    @PostMapping(value = ApiPaths.REGISTRATION)
    @ResponseBody
    public ResponseEntity<?> registration(@RequestBody RegistrationRequest registrationRequest) {
        return iAuthService.registration(registrationRequest);
    }

    @PostMapping(value = ApiPaths.LOGIN)
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return iAuthService.login(loginRequest);
    }

    @GetMapping(value = ApiPaths.LOGOUT)
    @ResponseBody
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return iAuthService.logout(request, response);
    }

    @GetMapping(value = ApiPaths.ACTIVATE + ApiPaths.LINK)
    @ResponseBody
    public ResponseEntity<?> activate(HttpServletRequest request) {
        return iAuthService.activate(request);
    }

    @GetMapping(value = ApiPaths.REFRESH)
    @ResponseBody
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        return iAuthService.refresh(request);
    }

    @GetMapping(value = ApiPaths.CHECK + ApiPaths.LINK)
    @ResponseBody
    public ResponseEntity<?> check(HttpServletRequest request) {
        return iAuthService.check(request);
    }
}
