package com.example.osbb.dto;

import lombok.*;
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdAndBill {
    private Long id;
    private String bill;
}
