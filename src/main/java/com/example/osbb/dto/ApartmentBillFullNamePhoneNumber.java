package com.example.osbb.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ApartmentBillFullNamePhoneNumber {
    private String apartment;
    private String bill;
    private String fullName;
    private String phoneNumber;
}
