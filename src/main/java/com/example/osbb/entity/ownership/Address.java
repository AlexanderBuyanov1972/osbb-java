package com.example.osbb.entity.ownership;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    // ------- setters -------------
    public Address setId(long id) {
        this.id = id;
        return this;
    }

    public Address setFloor(String floor) {
        this.floor = floor;
        return this;
    }

    public Address setApartment(String apartment) {
        this.apartment = apartment;
        return this;
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