package com.example.osbb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntryBalanceHouse {
    private String bill;
    private String apartment;
    private Double summa;

}
