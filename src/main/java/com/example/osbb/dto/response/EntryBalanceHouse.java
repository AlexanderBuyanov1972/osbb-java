package com.example.osbb.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntryBalanceHouse {
    private String bill;
    private String apartment;
    private Double summa;
}
