package com.example.osbb.security.user_details;

import com.example.osbb.security.entity.User;
import com.example.osbb.security.jwt.filters.AuthenticationFilter;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomUserDetails implements UserDetails {
    private static final Logger log = Logger.getLogger(AuthenticationFilter.class);
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static CustomUserDetails fromUserEntityToCustomUserDetails(User user) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info("Method fromUserEntityToCustomUserDetails : enter");
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.username = user.getEmail();
        log.info(" userDetails.username = " + userDetails.username);
        userDetails.password = user.getPassword();
        log.info("userDetails.password = " + userDetails.password);
        userDetails.grantedAuthorities = Stream.of(user.getRole())
                .map(el -> "ROLE_" + el)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        //userDetails.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole()));
        log.info("userDetails.grantedAuthorities = " + userDetails.grantedAuthorities);
        log.info("userDetails : " + userDetails);
        log.info("Method fromUserEntityToCustomUserDetails : exit");
        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

