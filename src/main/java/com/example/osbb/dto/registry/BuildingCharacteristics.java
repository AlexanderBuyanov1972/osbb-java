package com.example.osbb.dto.registry;

import com.example.osbb.dto.AddressDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuildingCharacteristics {
    private String countRooms;
    private String countApartment;
    private String countNonResidentialRoom;
    private String summaAreaRooms;
    private String summaAreaApartment;
    private String summaAreaNonResidentialRoom;
    private AddressDto addressDto;

}
