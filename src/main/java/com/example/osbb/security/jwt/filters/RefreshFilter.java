package com.example.osbb.security.jwt.filters;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.entity.User;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



import java.io.IOException;

public class RefreshFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger log = LogManager.getLogger("Class - RefreshFilter");
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
        setRefreshPath(ApiConstants.REFRESH);

    }

    private void setRefreshPath(String path) {
        setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(path, "GET"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            log.info("Enter in attemptAuthentication");
            String email = "";
            String password = "";
            String refreshToken = request.getHeader("Authorization").substring(7);
            log.info("refreshToken : " + refreshToken);
            Claims claims = tokenService.getClaimsRefresh(refreshToken);
            log.info("claims : " + claims);
            if (!refreshToken.equals("")) {
                email = claims.getSubject();
                password = claims.get("password", String.class);
            } else {
                log.error("Refresh token is missing or damaged");
                throw new RuntimeException("Refresh token is missing or damaged");
            }
            User user = userService.getUserByEmail(email);
            log.info("user : " + user);
            request.setAttribute("password",password);
            CustomUserDetails customUserDetails = CustomUserDetails.fromUserEntityToCustomUserDetails(user);
            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            email,
                            password,
                            customUserDetails.getAuthorities()));
        } catch (Exception error) {
            log.error(error.getMessage());
            throw new RuntimeException("Unexpected server error.");
        }
    }


    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws ServletException, IOException {
        log.info("Enter in successfulAuthentication");
        String email = auth.getName();
        String password = request.getAttribute("password").toString();
        User user = userService.getUserByEmail(email);
        if (user != null) {
            String refreshToken = "Bearer " + tokenService.createTokenRefresh(
                    user, password);
            tokenService.removeTokenByEmail(user.getEmail());
            tokenService.saveRefreshToken(user.getEmail(), refreshToken);
            Cookie cookieRefresh = cookieService.addCookie("refreshToken", refreshToken.substring(7));
            response.addCookie(cookieRefresh);
        }
        request.setAttribute("email", email);
        request.setAttribute("password", "*****");
        chain.doFilter(request, response);
    }

}

