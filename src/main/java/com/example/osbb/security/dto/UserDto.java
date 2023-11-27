package com.example.osbb.security.dto;

import com.example.osbb.security.entity.User;
import lombok.*;

@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private String username;
    private String email;
    private String role;
    private boolean activated;
    private String activationLink;

    public UserDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().toString();
        this.activated = user.isActivated();
        this.activationLink = user.getActivationLink();
    }


}