package com.example.osbb.entity.ownership;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
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
