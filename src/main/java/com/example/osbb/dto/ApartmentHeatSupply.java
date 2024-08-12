package com.example.osbb.dto;

import com.example.osbb.entity.ownership.Ownership;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApartmentHeatSupply {
    private String apartment;
    private String heatSupply;

    public ApartmentHeatSupply(Ownership ownership) {
        this.apartment = ownership.getAddress().getApartment();
        this.heatSupply = ownership.getHeatSupply().toString();
    }
}
