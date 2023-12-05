package com.example.osbb.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Debt {
    private HeaderDebt header;
    private BodyDebt body;
}
