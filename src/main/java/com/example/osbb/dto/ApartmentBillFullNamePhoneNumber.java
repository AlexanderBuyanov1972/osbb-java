package com.example.osbb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApartmentBillFullNamePhoneNumber {
    private String apartment;
    private String bill;
    private String fullName;
    private String phoneNumber;
}
