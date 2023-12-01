package com.example.osbb.security.jwt.filters;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.security.dto.LoginRequest;
import com.example.osbb.security.entity.User;
import com.example.osbb.service.CookieService;
import com.example.osbb.service.TokenService;
import com.example.osbb.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import java.io.IOException;
import java.util.Collections;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger log = Logger.getLogger(AuthenticationFilter.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;
    private final CookieService cookieService;


    public AuthenticationFilter(
            AuthenticationManager authenticationManager,
            UserService userService,
            TokenService tokenService,
            CookieService cookieService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
        this.cookieService = cookieService;
        this.objectMapper = new ObjectMapper();
        setLoginPath(ApiConstants.AUTH + ApiConstants.LOGIN);
    }

    private void setLoginPath(String path) {
        setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(path, "POST"));
    }


    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();

            log.info(messageEnter(methodName));
            LoginRequest credentials = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            log.info("credentials : " + credentials);
            request.setAttribute("password", credentials.getPassword());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            Collections.emptyList()
                    ));
        } catch (IOException e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        log.info(messageEnter(methodName));
        log.info("Authentication auth : " + auth);
        String email = auth.getName();
        String password = request.getAttribute("password").toString();
        User user = userService.getUserByEmail(email);
        log.info("User user : " + user);
        if (user != null) {
            String refreshToken = "Bearer " + tokenService.createTokenRefresh(user, password);
            tokenService.removeTokenByEmail(user.getEmail());
            tokenService.saveRefreshToken(user.getEmail(), refreshToken);
            Cookie cookieRefresh = cookieService.addCookie("refreshToken", refreshToken.substring(7));
            response.addCookie(cookieRefresh);
        }
        request.setAttribute("email", email);
        request.setAttribute("password", "*****");
        chain.doFilter(request, response);
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}
