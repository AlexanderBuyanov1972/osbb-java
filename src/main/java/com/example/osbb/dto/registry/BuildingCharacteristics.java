package com.example.osbb.dto.registry;

import com.example.osbb.dto.AddressDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuildingCharacteristics {
    private Long countRooms;
    private Long countApartment;
    private Long countNonResidentialRoom;
    private Double summaAreaRooms;
    private Double summaAreaApartment;
    private Double summaAreaLivingApartment;
    private Double summaAreaNonResidentialRoom;
    private AddressDto addressDto;

}
