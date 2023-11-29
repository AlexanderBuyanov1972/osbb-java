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
        log.info("Method registration - enter");
        try {
            RegistrationRequest body = objectMapper.readValue(request.getInputStream(), RegistrationRequest.class);
            String hashPassword = passwordEncoder.encode(body.getPassword());
            String activationLink = UUID.randomUUID().toString().replace("-", "");
            String path = ApiConstants.URL_SERVER + ApiConstants.ACTIVATE + "/" + activationLink;
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
            mailService.sendActivationMail(path);
            log.info("Регистрация прошла успешно");
            log.info("Method registration - exit");
            return Response
                    .builder()
                    .data(new UserDto(user))
                    .messages(List.of("Регистрация прошла успешно"))
                    .build();

        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    public Object login(HttpServletRequest request) {
        log.info("Method login - enter");
        try {
            String email = request.getAttribute("email").toString();
            log.info("email : " + email);
            return doMain(email);
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method logout - enter");
        try {
            String accessToken = request.getHeader("Authorization");
            log.info("accessToken : " + accessToken);
            if (tokenService.validateToken(accessToken)) {
                Claims claims = tokenService.getClaimsAccess(accessToken.substring(7));
                String email = claims.getSubject();
                if (email == null) {
                    log.error("Email invalid or is not exists.");
                    return new ErrorResponseMessages(List.of("Email invalid or is not exists."));
                }
                tokenService.removeTokenByEmail(email);
//                Cookie cookie = cookieService.getCookie("refreshToken", request);
//                if (cookie == null) {
//                    log.info("Cookie file is missing or damaged.");
//                    return new ErrorResponseMessages(List.of("Cookie file is missing or damaged."));
//                }
//                cookie.setMaxAge(0);
//                response.addCookie(cookie);
                log.info("Выход из системы осуществлён успешно");
                log.info("Method logout - exit");
                return new Response(List.of("Выход из системы осуществлён успешно"));
            }
            log.info("Access token invalid or is not exists");
            return new ErrorResponseMessages(List.of("Access token invalid or is not exists"));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    public Object activate(HttpServletRequest request) {
        log.info("Method activate - enter");
        try {
            String url = new URL(request.getRequestURL().toString()).toString();
            String link = url.substring(url.lastIndexOf("/") + 1).trim();
            log.info("Линк активации : " + link);
            User user = userService.getUserByActivationLink(link);
            if (user != null) {
                user.setActivated(true);
                userService.updateUser(user);
                log.info("user : " + user);
                log.error("Активация прошла успешно");
                log.info("Method activate - enter");
                return Response.builder()
                        .data(new UserDto(user))
                        .messages(List.of("Активация прошла успешно"))
                        .build();
            }
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
        log.info("The user for this activation link does not exist");
        return new ErrorResponseMessages(List.of("The user for this activation link does not exist"));
    }

    public Object refresh(HttpServletRequest request) {
        log.info("Method refresh - enter");
        try {
            String email = request.getAttribute("email").toString();
            return doMain(email);
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    public Object check(HttpServletRequest request) {
        log.info("Method check - enter");
        try {
            String url = new URL(request.getRequestURL().toString()).toString();
            String link = url.substring(url.lastIndexOf("/") + 1).trim();
            log.info("Линк активации :  " + link);
            User user = userService.getUserByActivationLink(link);
            log.info("email : " + user.getEmail());
            return doMain(user.getEmail());
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    private Object doMain(String email) {
        log.error("Entered the doMain method.");
        // --------------------------------------------------------
        User user = userService.getUserByEmail(email);
        if (user != null) {
            String refreshToken = tokenService.getTokenByEmail(email).getToken();
            if (refreshToken != null) {
                String accessToken = "Bearer " + tokenService.createTokenAccess(
                        user);
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
                return Response.builder()
                        .data(tudto)
                        .messages(List.of("Авторизация прошла успешно"))
                        .build();
            }
            log.error("Пользователь не авторизован");
            return new ErrorResponseMessages(List.of("Пользователь не авторизован"));
        }
        log.info("Пользователь с  email : " + email + " не существует");
        return new ErrorResponseMessages(List.of("Пользователь с  email : " + email + " не существует"));
    }


}
