package com.example.osbb.security.jwt.filters;

import com.example.osbb.dto.Response;
import com.example.osbb.security.service.token.ITokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final ITokenService iTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String username = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.info("authHeader : {}", authHeader);
            token = authHeader.substring(7);
            log.info("Token после удаления семи первых символов : {}", token);
            try {
                username = iTokenService.getUsernameFromAccessToken(token);
                log.info("Username ; {}", username);
            } catch (ExpiredJwtException e) {
                log.debug("Время жизни токена закончилось");
            } catch (SignatureException e) {
                log.debug("Подпись токена повреждена");
            } catch (JwtException | IllegalArgumentException e) {
                log.debug("Токен повреждён, {}", e.getMessage());
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<SimpleGrantedAuthority> authorityList = iTokenService.getRolesFromAccessToken(token)
                    .stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
            log.info("authorityList : {}", authorityList);
            UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorityList);
            SecurityContextHolder.getContext().setAuthentication(upat);
        }
        filterChain.doFilter(request, response);
    }
}
