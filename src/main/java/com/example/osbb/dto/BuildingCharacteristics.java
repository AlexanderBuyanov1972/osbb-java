package com.example.osbb.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuildingCharacteristics {
    private Long countOwners;
    private Long countRooms;
    private Long countApartment;
    private Long countNonResidentialRoom;
    private Double summaTotalArea;
    private Double summaTotalAreaApartment;
    private Double summaLivingAreaApartment;
    private Double summaTotalAreaNonResidentialRoom;
    private AddressDto addressDto;

}
