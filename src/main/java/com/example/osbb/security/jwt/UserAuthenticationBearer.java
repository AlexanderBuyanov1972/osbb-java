package com.example.osbb.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class UserAuthenticationBearer {
    public static Authentication create(JwtHandler.VerificationResult verificationResult){

        Claims claims = verificationResult.claims;

        String subject = claims.getSubject();

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");

        String username = claims.get("username", String.class);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        Long principalId = Long.parseLong(subject);

        CustomPrincipal principal = new CustomPrincipal(principalId, username);

return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }
}
