package com.example.osbb.dto;

import com.example.osbb.entity.User;
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