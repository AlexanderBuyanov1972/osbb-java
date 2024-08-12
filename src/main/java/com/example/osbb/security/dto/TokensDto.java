package com.example.osbb.security.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokensDto {
    private String accessToken;
    private String refreshToken;
}

