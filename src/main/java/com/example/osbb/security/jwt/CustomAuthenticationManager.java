package com.example.osbb.security.jwt;

import com.example.osbb.entity.authorization.User;
import com.example.osbb.exceptions.UnauthorizedException;
import com.example.osbb.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final IUserService iUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        User user = (User) iUserService.getUser(principal.getId());
        if(!user.isEnabled()){
            throw new UnauthorizedException("Account disabled");
        }
        return authentication;
    }
}
