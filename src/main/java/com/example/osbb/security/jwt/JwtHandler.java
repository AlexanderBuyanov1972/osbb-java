package com.example.osbb.security.jwt;

import com.example.osbb.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Base64;
import java.util.Date;

public class JwtHandler {
    private final String secret;

    public JwtHandler(String secret) {
        this.secret = secret;
    }

    VerificationResult check(String accessToken){
        try{
            return verify(accessToken);
        }catch(Exception ex){
            throw new UnauthorizedException(ex.getMessage());
        }

    }

    private VerificationResult verify(String token){
        Claims claims = getClaimsFromToken(token);
        final Date expirationDate = claims.getExpiration();
        if(expirationDate.before(new Date())){
            throw new RuntimeException("Token expired");
        }
        return new VerificationResult(claims, token);
    }

    private Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public static class VerificationResult{
        protected Claims claims;
        protected String token;

        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }
}
