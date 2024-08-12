package com.example.osbb.security.service.token;

import com.example.osbb.security.dao.TokenDAO;
import com.example.osbb.security.entity.RefreshToken;
import com.example.osbb.security.entity.Role;
import com.example.osbb.security.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TokenService implements ITokenService {

    @Autowired
    private TokenDAO tokenDAO;

    @Value("${jwt.secret.access}")
    private String secretAccessToken;

    @Value("${jwt.secret.refresh}")
    private String secretRefreshToken;

    @Value("#{T(Long).parseLong('${lifetime.token.access}')}")
    private long expirationAccessToken;

    @Value("#{T(Long).parseLong('${lifetime.token.refresh}')}")
    private long expirationRefreshToken;

    private final Long time = 24 * 60 * 60 * 1000L;

    public void saveRefreshToken(String username, String refreshToken) {
        tokenDAO.removeByUsername(username);
        tokenDAO.save(RefreshToken.builder()
                .username(username)
                .token(refreshToken)
                .build());
    }

    @Override
    public RefreshToken getTokenByUsername(String username) {
        return !username.isEmpty() ? tokenDAO.findTokenByUsername(username) : null;
    }

    @Override
    public void removeTokenByUsername(String username) {
        RefreshToken token = getTokenByUsername(username);
        if (token != null)
            tokenDAO.delete(token);
    }

    @Override
    public String createTokenAccess(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getRoles().stream().map(Role::getName).toList())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationAccessToken * time))
                .signWith(getSignInKey(secretAccessToken))
                .compact();
    }

    @Override
    public String createTokenRefresh(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getRoles().stream().map(Role::getName).toList())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationRefreshToken * time))
                .signWith(getSignInKey(secretRefreshToken))
//                .signWith(SignatureAlgorithm.HS256, getSignInKey(secretRefreshToken))
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    @Override
    public Claims getClaimsAccess(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey(secretAccessToken))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public Claims getClaimsRefresh(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey(secretRefreshToken))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public String getUsernameFromAccessToken(String token) {
        try {
            return getClaimsAccess(token).getSubject();
        } catch (Exception e) {
            log.error("Access Токен повреждён : {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String getUsernameFromRefreshToken(String token) {
        try {
            return getClaimsRefresh(token).getSubject();
        } catch (Exception e) {
            log.error("Refresh Токен повреждён : {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> getRolesFromAccessToken(String token) {
        return getClaimsAccess(token).get("roles", List.class);
    }

    private SecretKey getSignInKey(String secret) {
        byte[] bytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

}

