package com.example.osbb.security.service.auth;

import com.example.osbb.security.dto.LoginRequest;
import com.example.osbb.security.dto.RegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface IAuthService {

    ResponseEntity<?> registration(RegistrationRequest data);

    ResponseEntity<?> login(LoginRequest data);

    ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> activate(HttpServletRequest request);

    ResponseEntity<?> refresh(HttpServletRequest request);

    ResponseEntity<?> check(HttpServletRequest request);


}
