package com.example.osbb.entity.owner;

import com.example.osbb.entity.owner.Owner;
import com.example.osbb.enums.TypeOfColor;
import com.example.osbb.enums.TypeOfManufacturer;
import com.example.osbb.enums.TypeOfVehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_vehicle")
    @Enumerated(EnumType.STRING)
    private TypeOfVehicle typeVehicle;

    @Column(name = "number_vehicle")
    private String numberVehicle;

    @Column(name = "year_of_issue")
    private String yearOfIssue;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private TypeOfColor typeColor;

    @Column(name = "type_manufacturer")
    @Enumerated(EnumType.STRING)
    private TypeOfManufacturer typeManufacturer;

    // --------- many to one ---------------

    @OneToOne(mappedBy = "vehicle")
    @JsonIgnore
    private Owner owner;

}

//    id
//    typeVehicle
//    numberVehicle
//    yearOfIssue
//    typeColor
//    typeManufacturer
//    owner
