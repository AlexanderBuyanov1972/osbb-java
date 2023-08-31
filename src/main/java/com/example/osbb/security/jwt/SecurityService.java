package com.example.osbb.security.jwt;

import com.example.osbb.entity.authorization.Role;
import com.example.osbb.entity.authorization.User;
import com.example.osbb.exceptions.AuthException;
import com.example.osbb.service.user.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final IUserService iUserService;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSecond;
    @Value("${jwt.issuer}")
    private String issuer;



    public TokenDetails authenticate(String username, String password){
           User user = iUserService.getUser(username);
           if(user == null) {
               throw new AuthException("Invalid username", "USER_USERNAME_INVALID");
           }
           if(!user.isEnabled()){
               throw new AuthException("Account disabled", "USER_ACCOUNT_DISABLED");
           }
           if(passwordEncoder.matches(password, user.getPassword())){
               throw new AuthException("Invalid password", "USER_PASSWORD_INVALID");
           }
           return generateToken(user);
    }

    private TokenDetails generateToken(User user){
            Map<String, Object> claims = new HashMap<>(){{
            put("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
            put("username", user.getUsername());
        }};
        String subject = String.valueOf(user.getId());

        return generateToken(user.getId(), claims, subject);
    }

    private TokenDetails generateToken(Long userId,  Map<String, Object> claims,String subject){
        long expirationDateInMilliseconds = expirationInSecond * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationDateInMilliseconds);
        return generateToken(userId, expirationDate, claims, subject);
    }

    private TokenDetails generateToken(Long userId, Date expirationDate, Map<String, Object> claims,String subject){
        Date createdDate = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.ES256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return TokenDetails.builder()
                .userId(userId)
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();
    }

}

//    private Long userId;
//    private String token;
//    private Date issuedAt;
//    private Date expiresAt;
