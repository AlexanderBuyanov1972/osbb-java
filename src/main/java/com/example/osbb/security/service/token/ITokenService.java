package com.example.osbb.security.service.token;

import com.example.osbb.security.entity.RefreshToken;
import com.example.osbb.security.entity.User;
import io.jsonwebtoken.Claims;

import java.util.List;

public interface ITokenService {

    void saveRefreshToken(String username, String refreshToken);

    RefreshToken getTokenByUsername(String username);

    void removeTokenByUsername(String username);

    String createTokenAccess(User user);

    String createTokenRefresh(User user);

    boolean validateToken(String token);

    Claims getClaimsAccess(String token);

    Claims getClaimsRefresh(String token);

    String getUsernameFromAccessToken(String token);

    String getUsernameFromRefreshToken(String token);

    List<String> getRolesFromAccessToken(String token);
}
