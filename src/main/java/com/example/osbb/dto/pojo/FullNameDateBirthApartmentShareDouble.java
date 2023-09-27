package com.example.osbb.dto.pojo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullNameDateBirthApartmentShareDouble {
    private String fullName;
    private LocalDate dateBirth;
    private String apartment;
    private Double share;
}
