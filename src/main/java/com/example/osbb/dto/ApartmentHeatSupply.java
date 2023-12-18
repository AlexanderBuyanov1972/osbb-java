package com.example.osbb.dto;

import com.example.osbb.entity.ownership.Ownership;

public class ApartmentHeatSupply {
    private String apartment;
    private String heatSupply;

    public ApartmentHeatSupply(Ownership ownership) {
        this.apartment = ownership.getAddress().getApartment();
        this.heatSupply = ownership.getHeatSupply().toString();
    }

    public ApartmentHeatSupply() {
    }

    public ApartmentHeatSupply(String apartment, String heatSupply) {
        this.apartment = apartment;
        this.heatSupply = heatSupply;
    }

    public String getApartment() {
        return apartment;
    }

    public String getHeatSupply() {
        return heatSupply;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public void setHeatSupply(String heatSupply) {
        this.heatSupply = heatSupply;
    }

    @Override
    public String toString() {
        return "ApartmentHeatSupply = { apartment = " + apartment + ", heatSupply = " + heatSupply + " }";
    }
}
