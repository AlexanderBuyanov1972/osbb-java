package com.example.osbb.security.dto;

import com.example.osbb.security.entity.Role;
import com.example.osbb.security.entity.User;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private boolean activated;
    private String activationLink;
    private List<String> roles;

    public UserDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(Role::getName).toList();
        this.activated = user.isActivated();
        this.activationLink = user.getActivationLink();
    }


}