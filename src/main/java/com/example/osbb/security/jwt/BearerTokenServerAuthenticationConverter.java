package com.example.osbb.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
@RequiredArgsConstructor
public class BearerTokenServerAuthenticationConverter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtHandler jwtHandler;

//    private final UserAuthenticationBearer userAuthenticationBearer;

    
//    private static final Function<String, String> getBearer = authValue -> authValue.substring(BEARER_PREFIX.length());

    public Authentication converter(ServerWebExchange exchange){
        String tokenWithBearer = extractHeader(exchange);
        String token = getBearer(tokenWithBearer);
        JwtHandler.VerificationResult result = jwtHandler.check(token);
        return UserAuthenticationBearer.create(result);
    }
    private String getBearer(String line){
        return line.substring(BEARER_PREFIX.length());
    }

    private String extractHeader(ServerWebExchange exchange){
        return exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    }

}
