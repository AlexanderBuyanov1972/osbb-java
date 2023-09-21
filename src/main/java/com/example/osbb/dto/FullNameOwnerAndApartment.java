package com.example.osbb.dto;

import com.example.osbb.enums.TypeOfAnswer;
import lombok.*;

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
