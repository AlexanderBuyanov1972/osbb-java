package com.example.osbb.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomPrincipal implements Principal {

    private Long id;
    private String name;

    @Override
    public String getName() {
        return this.name;
    }

}
