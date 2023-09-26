package com.example.osbb.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FullNameOwnerAndApartment {
    private String fullname;
    private Double shareInRealEstate;
    private String apartment;
    private Double totalArea;


}
