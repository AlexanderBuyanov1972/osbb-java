package com.example.osbb.security.jwt.filters;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.security.entity.User;
import com.example.osbb.security.user_details.CustomUserDetails;
import com.example.osbb.service.CookieService;
import com.example.osbb.service.TokenService;
import com.example.osbb.service.UserService;
import io.jsonwebtoken.Claims;
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

public class RefreshFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger log = Logger.getLogger(RefreshFilter.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final CookieService cookieService;
    private final UserService userService;

    public RefreshFilter(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            CookieService cookieService,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.cookieService = cookieService;
        this.userService = userService;
        setRefreshPath(ApiConstants.AUTH + ApiConstants.REFRESH);

    }

    private void setRefreshPath(String path) {
        setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(path, "GET"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Токен обновления отсутствует или поврежден";
        try {
            log.info(messageEnter(methodName));
            String email = "";
            String password = "";
            String refreshToken = request.getHeader("Authorization").substring(7);
            log.info("refreshToken : " + refreshToken);
            Claims claims = tokenService.getClaimsRefresh(refreshToken);
            log.info("claims : " + claims);
            if (!refreshToken.isEmpty()) {
                email = claims.getSubject();
                password = claims.get("password", String.class);
            } else {
                log.error(messageResponse);
                throw new RuntimeException(messageResponse);
            }
            User user = userService.getUserByEmail(email);
            log.info("user : " + user);
            request.setAttribute("password", password);
            CustomUserDetails customUserDetails = CustomUserDetails.fromUserEntityToCustomUserDetails(user);
            log.info("customUserDetails : " + customUserDetails);
            log.info(messageExit(methodName));
            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            email,
                            password,
                            customUserDetails.getAuthorities()));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(ERROR_SERVER);
        }
    }


    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws ServletException, IOException {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        String email = auth.getName();
        String password = request.getAttribute("password").toString();
        User user = userService.getUserByEmail(email);
        if (user != null) {
            String refreshToken = "Bearer " + tokenService.createTokenRefresh(
                    user, password);
            log.info("String refreshToken : " + refreshToken);
            tokenService.removeTokenByEmail(user.getEmail());
            tokenService.saveRefreshToken(user.getEmail(), refreshToken);
            Cookie cookieRefresh = cookieService.addCookie("refreshToken", refreshToken.substring(7));
            response.addCookie(cookieRefresh);
        }
        request.setAttribute("email", email);
        request.setAttribute("password", "*****");
        log.info(messageExit(methodName));
        chain.doFilter(request, response);
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}

