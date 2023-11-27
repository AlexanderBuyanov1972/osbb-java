package com.example.osbb.security.dto;

import lombok.*;

@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
    private String role;
}
