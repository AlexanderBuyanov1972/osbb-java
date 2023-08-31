package com.example.osbb.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDetails {
    private Long userId;
    private String token;
    private Date issuedAt;
    private Date expiresAt;
}
