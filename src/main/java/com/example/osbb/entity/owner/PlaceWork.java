package com.example.osbb.entity.owner;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "places_work")
public class PlaceWork {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;
    @Column(name = "business_name")
    private String businessName;
    @Column(name = "address")
    private String address;
    @Column(name = "number_phone")
    private String numberPhone;
    @Column(name = "position")
    private String position;
    @Column(name = "addition")
    private String addition;

    @OneToOne(mappedBy = "placeWork")
    @JsonIgnore
    private Owner owner;

}

//    id
//    businessName
//    address
//    numberPhone
//    position
//    addition
//    owner
