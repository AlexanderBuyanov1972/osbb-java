package com.example.osbb.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Auth {
    private String username;
    private String password;


}
