package com.example.osbb.dto.queries;

import com.example.osbb.entity.ownership.Ownership;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ApartmentHeatSupply {
    private String apartment;
    private String heatSupply;

    public ApartmentHeatSupply(Ownership ownership){
        this.apartment = ownership.getAddress().getApartment();
        this.heatSupply = ownership.getHeatSupply().toString();
    }


}
