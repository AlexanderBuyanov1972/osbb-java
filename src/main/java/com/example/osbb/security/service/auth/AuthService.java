package com.example.osbb.security.service.auth;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.security.dto.LoginRequest;
import com.example.osbb.security.dto.RegistrationRequest;
import com.example.osbb.security.dto.TokensDto;
import com.example.osbb.security.dto.UserDto;
import com.example.osbb.dto.Response;
import com.example.osbb.security.entity.RefreshToken;
import com.example.osbb.security.entity.Role;
import com.example.osbb.security.entity.User;
import com.example.osbb.security.service.token.ITokenService;
import com.example.osbb.security.service.user.IUserService;
import com.example.osbb.service.mail.MailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URI;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUserService iUserService;

    private final MailService mailService;

    private final ITokenService iTokenService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> registration(RegistrationRequest registrationRequest) {
        try {
            log.info(String.valueOf(registrationRequest));
            String activationLink = UUID.randomUUID().toString().replace("-", "");
            User user = buildUser(registrationRequest, activationLink);
            user = iUserService.createUser(user);
            mailService.sendActivationMail(ApiPaths.URL_SERVER + ApiPaths.ACTIVATE + "/" + activationLink);
            String message = "Регистрация прошла успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(new UserDto(user), List.of(message)));
        } catch (Exception error) {
            log.error(error.getMessage());
            return ResponseEntity.badRequest().body(new Response(List.of(error.getMessage())));
        }
    }

    private User buildUser(RegistrationRequest data, String link) {
        return User
                .builder()
                .username(data.getUsername())
                .email(data.getEmail())
                .roles(data.getRoles().stream().map(Role::new).toList())
                .activationLink(link)
                .password(passwordEncoder.encode(data.getPassword()))
                .activated(false)
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> login(LoginRequest loginRequestest) {
        try {
            String message = "Авторизация прошла успешно";
            String username = loginRequestest.getUsername();
            User user = iUserService.getUserByUsername(username);
            if (user == null) {
                message = "Пользователь с  username : " + username + " не существует. Пройдите регистрацию.";
                log.error(message);
                return ResponseEntity.badRequest().body(new Response(List.of(message)));
            }
            String accessToken = "Bearer " + iTokenService.createTokenAccess(user);
            String refreshToken = "Bearer " + iTokenService.createTokenRefresh(user);
            iTokenService.saveRefreshToken(username, refreshToken);
            log.info(message);
            return ResponseEntity.ok(new Response(new TokensDto(accessToken, refreshToken), List.of(message)));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new Response(List.of(e.getMessage())));
        }
    }

    @Override
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String message = "";
            String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            String username = iTokenService.getUsernameFromAccessToken(accessToken.substring(7));
            if (username == null) {
                message = "Токен доступа повреждён или не существует";
                log.info(message);
                return ResponseEntity.badRequest().body(new Response(List.of(message)));
            }
            iTokenService.removeTokenByUsername(username);
            response.setHeader(HttpHeaders.AUTHORIZATION, "");

//        Cookie cookie = cookieService.getCookie("refreshToken", request);
//        if (cookie == null) {
//            message = "Cookie file is missing or damaged.";
//            log.info(message);
//            return Re new Response(List.of("Cookie file is missing or damaged."));
//            return ResponseEntity.badRequest().body(new Response(List.of(message)));
//        }
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);

            message = "Выход из системы осуществлён успешно";
            log.info(message);
            return ResponseEntity.ok(new Response(List.of(message)));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new Response(List.of(e.getMessage())));
        }
    }

    @Override
    public ResponseEntity<?> activate(HttpServletRequest request) {
        String message = "Активация прошла успешно";
        try {
            String url = new URI(request.getRequestURL().toString()).toString();
            String link = url.substring(url.lastIndexOf("/") + 1).trim();
            User user = iUserService.getUserByActivationLink(link);
            if (user != null) {
                user.setActivated(true);
                user = iUserService.updateUser(user);
                log.info(message);
                return ResponseEntity.ok(new Response(new UserDto(user), List.of(message)));
            }
            message = "Пользователь по данному активационному линку не существует";
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new Response(List.of(e.getMessage())));
        }
    }

    @Override
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        String message = "";
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String username = iTokenService.getUsernameFromRefreshToken(token.substring(7));
        User user = iUserService.getUserByUsername(username);
        if (user == null) {
            message = "Вам нужно зарегистрироваться. Вас нет базе данных.";
            log.error(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        RefreshToken refreshTokenDB = iTokenService.getTokenByUsername(username);
        if (!refreshTokenDB.getToken().equals(token)) {
            message = "Токен доступа повреждён. Пройдите авторизацию.";
            log.error(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }

        String accessToken = "Bearer " + iTokenService.createTokenAccess(user);
        String refreshToken = "Bearer " + iTokenService.createTokenRefresh(user);
        iTokenService.removeTokenByUsername(username);
        iTokenService.saveRefreshToken(username, refreshToken);
        log.info(message);
        return ResponseEntity.ok(new Response(new TokensDto(accessToken, refreshToken), List.of(message)));
    }

    @Override
    public ResponseEntity<?> check(HttpServletRequest request) {
        try {
            String message = "";
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            String username = iTokenService.getUsernameFromAccessToken(token.substring(7));
            User user = iUserService.getUserByUsername(username);
            if (user == null) {
                message = "Пользователь отсутствует в базе данных";
                log.error(message);
                return ResponseEntity.badRequest().body(new Response(List.of(message)));
            }
            RefreshToken refreshTokenDB = iTokenService.getTokenByUsername(username);
            if (refreshTokenDB == null) {
                message = "Вам нужно пройти авторизацию.";
                log.error(message);
                return ResponseEntity.badRequest().body(new Response(List.of(message)));
            }
            message = "Вы авторизированы";
            log.error(message);
            return ResponseEntity.ok(new Response(List.of(message)));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new Response(List.of(e.getMessage())));
        }

    }


}
