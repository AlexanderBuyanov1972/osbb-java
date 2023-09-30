package com.example.osbb.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AddressDto {
    private String city;
    private String street;
    private String house;

    public static AddressDto getAddressDto() {
        return AddressDto.builder().city("Каменское").street("Свободы").house("51").build();
    }


}
