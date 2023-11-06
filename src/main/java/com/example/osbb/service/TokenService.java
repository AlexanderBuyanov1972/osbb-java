package com.example.osbb.service;

import com.example.osbb.dao.TokenDAO;
import com.example.osbb.entity.RefreshToken;
import com.example.osbb.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TokenService {
    @Autowired
    private TokenDAO tokenDAO;
    private final String AUTHORIZATION = "Authorization";
    @Value("$(jwt.secret.access)")
    private String secretAccessToken;
    @Value("#{T(Long).parseLong('${lifetime.token.access}')}")
    private long expirationAccessToken;
    @Value("$(jwt.secret.refresh)")
    private String secretRefreshToken;
    @Value("#{T(Long).parseLong('${lifetime.token.refresh}')}")
    private long expirationRefreshToken;
    private Long time = 24 * 60 * 60 * 1000L;

    public void saveRefreshToken(String email, String refreshToken) {
        if (!email.isEmpty() && !refreshToken.isEmpty()) {
            tokenDAO.removeByEmail(email);
            tokenDAO.save(RefreshToken.builder()
                    .email(email)
                    .token(refreshToken)
                    .build());
        }
    }


    public RefreshToken getTokenByEmail(String email) {
        return !email.isEmpty() ? tokenDAO.findTokenByEmail(email) : null;
    }

    public void removeTokenByEmail(String email) {
        RefreshToken token = getTokenByEmail(email);
        if (token != null) {
            tokenDAO.delete(token);
        }
    }


    public String createTokenAccess(User user) {
        long now = System.currentTimeMillis();
        List<String> authorities = List.of(user.getRole().toString());
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim(AUTHORIZATION, authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationAccessToken * time))
                .signWith(SignatureAlgorithm.HS256, secretAccessToken)
                .compact();
    }


    public String createTokenRefresh(User user, String password) {
        long now = System.currentTimeMillis();
        List<String> authorities = List.of(user.getRole().toString());
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim(AUTHORIZATION, authorities)
                .claim("password", password)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationRefreshToken * time))
                .signWith(SignatureAlgorithm.HS256, secretRefreshToken)
                .compact();
    }


    public boolean validateToken(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    public Claims getClaimsAccess(String token) {
        return Jwts.parser()
                .setSigningKey(secretAccessToken)
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getClaimsRefresh(String token) {
        return Jwts.parser()
                .setSigningKey(secretRefreshToken)
                .parseClaimsJws(token)
                .getBody();
    }

}

