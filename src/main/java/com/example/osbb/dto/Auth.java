package com.example.osbb.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Auth {
    private String username;
    private String password;
}
