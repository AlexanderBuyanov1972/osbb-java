package com.example.osbb.security.jwt.filters;

import com.example.osbb.security.entity.User;
import com.example.osbb.security.user_details.CustomUserDetails;
import com.example.osbb.service.TokenService;
import com.example.osbb.service.UserService;
import io.jsonwebtoken.Claims;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.apache.log4j.Logger;

import java.io.IOException;

public class AuthorizationFilter extends OncePerRequestFilter {
    private static final Logger log = Logger.getLogger(AuthorizationFilter.class);
    private final TokenService tokenService;
    private final UserService userService;

    public AuthorizationFilter(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws jakarta.servlet.ServletException, IOException {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        String valueHeader = request.getHeader("Authorization");
        log.info("String valueHeader : " + valueHeader);
        if (tokenService.validateToken(valueHeader)) {
            try {
                Claims claims = tokenService.getClaimsAccess(valueHeader.substring(7));
                log.info(String.valueOf(claims));
                String email = claims.getSubject();
                log.info(email);
                User user = userService.getUserByEmail(email);
                log.info("User user : " + user);
                CustomUserDetails customUserDetails = CustomUserDetails.fromUserEntityToCustomUserDetails(user);
                log.info("customUserDetails : " + customUserDetails);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                customUserDetails.getAuthorities()
                        ));
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }
        log.info(messageExit(methodName));
        filterChain.doFilter(request, response);
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}
