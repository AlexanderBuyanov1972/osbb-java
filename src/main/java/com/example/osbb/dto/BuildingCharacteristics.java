package com.example.osbb.dto;


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

    public BuildingCharacteristics() {
    }

    public BuildingCharacteristics(Long countOwners, Long countRooms, Long countApartment, Long countNonResidentialRoom, Double summaTotalArea, Double summaTotalAreaApartment, Double summaLivingAreaApartment, Double summaTotalAreaNonResidentialRoom, AddressDto addressDto) {
        this.countOwners = countOwners;
        this.countRooms = countRooms;
        this.countApartment = countApartment;
        this.countNonResidentialRoom = countNonResidentialRoom;
        this.summaTotalArea = summaTotalArea;
        this.summaTotalAreaApartment = summaTotalAreaApartment;
        this.summaLivingAreaApartment = summaLivingAreaApartment;
        this.summaTotalAreaNonResidentialRoom = summaTotalAreaNonResidentialRoom;
        this.addressDto = addressDto;
    }

    public Long getCountOwners() {
        return countOwners;
    }

    public Long getCountRooms() {
        return countRooms;
    }

    public Long getCountApartment() {
        return countApartment;
    }

    public Long getCountNonResidentialRoom() {
        return countNonResidentialRoom;
    }

    public Double getSummaTotalArea() {
        return summaTotalArea;
    }

    public Double getSummaTotalAreaApartment() {
        return summaTotalAreaApartment;
    }

    public Double getSummaLivingAreaApartment() {
        return summaLivingAreaApartment;
    }

    public Double getSummaTotalAreaNonResidentialRoom() {
        return summaTotalAreaNonResidentialRoom;
    }

    public AddressDto getAddressDto() {
        return addressDto;
    }

    public BuildingCharacteristics setCountOwners(Long countOwners) {
        this.countOwners = countOwners;
        return this;
    }

    public BuildingCharacteristics setCountRooms(Long countRooms) {
        this.countRooms = countRooms;
        return this;
    }

    public BuildingCharacteristics setCountApartment(Long countApartment) {
        this.countApartment = countApartment;
        return this;
    }

    public BuildingCharacteristics setCountNonResidentialRoom(Long countNonResidentialRoom) {
        this.countNonResidentialRoom = countNonResidentialRoom;
        return this;
    }

    public BuildingCharacteristics setSummaTotalArea(Double summaTotalArea) {
        this.summaTotalArea = summaTotalArea;
        return this;
    }

    public BuildingCharacteristics setSummaTotalAreaApartment(Double summaTotalAreaApartment) {
        this.summaTotalAreaApartment = summaTotalAreaApartment;
        return this;
    }

    public BuildingCharacteristics setSummaLivingAreaApartment(Double summaLivingAreaApartment) {
        this.summaLivingAreaApartment = summaLivingAreaApartment;
        return this;
    }

    public BuildingCharacteristics setSummaTotalAreaNonResidentialRoom(Double summaTotalAreaNonResidentialRoom) {
        this.summaTotalAreaNonResidentialRoom = summaTotalAreaNonResidentialRoom;
        return this;
    }

    public BuildingCharacteristics setAddressDto(AddressDto addressDto) {
        this.addressDto = addressDto;
        return this;
    }

    @Override
    public String toString() {
        return "BuildingCharacteristics = { countOwners = " + countOwners + ", countRooms = " + countRooms +
                ", countApartment = " + countApartment + ", countNonResidentialRoom = " + countNonResidentialRoom +
                ", summaTotalArea = " + summaTotalArea + ", summaTotalAreaApartment = " + summaTotalAreaApartment +
                ", summaLivingAreaApartment = " + summaLivingAreaApartment +
                ", summaTotalAreaNonResidentialRoom = " + summaTotalAreaNonResidentialRoom +
                ", addressDto = " + addressDto + " }";
    }
}
