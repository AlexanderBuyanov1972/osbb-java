package com.example.osbb.service;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.security.dao.TokenDAO;
import com.example.osbb.security.entity.RefreshToken;
import com.example.osbb.security.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class TokenService {
    private static final Logger log = Logger.getLogger(TokenService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    private TokenDAO tokenDAO;
    private final String AUTHORIZATION = "Authorization";

    @Value("$(jwt.secret.access)")
    private String secretAccessToken;
    @Value("$(jwt.secret.refresh)")
    private String secretRefreshToken;

    @Value("#{T(Long).parseLong('${lifetime.token.access}')}")
    private long expirationAccessToken;

    @Value("#{T(Long).parseLong('${lifetime.token.refresh}')}")
    private long expirationRefreshToken;
    private Long time = 24 * 60 * 60 * 1000L;

    public void saveRefreshToken(String email, String refreshToken) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            if (!email.isEmpty() && !refreshToken.isEmpty()) {
                tokenDAO.removeByEmail(email);
                tokenDAO.save(RefreshToken.builder()
                        .email(email)
                        .token(refreshToken)
                        .build());
            }
            log.info(messageExit(methodName));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }


    public RefreshToken getTokenByEmail(String email) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            return !email.isEmpty() ? tokenDAO.findTokenByEmail(email) : null;
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    public void removeTokenByEmail(String email) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            RefreshToken token = getTokenByEmail(email);
            if (token != null)
                tokenDAO.delete(token);
            log.info(messageExit(methodName));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }


    public String createTokenAccess(User user) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            long now = System.currentTimeMillis();
            List<String> authorities = List.of(user.getRole().toString());
            log.info(messageExit(methodName));
            return Jwts.builder()
                    .setSubject(user.getEmail())
                    .claim(AUTHORIZATION, authorities)
                    .setIssuedAt(new Date(now))
                    .setExpiration(new Date(now + expirationAccessToken * time))
                    .signWith(SignatureAlgorithm.HS256, secretAccessToken.getBytes(StandardCharsets.UTF_8))
                    .compact();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }


    public String createTokenRefresh(User user, String password) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            long now = System.currentTimeMillis();
            List<String> authorities = List.of(user.getRole().toString());
            log.info(messageExit(methodName));
            return Jwts.builder()
                    .setIssuer("Stormpath")
                    .setSubject(user.getEmail())
                    .claim(AUTHORIZATION, authorities)
                    .claim("password", password)
                    .setIssuedAt(new Date(now))
                    .setExpiration(new Date(now + expirationRefreshToken * time))
                    .signWith(SignatureAlgorithm.HS256, secretRefreshToken.getBytes(StandardCharsets.UTF_8))
                    .compact();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }


    public boolean validateToken(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    public Claims getClaimsAccess(String token) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            return Jwts.parser()
                    .setSigningKey(secretAccessToken.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    public Claims getClaimsRefresh(String token) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            return Jwts.parser()
                    .setSigningKey(secretRefreshToken.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}

