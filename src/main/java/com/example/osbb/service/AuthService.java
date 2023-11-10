package com.example.osbb.service;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.dto.RegistrationRequest;
import com.example.osbb.dto.TokensUserDto;
import com.example.osbb.dto.UserDto;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.User;
import com.example.osbb.enums.TypoOfRoles;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URL;
import java.util.*;

@Service
public class AuthService {
    private static final Logger log = LogManager.getLogger("AppService");

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
        log.info("Entered the registration method.");
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
            log.info("Registration completed successfully.");
            return Response
                    .builder()
                    .data(new UserDto(user))
                    .messages(List.of("Регистрация прошла успешно"))
                    .build();

        } catch (Exception error) {
            log.error("Unexpected server error.");
            return new ErrorResponseMessages(List.of("Unexpected server error.", error.getMessage()));
        }
    }

    public Object login(HttpServletRequest request) {
        log.info("Entered the login method.");
        try {
            String email = request.getAttribute("email").toString();
            log.info("email : " + email);
            return doMain(email);
        } catch (Exception error) {
            log.error("Unexpected server error.");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("Unexpected server error.", error.getMessage()));
        }

    }

    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Entered the logout method.");
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
                log.error("Logout successfully");
                return new ResponseMessages(List.of("Выход из системы осуществлён успешно"));
            }
            return new ErrorResponseMessages(List.of("Access token invalid or is not exists"));
        } catch (Exception error) {
            log.error("Unexpected server error.");
            return new ErrorResponseMessages(List.of("Unexpected server error.", error.getMessage()));
        }
    }

    public Object activate(HttpServletRequest request) {
        log.info("Entered the mail activation method.");
        try {
            String url = new URL(request.getRequestURL().toString()).toString();
            String link = url.substring(url.lastIndexOf("/") + 1).trim();
            log.info("link : " + link);
            User user = userService.getUserByActivationLink(link);
            if (user != null) {
                user.setActivated(true);
                userService.updateUser(user);
                log.info("user : " + user);
                log.error("Activation was successful.");
                return Response.builder()
                        .data(new UserDto(user))
                        .messages(List.of("Activation was successful."))
                        .build();
            }
        } catch (Exception error) {
            log.error("Unexpected server error.");
            return new ErrorResponseMessages(List.of("Unexpected server error.", error.getMessage()));
        }
        return new ErrorResponseMessages(List.of("The user for this activation link does not exist"));
    }

    public Object refresh(HttpServletRequest request) {
        log.info("Entered the refresh method.");
        try {
            String email = request.getAttribute("email").toString();
            return doMain(email);
        } catch (Exception error) {
            log.error("Unexpected server error.");
            return new ErrorResponseMessages(List.of("Unexpected server error.", error.getMessage()));
        }
    }

    public Object check(HttpServletRequest request) {
        log.info("Entered user verification method.");
        try {
            String url = new URL(request.getRequestURL().toString()).toString();
            String link = url.substring(url.lastIndexOf("/") + 1).trim();
            log.info("link : " + link);
            User user = userService.getUserByActivationLink(link);
            log.info("email : " + user.getEmail());
            return doMain(user.getEmail());
        } catch (Exception error) {
            log.error("Unexpected server error.");
            return new ErrorResponseMessages(List.of("Unexpected server error.", error.getMessage()));
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
            log.error("The user is not authorized.");
            return new ErrorResponseMessages(List.of("The user is not authorized."));
        }
        log.info("User with that email is not exists.");
        return new ErrorResponseMessages(List.of("User with that email is not exists."));
    }


}
