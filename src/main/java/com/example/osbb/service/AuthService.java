package com.example.osbb.service;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.security.dto.RegistrationRequest;
import com.example.osbb.security.dto.TokensUserDto;
import com.example.osbb.security.dto.UserDto;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.security.entity.User;
import com.example.osbb.enums.TypoOfRoles;
import com.example.osbb.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;

import org.apache.log4j.Logger;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URL;
import java.util.*;

@Service
public class AuthService {
    private static final Logger log = Logger.getLogger(AuthService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private CookieService cookieService;
    @Autowired
    private TokenService tokenService;

    public AuthService(BCryptPasswordEncoder passwordEncoder) {
        this.objectMapper = new ObjectMapper();
        this.passwordEncoder = passwordEncoder;
    }

    public Object registration(HttpServletRequest request) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        log.info(messageEnter(methodName));
        try {
            RegistrationRequest body = objectMapper.readValue(request.getInputStream(), RegistrationRequest.class);
            log.info("RegistrationRequest body : " + body);
            String hashPassword = passwordEncoder.encode(body.getPassword());
            log.info("hashPassword : " + hashPassword);
            String activationLink = UUID.randomUUID().toString().replace("-", "");
            log.info("activationLink : " + activationLink);
            String path = ApiConstants.URL_SERVER + ApiConstants.ACTIVATE + "/" + activationLink;
            log.info("String path : " + path);
            User user = User
                    .builder()
                    .username(body.getUsername())
                    .email(body.getEmail())
                    .role(TypoOfRoles.valueOf(body.getRole()))
                    .activationLink(activationLink)
                    .password(hashPassword)
                    .activated(false)
                    .build();
            userService.createUser(user);
            log.info("Пользователь создан успешно");
            mailService.sendActivationMail(path);
            log.info("Письмо на электронную почту отправлено успешно");
            log.info(messageExit(methodName));
            return new Response(new UserDto(user), List.of("Регистрация прошла успешно"));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    public Object login(HttpServletRequest request) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            log.info(messageEnter(methodName));
            String email = request.getAttribute("email").toString();
            log.info("email : " + email);
            log.info(messageExit(methodName));
            return doMain(email);
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        String messageResponse = "Е-маил повреждён или не существует";
        try {
            String accessToken = request.getHeader("Authorization");
            log.info("accessToken : " + accessToken);
            if (tokenService.validateToken(accessToken)) {
                Claims claims = tokenService.getClaimsAccess(accessToken.substring(7));
                String email = claims.getSubject();
                if (email == null) {
                    log.error(messageResponse);
                    return new ErrorResponseMessages(List.of(messageResponse));
                }
                tokenService.removeTokenByEmail(email);
                log.info("Удаление токена произведено успешно");
//                Cookie cookie = cookieService.getCookie("refreshToken", request);
//                if (cookie == null) {
//                    log.info("Cookie file is missing or damaged.");
//                    return new ErrorResponseMessages(List.of("Cookie file is missing or damaged."));
//                }
//                cookie.setMaxAge(0);
//                response.addCookie(cookie);
                messageResponse = "Выход из системы осуществлён успешно";
                log.info(messageResponse);
                log.info(messageExit(methodName));
                return new Response(List.of(messageResponse));
            }
            messageResponse = "Токен доступа повреждён или не существует";
            log.info(messageResponse);
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    public Object activate(HttpServletRequest request) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        String messageResponse = "Активация прошла успешно";
        try {
            String url = new URL(request.getRequestURL().toString()).toString();
            log.info("Полученный URL из запроса : " + url);
            String link = url.substring(url.lastIndexOf("/") + 1).trim();
            log.info("Линк активации : " + link);
            User user = userService.getUserByActivationLink(link);
            log.info("Полученный пользователь по линку : " + user);
            if (user != null) {
                log.info("user != null : " + (user != null));
                user.setActivated(true);
                log.info("user.setActivated(true)");
                user = userService.updateUser(user);
                log.info("Пользователь успешно обновлён");
                log.info("Обновлённый user : " + user);
                log.info(messageResponse);
                log.info(messageExit(methodName));
                return new Response(new UserDto(user), List.of(messageResponse));
            }
            messageResponse = "Пользователь по данному активационному линку не существует";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    public Object refresh(HttpServletRequest request) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            String email = request.getAttribute("email").toString();
            log.info("request.getAttribute(\"email\").toString() : " + email);
            log.info(messageExit(methodName));
            return doMain(email);
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    public Object check(HttpServletRequest request) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            String url = new URL(request.getRequestURL().toString()).toString();
            log.info("Полученный URL из запроса : " + url);
            String link = url.substring(url.lastIndexOf("/") + 1).trim();
            log.info("Линк активации :  " + link);
            User user = userService.getUserByActivationLink(link);
            log.info("Полученный пользователь по линку активации : " + user);
            log.info(messageExit(methodName));
            return doMain(user.getEmail());
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    private Object doMain(String email) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        String messageResponse = "Авторизация прошла успешно";
        // --------------------------------------------------------
        User user = userService.getUserByEmail(email);
        log.info("Пользователь полученный по емаил : " + user);
        if (user != null) {
            log.info("(user != null) : " + (user != null));
            String refreshToken = tokenService.getTokenByEmail(email).getToken();
            log.info("Рефреш токен : " + refreshToken);
            if (refreshToken != null) {
                String accessToken = "Bearer " + tokenService.createTokenAccess(
                        user);
                log.info("Токен доступа : " + accessToken);
                Claims claimsAccessToken = tokenService.getClaimsAccess(accessToken.substring(7));
                log.info("claimsAccessToken : " + claimsAccessToken);
                Claims claimsRefreshToken = tokenService.getClaimsRefresh(refreshToken.substring(7));
                log.info("claimsRefreshToken : " + claimsRefreshToken);
                log.info("Authorization completed successfully.");
                TokensUserDto tudto = TokensUserDto
                        .builder()
                        .accessToken(accessToken)
                        .accessTokenIssuedAt(claimsAccessToken.getIssuedAt())
                        .accessTokenExpiredAt(claimsAccessToken.getExpiration())
                        .refreshToken(refreshToken)
                        .refreshTokenIssuedAt(claimsRefreshToken.getIssuedAt())
                        .refreshTokenExpiredAt(claimsRefreshToken.getExpiration())
                        .userDto(new UserDto(user))
                        .build();
                log.info(messageResponse);
                log.info(messageExit(methodName));
                return new Response(tudto, List.of(messageResponse));
            }
            messageResponse = "Пользователь не авторизован";
            log.error(messageResponse);
            return new Response(List.of(messageResponse));
        }
        messageResponse = "Пользователь с  email : " + email + " не существует";
        log.error(messageResponse);
        return new Response(List.of(messageResponse));
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}
