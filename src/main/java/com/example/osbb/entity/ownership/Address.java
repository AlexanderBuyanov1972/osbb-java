package com.example.osbb.entity.ownership;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Value("${address.zipcode}")
    @Column(name = "zipCode", nullable = false)
    private String zipCode;

    @Value("${address.country}")
    @Column(name = "country", nullable = false)
    private String country;

    @Value("${address.region}")
    @Column(name = "region", nullable = false)
    private String region;

    @Value("${address.city}")
    @Column(name = "city", nullable = false)
    private String city;

    @Value("${address.street}")
    @Column(name = "street", nullable = false)
    private String street;

    @Value("${address.house}")
    @Column(name = "house", nullable = false)
    private String house;

    @Column(name = "entrance")
    private String entrance;

    @Column(name = "floor")
    private String floor;

    @Column(name = "apartment")
    private String apartment;

    @OneToOne(mappedBy = "address")
    @JsonIgnore
    private Ownership ownership;

    public Address() {
    }

    public Address(long id, String zipCode, String country, String region, String city, String street, String house, String entrance, String floor, String apartment, Ownership ownership) {
        this.id = id;
        this.zipCode = zipCode;
        this.country = country;
        this.region = region;
        this.city = city;
        this.street = street;
        this.house = house;
        this.entrance = entrance;
        this.floor = floor;
        this.apartment = apartment;
        this.ownership = ownership;
    }

    public Address(String zipCode, String country, String region, String city,
                   String street, String house, String entrance, String floor, String apartment) {
        this.zipCode = zipCode;
        this.country = country;
        this.region = region;
        this.city = city;
        this.street = street;
        this.house = house;
        this.entrance = entrance;
        this.floor = floor;
        this.apartment = apartment;
    }

    public long getId() {
        return id;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
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

    public String getEntrance() {
        return entrance;
    }

    public String getFloor() {
        return floor;
    }

    public String getApartment() {
        return apartment;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public Address setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public Address setCountry(String country) {
        this.country = country;
        return this;
    }

    public Address setRegion(String region) {
        this.region = region;
        return this;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    public Address setStreet(String street) {
        this.street = street;
        return this;
    }

    public Address setHouse(String house) {
        this.house = house;
        return this;
    }

    public Address setEntrance(String entrance) {
        this.entrance = entrance;
        return this;
    }

    public Address setOwnership(Ownership ownership) {
        this.ownership = ownership;
        return this;
    }

    @Override
    public String toString() {
        return "Address = { id = " + id + ", zipCode = " + zipCode + ", country = " + country +
                ", region = " + region + ", city = " + city + ", street = " + street +
                ", house = " + house + ", entrance = " + entrance + ", floor = " + floor +
                ", apartment = " + apartment + ", ownership = " + ownership + "}";
    }
}


//    id
//    zipCode
//    country
//    region
//    city
//    street
//    house
//    entrance
//    floor
//    apartment
//    ownership
