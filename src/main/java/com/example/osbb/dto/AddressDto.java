package com.example.osbb.dto;

public class AddressDto {
    private String city;
    private String street;
    private String house;

    public static AddressDto getAddressDto() {
        return new AddressDto("Каменское", "Свободы", "51");
    }

    public AddressDto() {
    }

    public AddressDto(String city, String street, String house) {
        this.city = city;
        this.street = street;
        this.house = house;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHouse() {
        return house;
    }

    public AddressDto setCity(String city) {
        this.city = city;
        return this;
    }

    public AddressDto setStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressDto setHouse(String house) {
        this.house = house;
        return this;
    }

    @Override
    public String toString() {
        return "AddressDto = { city = " + city + ", street = " + street + ", house = " + house + " }";
    }
}
